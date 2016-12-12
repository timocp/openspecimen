
angular.module('os.biospecimen.consentstatement',
  [
    'os.biospecimen.consentstatement.list',
    'os.biospecimen.consentstatement.addedit',
    'os.biospecimen.consentstatement.detail'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('consent-statement-list', {
        url: '/consent-statements',
        templateUrl: 'modules/biospecimen/consent-statement/list.html',
        controller: 'ConsentStatementListCtrl',
        parent: 'signed-in'
      })
      .state('consent-statement-addedit', {
        url: '/consent-statement-addedit/:consentStatementId',
        templateUrl: 'modules/biospecimen/consent-statement/addedit.html',
        resolve: {
          consentStatement: function($stateParams, ConsentStatement) {
            if ($stateParams.consentStatementId) {
              return ConsentStatement.getById($stateParams.consentStatementId);
            }
            return new ConsentStatement();
          }
        },
        controller: 'ConsentStatementAddEditCtrl',
        parent: 'signed-in'
      })
      .state('consent-statement-detail', {
        url: '/consent-statements/:consentStatementId',
        templateUrl: 'modules/biospecimen/consent-statement/detail.html',
        resolve: {
          consentStatement: function($stateParams, ConsentStatement) {
            return ConsentStatement.getById($stateParams.consentStatementId);
          }
        },
        controller: 'ConsentStatementDetailCtrl',
        parent: 'signed-in'
      })
      .state('consent-statement-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/consent-statement/overview.html',
        controller: function() {
        },
        parent: 'consent-statement-detail'
      });
  });

