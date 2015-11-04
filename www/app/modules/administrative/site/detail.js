angular.module('os.administrative.site.detail', ['os.administrative.models'])
  .controller('SiteDetailCtrl', function($scope, $q, $state, site, Institute, PvManager, DeleteUtil, Alerts) {

    function init() {
      $scope.site = site;
      $scope.institutes = [];
      loadPvs();
    }

    function loadPvs() {
      $scope.siteTypes = PvManager.getPvs('site-type');

      Institute.query().then(
        function(instituteList) {
          angular.forEach(instituteList, function(institute) {
            $scope.institutes.push(institute.name);
          });
        }
      );
    }

    $scope.editSite = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteSite = function() {
      DeleteUtil.delete($scope.site, {onDeleteState: 'site-list'});
    }

    $scope.archiveSite = function() {
      $scope.site.$patch({activityStatus: "Closed"}).then(
        function(updatedSite) {
          Alerts.success('site.archived_successfully', {name: updatedSite.name});
          $state.go('site-list');
        }
      );
    }


    $scope.getCoordinatorDisplayText = function(coordinator) {
      return coordinator.firstName + ' ' + coordinator.lastName;
    }

    init();
  });
