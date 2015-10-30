
angular.module('os.biospecimen.cp.list', ['os.biospecimen.models'])
  .controller('CpListCtrl', function($scope, $state, $stateParams, CollectionProtocol, Util, PvManager, DeleteUtil) {
    function init() {
      if ($stateParams.reload != "false") {
        $scope.loadCollectionProtocols();
      }

      $scope.sites = PvManager.getSites();
    }

    $scope.loadCollectionProtocols = function(filterOpts) {
      CollectionProtocol.list(filterOpts).then(
        function(cpList) {
          $scope.cpList = cpList;
        }
      );
    };

    function filter(filterOpts) {
      var cpFilterOpts = angular.copy(filterOpts);
      if (cpFilterOpts.pi) {
        cpFilterOpts.piId = cpFilterOpts.pi.id;
        delete cpFilterOpts.pi;
      }

      $scope.loadCollectionProtocols(cpFilterOpts);
    }

    $scope.showParticipants = function(cp) {
      $state.go('participant-list', {cpId: cp.id});
    };

    $scope.deleteCp = function(cp) {
      DeleteUtil.delete(cp, {onDeletion: $scope.loadCollectionProtocols});
    }

    init();
  });
