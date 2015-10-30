angular.module('os.administrative.site.list', ['os.administrative.models'])
  .controller('SiteListCtrl', function($scope, $state, $stateParams, Site, Util, DeleteUtil) {

    function init() {
      $scope.siteFilterOpts = {includeStats: true};

      if ($stateParams.reload != "false") {
        $scope.loadSites($scope.siteFilterOpts);
      }
    }

    $scope.loadSites = function(filterOpts) {
      Site.query(filterOpts).then(
        function(siteList) {
          $scope.siteList = siteList;
        }
      );
    };

    $scope.showSiteOverview = function(site) {
      $state.go('site-detail.overview', {siteId: site.id});
    };

    $scope.deleteSite = function(site) {
      DeleteUtil.delete(site, {onDeletion: $scope.loadSites});
    }

    init();
  });
