
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function($scope, $state, $stateParams, DistributionProtocol, Util, PvManager, DeleteUtil) {

    function init() {
      $scope.dpFilterOpts = {includeStats: true};
      if ($stateParams.reload != "false") {
        $scope.loadDps($scope.dpFilterOpts);
      }

      loadActivityStatuses();
    }
    
    $scope.loadDps = function(filterOpts) {
      DistributionProtocol.query(filterOpts).then(
        function(result) {
          $scope.distributionProtocols = result; 
        }
      );
    }

    $scope.showDpOverview = function(distributionProtocol) {
      $state.go('dp-detail.overview', {dpId:distributionProtocol.id});
    };
    
    function loadActivityStatuses () {
      PvManager.loadPvs('activity-status').then(
        function (result) {
          $scope.activityStatuses = [];
          angular.forEach(result, function (status) {
            if (status != 'Disabled' && status != 'Pending') {
              $scope.activityStatuses.push(status);
            }
          });
        }
      );
    }

    $scope.deleteDp = function(distributionProtocol) {
      DeleteUtil.delete(distributionProtocol, {onDeletion: $scope.loadDps});
    }

    init();
  });
