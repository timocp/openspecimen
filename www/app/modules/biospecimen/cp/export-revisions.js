angular.module('os.biospecimen.cp')
  .controller('CpExportRevisionsCtrl', function($scope, cp, Audit, Alerts) {
    function init() {
      $scope.cp = cp;
      $scope.expCtx = {}
    }

    $scope.exportReport = function() {
      var name = 'collection_protocol';
      if ($scope.expCtx.incBiospecimen) {
        name = 'collection_protocol_bio';
      }

      var opts = {from: $scope.expCtx.startDate, to: $scope.expCtx.endDate};
      Audit.exportRevisions(name, cp.id, opts).then(
        function() {
          Alerts.success('common.audit.report_exported');
          $scope.back();
        }
      );
    }

    init();
  });
