
angular.module('os.biospecimen.participant.list', ['os.biospecimen.models'])
  .controller('ParticipantListCtrl', function(
    $scope, $state, $stateParams, $modal, $q, osRightDrawerSvc,
    cp, CollectionProtocolRegistration, Util, DeleteUtil) {

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.cp = cp;
      $scope.filterOpts = {};

      $scope.participantResource = {
        registerOpts: {resource: 'ParticipantPhi', operations: ['Create'], cp: $scope.cp.shortTitle},
      }

      if ($stateParams.reload != "false") {
        loadParticipants($scope.filterOpts, $scope.cpId, true);
      }
    }

    function loadParticipants() {
      $scope.loadParticipants($scope.filterOpts, $scope.cpId, true);
    }

    $scope.loadParticipants = function (filterOpts, cpId, includeStats) {
      cpId = cpId || $scope.cpId;

      CollectionProtocolRegistration.listForCp(cpId, includeStats, filterOpts).then(
        function(cprList) {
          if (!$scope.cprList && cprList.length > 50) {
            //
            // Show search options when number of participants are more than 12
            //
            osRightDrawerSvc.open();
          }

          $scope.cprList = cprList;
        }
      )
    }

    $scope.showParticipantOverview = function(cpr) {
      $state.go('participant-detail.overview', {cprId: cpr.cprId});
    };

    $scope.deleteReg = function(cpr) {
      cpr.id = cpr.cprId;
      DeleteUtil.delete(cpr, {onDeletion: loadParticipants});
    }

    init();
  });
