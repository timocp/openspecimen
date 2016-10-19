
angular.module('openspecimen')
  .factory('ObjectStateResolver', function() {
    var objStateMap = {
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

    return {
      getUrl: function getUrl(objName, value, key) {
        key = key ? key : "id";
        return "#/object-state-params-resolver?stateName=" + objStateMap[objName] + "&objectName=" + objName
          + "&key=" + key + "&value=" + value;
      }
    }
  });