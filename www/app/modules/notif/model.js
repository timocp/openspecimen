
angular.module('os.notif.model', ['os.common.models'])
  .factory('Notification', function(osModel, $http, $q, ObjectStateResolver) {
    var Notification = osModel('notifications');

    /*
     *  TODO: To be removed
     */

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

    function getNotifications(startAt, maxResult, callback) {
      return getPromise(notifications);
    }

    function getUnreadCount() {
      return getPromise(3);
    }

    function markAsRead(id) {
      return getPromise('success');
    }

    function markVisited() {
      return getPromise('success');
    }

    function checkForNewNotifs() {
      return getPromise(3);
    }

    function getPromise(data) {
      var defer = $q.defer();
      defer.resolve({data: data});
      return defer.promise;
    }

    /* TODO: Remove upto here... */

    Notification.modelArrayRespTransform = function(response) {
      var collection = response.data;

      return collection.map(function(notif){
        if (notif.op != 'DELETE')
          notif.sref = ObjectStateResolver.getState(notif.objName, 'id', notif.objId);

        return new Notification(notif);
      });
    }

    Notification.list = function(opts) {
      //return Notification.query(opts).then(Notification.modelArrayRespTransform);
      return getNotifications().then(Notification.modelArrayRespTransform);
    }

    Notification.getUnreadCount = function() {
      //return $http.get(Notification.url() + '/count').then(Notification.noTransform);
      return getUnreadCount().then(Notification.noTransform);
    }

    Notification.markVisited = function() {
      //return $http.put(Notification.url() + '/mark-visited')
    }

    Notification.checkForNewNotifs = function() {
      //return $http.get(Notification.url() + '/notifs-update').then(Notification.noTransform);
      return checkForNewNotifs().then(Notification.noTransform);
    }

    Notification.prototype.markAsRead = function(id) {
      //return $http.put(Notification.url() + '/' + id + '/mark-read').then(Notification.noTransform);
      return markAsRead().then(Notification.noTransform);
    }

    return Notification;
  });