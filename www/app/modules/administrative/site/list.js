angular.module('os.administrative.site.list', ['os.administrative.models'])
  .controller('SiteListCtrl', function($scope, $state, Site, Util, DeleteUtil) {

    function init() {
      $scope.siteFilterOpts = {includeStats: true};
      $scope.loadSites($scope.siteFilterOpts);
      Util.filter($scope, 'siteFilterOpts', $scope.loadSites);
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
