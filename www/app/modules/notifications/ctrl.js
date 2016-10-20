
angular.module('os.common.notifications.ctrl', [])
  .controller('NotificationListCtrl', function($scope, $state, NotificationSvc, ObjectStateResolver, Util) {

    var page = 0, maxResults = 3;

    function init() {
      $scope.notifCtx = {
        notifications: [],
        showMore: false
      };

      getUnreadNotificationsCount();
      loadNotifications();
      NotificationSvc.register($scope.pushNotifications);           // For testing purpose
    }

    function getUnreadNotificationsCount() {
      NotificationSvc.getUnreadNotificationsCount().then(
        function(count) {
          $scope.notifCtx.count = count;
        }
      );
    }

    function loadNotifications() {
      var startAt = maxResults * page;
      NotificationSvc.getNotifications(startAt, maxResults + 1).then(
        function(notifications) {
          setNotificationList(notifications);
        }
      );

      page++;
    }

    function setNotificationList(notifications) {
      if (notifications.length > maxResults) {
        $scope.notifCtx.showMore = true;
        notifications.splice(maxResults, notifications.length);
      }

      Util.appendAll($scope.notifCtx.notifications, setUrl(notifications));
    }

    function setUrl(notifications) {
      angular.forEach(notifications, function(notif) {
        if (notif.op != 'DELETE')
          notif.url = ObjectStateResolver.getUrl(notif.objName, "id", notif.objId);
      });

      return notifications;
    }

    function notifyUser() {
      $scope.notifCtx.notify = true;
    }

    $scope.loadMoreNotifications = loadNotifications;

    $scope.markAsRead = function(index) {
      var notification = $scope.notifCtx.notifications[index];
      if (notification.status == 'UNREAD') {
        NotificationSvc.markAsRead(notification.id).then(
          function(resp) {
            notification.status = 'READ';
            $scope.notifCtx.count--;
          }
        );
      }
    }

    $scope.markVisited = function() {
      $scope.notifCtx.notify = false;
      NotificationSvc.markVisited();
    }

    $scope.pushNotifications = function(notifications) {
      notifyUser();
      $scope.notifCtx.count += notifications.length;
      Util.unshiftAll($scope.notifCtx.notifications, setUrl(notifications));
    }

    init();
  });