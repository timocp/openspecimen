
angular.module('os.administrative.dp.consents', ['os.administrative.models'])
  .controller('DpConsentsCtrl', function($scope, $http, $q, distributionProtocol, consentTiers, DeleteUtil) {
    $scope.dp = distributionProtocol;
    $scope.allowEditConsent = true;

    var consents = {
      tiers: consentTiers,
      stmtAttr: 'consentStmt',
      stmtCode: 'consentStmtCode'
    };

    $scope.consents = consents;

    $scope.listChanged = function(action, stmt) {
      if (action == 'add') {
        stmt = distributionProtocol.newConsentTier(stmt);
        return stmt.$saveOrUpdate();
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
  });
