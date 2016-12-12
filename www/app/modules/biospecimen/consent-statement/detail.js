angular.module('os.biospecimen.consentstatement.detail', ['os.biospecimen.models'])
  .controller('ConsentStatementDetailCtrl', function($scope, consentStatement) {
    $scope.consentStatement = consentStatement;

  });
