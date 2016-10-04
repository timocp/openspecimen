
angular.module('os.common.audit')
  .controller('AuditRevListCtrl', function($scope, opts, Audit) {
    function init() {
      $scope.histCtx = {
        loading  : true,
        revisions: [],
        exportSref: opts.exportSref
      }

      loadRevisions();
    }

    function loadRevisions() {
      Audit.getRevisions(opts.entityName, opts.entityId).then(
        function(revisions) {
          $scope.histCtx.loading   = false;
          $scope.histCtx.revisions = revisions;
        }
      );
    }

    init();
  });
