angular.module('os.common.notif')
  .controller('NotifListCtrl', function($scope, $state, $interval, ObjectStateResolver, Util, Notification) {

    var pageNo = 0, pageSize = 10;
    var reload = true;

    function init() {
      $scope.$state = $state;

      $scope.notifCtx = {
        notifications: [],
        showMore: false
      };

      checkForNewNotifs();

      // Check for new notifications after every 2 minutes.
      $interval(checkForNewNotifs, 120000);
    }

    function checkForNewNotifs() {
      Notification.checkForNewNotifs().then(
        function(newNotifCount) {
          if (newNotifCount == 0) {
            return;
          }

          reload = true;
          $scope.notifCtx.newNotifCount = newNotifCount;
        }
      );
    }

    function loadNotifs() {
      if (reload) {
        pageNo = 0;
        loadMoreNotifs();
        getUnreadNotifsCount();
        markVisited();
      }
    }

    function getUnreadNotifsCount() {
      Notification.getUnreadCount().then(
        function(count) {
          $scope.notifCtx.unreadCount = count;
        }
      );
    }

    function loadMoreNotifs() {
      var startAt = pageNo * pageSize;
      var opts = {startAt: startAt, maxResults: pageSize + 1};
      Notification.list(opts).then(
        function(notifications) {
          if (notifications.length > pageSize) {
            $scope.notifCtx.showMore = true;
            notifications.splice(pageSize, notifications.length);
          }

          setNotificationList(notifications);
          reload = false;
        }
      );

      pageNo++;
    }

    function setNotificationList(notifications) {
      if (reload) {
        $scope.notifCtx.notifications = notifications;
      } else {
        Util.appendAll($scope.notifCtx.notifications, notifications);
      }
    }

    function markVisited() {
      $scope.notifCtx.newNotifCount = 0;
      Notification.markVisited();
    }

    $scope.loadNotifs = loadNotifs;

    $scope.showMoreNotifs = loadMoreNotifs;

    $scope.markAsRead = function(notif) {
      if (notif.status != 'UNREAD') {
        return;
      }

      notif.markAsRead(notif.id).then(
        function(resp) {
          notif.status = 'READ';
          $scope.notifCtx.unreadCount--;
        }
      );
    }

    init();
  });
