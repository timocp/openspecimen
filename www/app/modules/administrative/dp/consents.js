
angular.module('os.administrative.dp.consents', ['os.administrative.models'])
  .controller('DpConsentsCtrl',function(
    $scope, $http, $q, distributionProtocol,
    currentUser, consentTiers, DeleteUtil, ConsentStatement) {

    var defStmts;

    function init() {
      $scope.consentCtx = {
        allowEdit: currentUser.admin || currentUser.instituteAdmin,
        tiers: initConsentTiers(consentTiers),
        stmts: []
      };

      loadConsentStmts();
    }

    function initConsentTiers(consentTiers) {
      angular.forEach(consentTiers, initConsentTier);
      return consentTiers;
    }

    function initConsentTier(consentTier) {
      return addDisplayValue(consentTier, consentTier.consentStmtCode, consentTier.consentStmt);
    }

    function loadConsentStmts(searchString) {
      if (defStmts && !searchString) {
        $scope.consentCtx.stmts = defStmts;
        return;
      }

      if (defStmts && defStmts.length < 100) {
        return;
      }

      ConsentStatement.query({searchString: searchString}).then(
        function(stmts) {
          $scope.consentCtx.stmts = stmts;
          angular.forEach(stmts,
            function(stmt) {
              addDisplayValue(stmt, stmt.code, stmt.statement);
            }
          );

          if (!searchString) {
            defStmts = stmts;
          }
        }
      );
    }

    function addDisplayValue(obj, code, statement) {
      return angular.extend(obj, {itemKey: code, displayValue: statement + ' (' + code + ')'});
    }

    $scope.loadConsentStmts = loadConsentStmts;

    $scope.listChanged = function(action, stmt) {
      if (action == 'add') {
        return distributionProtocol.newConsentTier({consentStmtCode: stmt.itemKey}).$saveOrUpdate().then(initConsentTier);
      } else if (action == 'remove') {
        var deferred = $q.defer();
        var opts = {
          deleteWithoutCheck: true,
          confirmDelete: 'dp.delete_consent_tier',
          onDeletion: function() { deferred.resolve(true); },
          onDeleteFail: function() { deferred.reject(); }
        }

        stmt.id = stmt.consentStmtId;
        DeleteUtil.delete(stmt, opts);
        return deferred.promise;
      } else if (action == 'update') {
        return distributionProtocol.newConsentTier({
          consentStmtId: stmt.consentStmtId,
          newConsentStmtCode: stmt.displayValue
        }).$saveOrUpdate().then(initConsentTier);
      }

      return undefined;
    };

    init();
  });
