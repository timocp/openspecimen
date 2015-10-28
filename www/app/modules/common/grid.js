angular.module('openspecimen')
  .directive('osGrid', function() {


    return {
      restrict: 'E',
      scope: {
        objList: '=objList',
        onClick: '&onClick',
        onDelete: '&onDelete',
        loadObjects: '&loadObjects'
      },
      replace: true,
      template : '<div ng-include="templateUrl"></div>',

      link: function(scope, element, attrs) {
        scope.sortOrder = [];
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

        scope.sortBy = function(colName, element) {
          scope.sort = colName;

          var sortEl = angular.element(element.target.parentElement.children[1]);
          var orderExists = scope.sortOrder.indexOf(colName) > -1  || scope.sortOrder.indexOf("-" + colName) > -1 ? true  : false;

          if (!orderExists) {
            scope.sortOrder = [];
          }

          if (orderExists) {
            angular.forEach(scope.sortOrder, function(order, $index) {
              if (colName.indexOf(order) > -1 || order.indexOf(colName) > -1) {
                colName = order.indexOf("-") == 0 ? colName : "-" + colName;
                scope.sortOrder[$index] = colName;
              }

            })
          } else {
            scope.sortOrder.push(colName);
          }

          if (colName.indexOf("-") > -1) {
            sortEl.removeClass("fa fa-arrow-down");
            sortEl.addClass("fa fa-arrow-up");
          } else {
            sortEl.removeClass("fa fa-arrow-up");
            sortEl.addClass("fa fa-arrow-down");
          }

          scope.loadObjects({objId: scope.parentId, includeStats: true, filterOpts: {sortBy: scope.sortOrder.reverse()}});
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
