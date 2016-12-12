angular.module('os.biospecimen.consentstatement.addedit', ['os.biospecimen.models'])
  .controller('ConsentStatementAddEditCtrl', function($scope, $state, consentStatement) {

    var init = function() {
      $scope.consentStatement = consentStatement; 
    }

    $scope.save = function() {
      $scope.consentStatement.$saveOrUpdate().then(
        function() {
          $state.go('consent-statement-list');
        }
      );
    }

    init();
  });
