
angular.module('os.biospecimen.cp.consents', ['os.biospecimen.models'])
  .controller('CpConsentsCtrl', function($scope, $state, $q, cp, consentTiers, DeleteUtil, Alerts,
    AuthorizationService, ConsentStatement) {

    var defStmts;

    function init() {
      $scope.cp = cp;
      $scope.consentCtx = {
        allowEdit: AuthorizationService.isAllowed($scope.cpResource.updateOpts),
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
      return addDisplayValue(consentTier, consentTier.consentStmtCode, consentTier.statement);
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
        return cp.newConsentTier({consentStmtCode: stmt.itemKey}).$saveOrUpdate().then(initConsentTier);
      } else if (action == 'update') {
        return cp.newConsentTier({
          id: stmt.id,
          newConsentStmtCode: stmt.displayValue
        }).$saveOrUpdate().then(initConsentTier);
      } else if (action == 'remove') {
        var deferred = $q.defer();
        var opts = {
          confirmDelete: 'cp.delete_consent_tier',
          onDeletion: function() { deferred.resolve(true); },
          onDeleteFail: function() { deferred.reject(); }
        }

        DeleteUtil.delete(stmt, opts);
        return deferred.promise;
      }

      return undefined;
    };

    $scope.updateConsentsWaived = function() {
      $scope.cp.updateConsentsWaived().then(
        function(result) {
          Alerts.success("cp.consents_waived_updated", {waived: result.consentsWaived});
        }
      );
    }

    init();


  });
