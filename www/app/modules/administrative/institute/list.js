angular.module('os.administrative.institute.list', ['os.administrative.models'])
  .controller('InstituteListCtrl', function($scope, $state, $stateParams, Institute, Util, DeleteUtil) {

    function init() {
      $scope.instituteFilterOpts = {includeStats: true};

      if ($stateParams.reload != "false") {
        $scope.loadInstitutes($scope.instituteFilterOpts);
      }

    }

    $scope.loadInstitutes = function(filterOpts) {
      Institute.query(filterOpts).then(
        function(instituteList) {
          $scope.instituteList = instituteList;
        }
      )
    }

    $scope.showInstituteOverview = function(institute) {
      $state.go('institute-detail.overview', {instituteId: institute.id});
    };

    $scope.deleteInstitute = function(institute) {
      DeleteUtil.delete(institute, {onDeletion: $scope.loadInstitutes});
    }

    init();
  });
