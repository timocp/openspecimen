
angular.module('os.administrative.dp.consents', ['os.administrative.models'])
  .controller('DpConsentsCtrl', function($scope, $http, $q, distributionProtocol, 
    currentUser, consentTiers, DeleteUtil, ConsentStatement) {
    function init() {
      $scope.allowEditConsent = currentUser.admin || currentUser.instituteAdmin;

      setConsentTiers();
      loadStmts();

      $scope.consents = {
        tiers: consentTiers,
        list: [],
        stmtAttr: 'displayValue',
        stmtCode: 'consentStmtCode',
        selectProp: 'code'
      };
    }

    function setConsentTiers() {
      angular.forEach(consentTiers, function(input) {
        addDisplayValue(input, input.consentStmtCode, input.consentStmt);
      });
    }

    function loadStmts() {
      ConsentStatement.query().then(
        function(stmts) {
          $scope.consents.list = stmts;
          angular.forEach($scope.consents.list, function(input) {
            addDisplayValue(input, input.code, input.statement);
          });
        }
      );
    }

    function addDisplayValue(input, code, statement) {
      return angular.extend(input, {displayValue: statement + ' (' + code + ') '});
    }

    $scope.listChanged = function(action, stmt) {
      if (action == 'add') {
        return distributionProtocol.newConsentTier(stmt).$saveOrUpdate().then(
          function(result) {
            return addDisplayValue(result, result.consentStmtCode, result.consentStmt);
          }
        );
      } else if (action == 'remove') {
        stmt.id = stmt.consentStmtId;
        var deferred = $q.defer();
        DeleteUtil.delete(
          stmt,
          {
            deleteWithoutCheck: true,
            onDeletion: onConsentDeletion(deferred),
            onDeleteFail: onConsentDeleteFail(deferred)
          }
        );
        return deferred.promise;
      }
      return undefined;
    };

    function onConsentDeletion(deferred) {
      return function() {
        deferred.resolve(true);
      }
    }

    function onConsentDeleteFail(deferred) {
      return function() {
        deferred.reject();
      }
    }

    init();
  });
