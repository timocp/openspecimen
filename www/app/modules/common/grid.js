angular.module('openspecimen')
  .directive('osGrid', function() {


    return {
      restrict: 'E',
      scope: {
        objList: '=objList',
        onClick: '&onClick',
        onDelete: '&onDelete',
      },
      replace: true,
      template : '<div ng-include="templateUrl"></div>',

      link: function(scope, element, attrs) {
        scope.templateUrl = attrs.templateUrl;

        scope.showDetail = function(obj) {
          scope.onClick({obj: obj});
        }

        scope.clickOnMore = function(element) {
          if (element != undefined) {
            element.stopPropagation();
          }
        }

        scope.deleteObj = function(obj) {
          scope.onDelete({obj: obj});
        }


        scope.$watch('objList', function(newVal, oldVal) {
          if (newVal == oldVal) {
            return;
          }

          if (scope.objList != undefined) {
            scope.list = scope.objList;
          }
        });
      }
    };
  });
