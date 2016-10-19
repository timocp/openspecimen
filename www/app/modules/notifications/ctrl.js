
angular.module('os.common.notifications.ctrl', [])
  .controller('NotificationListCtrl', function($scope, $state, NotificationSvc) {

    function init() {
      $scope.notifCtx = {};
      loadNotifications();
      $scope.notifCtx.counter = getUnreadNotificationsCount();
    }

    function loadNotifications() {
      $scope.notifCtx.notificationList = NotificationSvc.getNotifications();
    }

    function getUnreadNotificationsCount() {
      var count = 0;
      angular.forEach($scope.notifCtx.notificationList, function(notification) {
        if (notification.status == 'UNREAD')
          count ++;
        });

      return count;
    }

    $scope.markAsRead = function(index) {
      var notification = $scope.notifCtx.notificationList[index];
      if (notification.status == 'UNREAD') {
        notification.status = 'READ';
        NotificationSvc.markAsRead(notification.id);
      }

      if (notification.operation == 'DELETE')
        return;

      $state.go('notification-state-resolver', {notificationId: notification.id});
    }

    $scope.markVisited = function() {
      $scope.notifCtx.visited = true;
      NotificationSvc.markVisited();
    }

    $scope.loadMoreNotifications = function() {
      $scope.notifCtx.notificationList.concat(NotificationSvc.getNotifications());
    }

    init();
  });