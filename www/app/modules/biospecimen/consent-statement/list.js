angular.module('os.biospecimen.consentstatement.list', ['os.biospecimen.models'])
  .controller('ConsentStatementListCtrl', function($scope, $state, ConsentStatement, Util) {

    function init() {
      $scope.consentStatementFilterOpts = {};
      loadConsentStatements($scope.consentStatementFilterOpts);
      Util.filter($scope, 'consentStatementFilterOpts', loadConsentStatements);
    }

    function loadConsentStatements(filterOpts) {
      ConsentStatement.query(filterOpts).then(
        function(consentStatementList) {
          $scope.consentStatementList = consentStatementList;
        }
      );
    }

    $scope.showConsentStatementOverview = function(consentStatement) {
      $state.go('consent-statement-detail.overview', {consentStatementId: consentStatement.id});
    };

    init();
  });

