
angular.module('os.common.notifications.ctrl', [])
  .controller('notificationCtrl', function($scope, $window, NotificationSvc) {

    var objStateMap = {};

    function init() {
      $scope.notifications = [];
      objStateMap = NotificationSvc.getObjStateMap();
      getNotifications();
      $scope.notificationCount = getUnreadNotificationsCount();
    }

    function getNotifications() {
      $scope.notifications = setUrl(NotificationSvc.getNotifications());
    }

    function setUrl(notifications) {
      angular.forEach(notifications, function(notification) {
        var state = objStateMap[notification.objName];
        var url = "#/object-state-params-resolver?stateName=" + state + "&objectName=" + notification.objName +
          "&key=id&value=" + notification.objId;
        angular.extend(notification, {url: url});
      });

      return notifications;
    }

    function getUnreadNotificationsCount() {
      var count = 0;
      angular.forEach($scope.notifications, function(notification) {
        if (notification.status == 'Unread')
          count ++;
        });

      return count;
    }

    $scope.markAsRead = function(index) {
      var notification = $scope.notifications[index];
      if (notification.status == 'Unread') {
        notification.status = 'Read';
        // NotificationSvc.markAsRead(notification.id);
      }

      if (notification.operation == 'Delete')
        return;

      $window.location.href = notification.url;
    }

    $scope.markVisited = function() {
      $scope.visited = true;
      // DB call to mark notifications visited...
    }

    init();
  });