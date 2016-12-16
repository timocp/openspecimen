angular.module('os.biospecimen.participant.bulkregistration', ['os.biospecimen.models'])
  .controller('BulkRegistrationCtrl', function($scope, $state, events, cp, CollectionProtocolRegistration, Alerts) {

    function init() {
      $scope.ctx = {
        cpEvents: events,
        cpId: cp.id,
        selection: {
          events: []
        }
      };
    }

    $scope.toggleAllEventSelect = function() {
      $scope.ctx.selection.events = [];
      if ($scope.ctx.selection.all) {
        $scope.ctx.selection.events = [].concat($scope.ctx.cpEvents);
      }

      angular.forEach($scope.ctx.cpEvents,
        function(event) {
          event.selected = $scope.ctx.selection.all;
        }
      );
    }

    $scope.toggleEventSelect = function(event) {
      var events = $scope.ctx.selection.events;

      if (event.selected) {
        events.push(event);
      } else {
        var idx = events.indexOf(event);
        if (idx != -1) {
          events.splice(idx, 1);
        }
      }

      $scope.ctx.selection.all = (events.length == $scope.ctx.cpEvents.length);
    };

    $scope.registerParticipants = function() {
      var bulkRegDetail = {
        cpId: $scope.ctx.cpId,
        regCount: $scope.ctx.regCount,
        events: angular.copy($scope.ctx.selection.events)
      }

      angular.forEach(bulkRegDetail.events, function(event) {
        delete event.selected;
      });

      CollectionProtocolRegistration.bulkRegistration(bulkRegDetail).then(
        function(cprList) {
          $state.go('participant-list', {cpId: $scope.cp.id});
          Alerts.success('participant.participant_registered', {numOfParticipants: bulkRegDetail.regCount});
        }
      )
    }

    init();
  });

