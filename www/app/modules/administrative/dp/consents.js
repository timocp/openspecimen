
angular.module('os.administrative.dp.consents', ['os.administrative.models'])
  .controller('DpConsentsCtrl', function($scope, distributionProtocol, consentTiers) {
    $scope.dp = distributionProtocol;
    $scope.allowEditConsent = true;

    var consents = {
      tiers: consentTiers,
      stmtAttr: 'consentStmt'
    };

    $scope.consents = consents;
  });
