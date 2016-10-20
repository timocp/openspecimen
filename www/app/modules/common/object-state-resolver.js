
angular.module('openspecimen')
  .factory('ObjectStateResolver', function() {
    var objStateMap = {};

    function regObjState(objName, state) {
      objStateMap[objName] = state;
    }

    function getUrl(objName, key, value) {
      return "#/object-state-params-resolver?stateName=" + objStateMap[objName] + "&objectName=" + objName
        + "&key=" + key + "&value=" + value;
    }

    return {
      regObjState: regObjState,

      getUrl: getUrl
    }
  });