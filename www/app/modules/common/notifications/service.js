
angular.module('os.common.notifications.service', [])
  .service('NotificationSvc', function() {

    var notifications = [
      {id: "1", msg: "You have been added as PI in Test CP", operation: "Create", objName: "cp",
        objId: "1", status: "Unread", date: "26-Aug-2016 3:10 PM"},
      {id: "2", msg: "Specimen Whole Blood EDTA has been created", operation: "Create", objName: "specimen",
        objId: "3", status: "Read", date: "02-Sep-2016 5:10 AM"},
      {id: "3", msg: "Participant PPID01 has been deleted", operation: "Delete", objName: "cpr",
        objId: "1", status: "Unread", date: "05-Sep-2016 6:40 PM"},
      {id: "4", msg: "Container -80F has been updated, Container -80F has been updated", operation: "Update", objName: "container",
        objId: "1", status: "Unread", date: "05-Sep-2016 6:40 PM"}
    ];

    function getNotifications() {
      return notifications;
    }

    function getObjStateMap() {
      return {
        "cp": "cp-detail.overview",
        "cpr": "participant-detail.overview",
        "visit": "visit-detail.overview",
        "specimen": "specimen-detail.overview",
        "container": "container-detail.overview",
        "site": "site-detail.overview",
        "order": "order-detail.overview",
        "distributionProtocol": "dp-detail.overview",
        "shipment": "shipment-detail.overview"
      };
    }

    function markAsRead() {

    }

    return {
      getNotifications: getNotifications,

      getObjStateMap: getObjStateMap,

      markAsRead: markAsRead
    };
  });