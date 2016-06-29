angular.module('openspecimen')
  .directive('osEntityInfo', function() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        obj: "=",
        header: "@"
      },
      controller: function($scope) {
        $scope.obj.getAuditInfo().then(
          function(result) {
            $scope.auditDetail = result;
          }
        );
      },
      template: '<div class="os-entity-info">' +
                '  <h3 class="os-sub-section-title">' +
                '    <span ng-if="header">{{"entity_info." + header | translate}}</span>' +
                '    <span ng-if="!header" translate="entity_info.activity">Activity</span>' +
                '  </h3>' +
                '  <ul class="os-one-col os-key-values">' +
                '    <li class="item">' +
                '      <strong class="key key-sm">' +
                '        <span translate="entity_info.created_on"></span>' + 
                '      </strong>' +
                '      <span class="value value-md">' +
                '         {{auditDetail.createdOn | date: global.dateTimeFmt}}' +
                '      </span>' +
                '    </li>' + 
                '    <li class="item">' +
                '      <strong class="key key-sm">' +
                '        <span translate="entity_info.created_by"></span>' + 
                '      </strong>' +
                '      <span class="value value-md">' +
                '         {{auditDetail.createdBy.firstName}} {{auditDetail.createdBy.lastName}}' +
                '      </span>' +
                '    </li>' + 
                '    <li class="item">' +
                '      <strong class="key key-sm">' +
                '        <span translate="entity_info.updated_on"></span>' + 
                '      </strong>' +
                '      <span class="value value-md" ng-if="auditDetail.lastUpdatedOn">' +
                '         {{auditDetail.lastUpdatedOn | date: global.dateTimeFmt}}' +
                '      </span>' +
                '    </li>' + 
                '    <li class="item">' +
                '      <strong class="key key-sm">' +
                '        <span translate="entity_info.updated_by"></span>' + 
                '      </strong>' +
                '      <span class="value value-md" ng-if="auditDetail.lastUpdatedBy">' +
                '         {{auditDetail.lastUpdatedBy.firstName}} {{auditDetail.lastUpdatedBy.lastName}}' +
                '      </span>' +
                '    </li>' + 
                '  </ul>' +
                '</div>'
    }
  });
