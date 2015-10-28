
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function($scope, $state, DistributionProtocol, Util, PvManager, DeleteUtil) {

    function init() {
      $scope.dpFilterOpts = {includeStats: true};
      $scope.loadDps($scope.dpFilterOpts);
      Util.filter($scope, 'dpFilterOpts', filter);
      loadActivityStatuses();
    }
    
    $scope.loadDps = function(filterOpts) {
      DistributionProtocol.query(filterOpts).then(
        function(result) {
          $scope.distributionProtocols = result; 
        }
      );
    }

    function filter(filterOpts) {
      var dpFilterOpts = angular.copy(filterOpts);
      if (dpFilterOpts.pi) {
        dpFilterOpts.piId = dpFilterOpts.pi.id;
        delete dpFilterOpts.pi;
      }

      $scope.loadDps(dpFilterOpts);
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
