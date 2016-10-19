
angular.module('os.common.notifications',
  [
    'ui.router',
    'os.common.notifications.service',
    'os.common.notifications.ctrl'
  ])
  .config(function($stateProvider) {
    $stateProvider
      .state('notification-state-resolver', {
        url: '/notification?notificationId',
        controller: function($location, redirectUrl) {
          if(redirectUrl) {
            $location.url(redirectUrl.substr(1, redirectUrl.length));
          }
        },
        resolve: {
          redirectUrl: function($stateParams, ObjectStateResolver, NotificationSvc) {
            if ($stateParams.notificationId) {
              var notification = NotificationSvc.getById($stateParams.notificationId);
              return ObjectStateResolver.getUrl(notification.objName ,notification.objId);
            }
          }
        },
        parent: 'signed-in'
      });
  });