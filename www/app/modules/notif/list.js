angular.module('os.common.notif')
  .controller('NotifListCtrl', function($scope, $state, NotifSvc, ObjectStateResolver, Util) {

    var pageNo = 0, pageSize = 3;

    function init() {
      $scope.notifCtx = {
        notifications: [],
        showMore: false
      };


      //
      // TODO: Notifs should not be loaded until user
      // clicks on bell icon
      //
      getUnreadNotifsCount();
      loadNotifs();

      //
      // Should be removed
      //
      NotifSvc.register($scope.pushNotifications);
    }

    function getUnreadNotifsCount() {
      NotifSvc.getUnreadCount().then(
        function(count) {
          $scope.notifCtx.unreadCount = count;
        }
      );
    }

    function loadNotifs() {
      var startAt = pageNo * pageSize;
      NotifSvc.getNotifications(startAt, pageSize + 1).then(
        function(notifications) {
          if (notifications.length > pageSize) {
            $scope.notifCtx.showMore = true;
            notifications.splice(pageSize, notifications.length);
          }

          Util.appendAll($scope.notifCtx.notifications, setUrl(notifications));
        }
      );

      pageNo++;
    }

    function setUrl(notifications) {
      angular.forEach(notifications,
        function(notif) {
          if (notif.op != 'DELETE') {
            notif.sref = ObjectStateResolver.getState(notif.objName, 'id', notif.objId);
          }
        }
      );

      return notifications;
    }

    function notifyUser() {
      $scope.notifCtx.newNotifs = true;
    }

    $scope.showMoreNotifs = loadNotifs;

    $scope.markAsRead = function(notif) {
      if (notif.status != 'UNREAD') {
        return;
      }

      NotifSvc.markAsRead(notif.id).then(
        function(resp) {
          notif.status = 'READ';
          $scope.notifCtx.unreadCount--;
        }
      );
    }

    $scope.markVisited = function() {
      $scope.notifCtx.newNotifs = false;
      NotifSvc.markVisited();
    }

    $scope.pushNotifications = function(notifications) {
      notifyUser();
      $scope.notifCtx.unreadCount += notifications.length;
      Util.unshiftAll($scope.notifCtx.notifications, setUrl(notifications));
    }

    init();
  });
