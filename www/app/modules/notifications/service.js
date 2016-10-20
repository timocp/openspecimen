
angular.module('os.common.notifications.service', [])
  .service('NotificationSvc', function($q, $timeout) {

    var notifications = [
      {id: "1", msg: "You have been added as PI in Test CP", op: "CREATE", objName: "cp",
        objId: "1", status: "UNREAD", date: "26-Aug-2016 3:10 PM"},
      {id: "2", msg: "Specimen Whole Blood EDTA has been created", op: "CREATE", objName: "specimen",
        objId: "3", status: "READ", date: "02-Sep-2016 5:10 AM"},
      {id: "3", msg: "Participant PPID01 has been deleted", op: "DELETE", objName: "cpr",
        objId: "1", status: "UNREAD", date: "05-Sep-2016 6:40 PM"},
      {id: "4", msg: "Container -80F has been updated", op: "UPDATE", objName: "container",
        objId: "1", status: "UNREAD", date: "05-Sep-2016 6:40 PM"}
    ];

    var newNotifs = [
      {id: "5", msg: "New Notif 1", op: "CREATE", objName: "cp",
        objId: "1", status: "UNREAD", date: "26-Aug-2016 3:10 PM"},
      {id: "6", msg: "New Notif 2", op: "CREATE", objName: "specimen",
        objId: "3", status: "UNREAD", date: "02-Sep-2016 5:10 AM"},
      {id: "7", msg: "New Notif 3", op: "DELETE", objName: "cpr",
        objId: "1", status: "UNREAD", date: "05-Sep-2016 6:40 PM"}
    ];

    function register(callbackFn) {
      $timeout(function() {
        callbackFn(newNotifs);
      }, 10000);
    }

    function getNotifications(startAt, maxResult, callback) {
      return getPromise(notifications);
      // $http call to get all notification list.
    }

    function getUnreadNotifCount() {
      return getPromise(3);
      // $http call to get notification based on id.
    }

    function markAsRead(id) {
      return getPromise('success');
      // $http call to mark notification as read.
    }

    function markVisited() {
      return getPromise('success');
      // $http call to mark notifications as visited.
    }

    function getPromise(data) {
      var defer = $q.defer();
      defer.resolve(data);
      return defer.promise;
    }

    return {
      register: register,

      getNotifications: getNotifications,

      getUnreadNotificationsCount: getUnreadNotifCount,

      markAsRead: markAsRead,

      markVisited: markVisited
    };
  });