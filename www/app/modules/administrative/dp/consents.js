
angular.module('os.administrative.dp.consents', ['os.administrative.models'])
  .controller('DpConsentsCtrl',function(
    $scope, $http, $q, distributionProtocol,
    currentUser, consentTiers, DeleteUtil, ConsentStatement) {

    function init() {
      $scope.consentCtx = {
        allowEdit: currentUser.admin || currentUser.instituteAdmin,
        tiers: initConsentTiers(consentTiers),
        stmts: [],
      };

      loadConsentStmts();
    }

    function initConsentTiers(consentTiers) {
      angular.forEach(consentTiers,
        function(consentTier) {
          addDisplayValue(consentTier, consentTier.consentStmtCode, consentTier.consentStmt);
        }
      );

      return consentTiers;
    }

    function loadConsentStmts(searchString) {
      ConsentStatement.query({searchString: searchString}).then(
        function(stmts) {
          $scope.consentCtx.stmts = stmts;
          angular.forEach(stmts,
            function(stmt) {
              addDisplayValue(stmt, stmt.code, stmt.statement);
            }
          );
        }
      );
    }

    function addDisplayValue(obj, code, statement) {
      return angular.extend(obj, {itemKey: code, displayValue: statement + ' (' + code + ')'});
    }

    $scope.loadConsentStmts = loadConsentStmts;

    $scope.listChanged = function(action, stmt) {
      if (action == 'add') {
        return distributionProtocol.newConsentTier({consentStmtCode: stmt.itemKey}).$saveOrUpdate().then(
          function(result) {
            return addDisplayValue(result, result.consentStmtCode, result.consentStmt);
          }
        );
      } else if (action == 'remove') {
        var deferred = $q.defer();
        var opts = {
          deleteWithoutCheck: true,
          onDeletion: function() { deferred.resolve(true); },
          onDeleteFail: function() { deferred.reject(); }
        }

        stmt.id = stmt.consentStmtId;
        DeleteUtil.delete(stmt, opts);
        return deferred.promise;
      } else if (action == 'update') {
        //
        // TODO:
        //
        alert("handle: " + action + ": " + JSON.stringify(stmt));
      }

      return undefined;
    };

    init();
  });
