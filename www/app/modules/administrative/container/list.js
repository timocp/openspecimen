angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container, Util, DeleteUtil) {

    function init() {
      $scope.containerFilterOpts = {};
      loadContainers();
      Util.filter($scope, 'containerFilterOpts', loadContainers);
    }

    function loadContainers(filterOpts) {
      Container.list(filterOpts).then(
        function(containers) {
          $scope.containerList = containers;
        }
      );
    }

    $scope.showContainerOverview = function(container) {
      $state.go('container-detail.overview', {containerId: container.id});
    };

    $scope.deleteContainer = function(container) {
      DeleteUtil.delete(container, {
        onDeletion: loadContainers,
        confirmDelete: 'container.confirm_delete'
      });
    }

    init();
  });