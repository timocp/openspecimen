
angular.module('os.common.notifications.service', [])
  .service('NotificationSvc', function() {

    var notifications = [
      {id: "1", msg: "You have been added as PI in Test CP", operation: "CREATE", objName: "cp",
        objId: "1", status: "UNREAD", date: "26-Aug-2016 3:10 PM"},
      {id: "2", msg: "Specimen Whole Blood EDTA has been created", operation: "CREATE", objName: "specimen",
        objId: "3", status: "READ", date: "02-Sep-2016 5:10 AM"},
      {id: "3", msg: "Participant PPID01 has been deleted", operation: "DELETE", objName: "cpr",
        objId: "1", status: "UNREAD", date: "05-Sep-2016 6:40 PM"},
      {id: "4", msg: "Container -80F has been updated, Container -80F has been updated", operation: "UPDATE", objName: "container",
        objId: "1", status: "UNREAD", date: "05-Sep-2016 6:40 PM"}
    ];

    function getNotifications(startAt, maxResult) {
      return notifications;
      // $http call to get all notification list.
    }

    function getById(id) {
      return notifications[id-1];
      // $http call to get notification based on id.
    }

    function markAsRead(id) {
      // $http call to mark notification as read.
    }

    function markVisited() {
      // $http call to mark notifications as visited.
    }

    return {
      getNotifications: getNotifications,

      getById: getById,

      markAsRead: markAsRead,

      markVisited: markVisited
    };
  });