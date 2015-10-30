angular.module('openspecimen')
  .directive('osGrid', function($state, $stateParams, Util) {

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
        scope.filterOpts = {};
        Util.filter(scope, 'filterOpts', loadObjects);

        if ($stateParams.filter || $stateParams.sortby) {
          scope.loadObjects({objId: undefined, includeStats: true, filterOpts: getFilterOpts()});
        }

        scope.sortBy = function(colName, element) {
          var orderExists = scope.sortOrder.indexOf(colName) > -1  || scope.sortOrder.indexOf("-" + colName) > -1 ? true  : false;
          if (!orderExists) {
            scope.sortOrder = [];
          }

          if (orderExists) {
            var order =  scope.sortOrder[0];
            if (colName.indexOf(order) > -1 || order.indexOf(colName) > -1) {
              colName = order.indexOf("-") == 0 ? colName : "-" + colName;
              scope.sortOrder[0] = colName;
            }
          } else {
            scope.sortOrder.push(colName);
          }

          loadObjects();
        }

        scope.$watch('objList', function(newVal, oldVal) {
          if (newVal == oldVal) {
            return;
          }

          if (scope.objList != undefined) {
            scope.list = scope.objList;
          }

          var sortElClass = $stateParams.sortby;
          if (!sortElClass) {
            return;
          }

          var asc = true;
          if (sortElClass.indexOf("-") > -1) {
            sortElClass = sortElClass.substr(1);
            var asc = false;
          }

          var sortEl = angular.element(document.getElementsByClassName(sortElClass));
          var inputEl = angular.element("<i class='fa'></input>")

          if (asc) {
            inputEl.addClass("fa-arrow-up");
          } else {
            inputEl.addClass("fa-arrow-down");
          }

          sortEl.append(inputEl);
        });

        scope.showDetail = function(obj) {
          scope.onClick({obj: obj});
        }

        scope.deleteObj = function(obj) {
          scope.onDelete({obj: obj});
        }

        scope.clearFilters = function() {
          scope.filterOpts = {};
        };

        function loadObjects() {
          var filterStr = JSON.stringify(scope.filterOpts);
          if ($stateParams.sortby && ! scope.sortOrder[0]) {
            scope.sortOrder[0] = $stateParams.sortby;
          }

          $state.go($state.current,  {sortby: scope.sortOrder, reload: false, filter : filterStr});
        }

        function getFilterOpts() {
          scope.filterOpts = JSON.parse($stateParams.filter);
          scope.sortOrder[0] = $stateParams.sortby
          var filterOpts = angular.copy(scope.filterOpts);
          filterOpts.sortBy = $stateParams.sortby;

          return filterOpts;
        }
      }
    };
  });
