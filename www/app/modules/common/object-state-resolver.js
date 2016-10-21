
angular.module('openspecimen')
  .factory('ObjectStateResolver', function() {
    var objViewStateMap = {};

    function regState(objName, state) {
      objViewStateMap[objName] = state;
    }

    function getState(objName, key, value) {
      return 'object-state-params-resolver({' +
        'stateName:' + objViewStateMap[objName] +
        ',objectName:' + objName +
        ',key:' + key +
        ',value:' + value +
      '})';
    }

    return {
      regState: regState,

      getState: getState
    }
  });
