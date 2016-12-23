
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel, $http) {
    var DistributionProtocol =
      osModel(
        'distribution-protocols',
        function(dp) {
          dp.consentModel = osModel('distribution-protocols/' + dp.$id() + '/consent-tiers');

          dp.consentModel.prototype.$id = function() {
            return this.consentStmtId;
          }

          dp.consentModel.prototype.getDisplayName = function() {
            return this.consentStmtCode;
          }

          dp.consentModel.prototype.getType = function() {
            return 'consent';
          }
        }
      );

    DistributionProtocol.prototype.getType = function() {
      return 'distribution_protocol';
    }

    DistributionProtocol.prototype.getDisplayName = function() {
      return this.title;
    }

    DistributionProtocol.prototype.getConsentTiers = function() {
      return this.consentModel.query();
    };

    DistributionProtocol.prototype.newConsentTier = function(consentTier) {
      return new this.consentModel(consentTier);
    };

    DistributionProtocol.prototype.close = function() {
      return updateActivityStatus(this, 'Closed');
    }
    
    DistributionProtocol.prototype.reopen = function() {
      return updateActivityStatus(this, 'Active');
    }
    
    function updateActivityStatus(dp, status) {
      return $http.put(DistributionProtocol.url() + '/' + dp.$id() + '/activity-status', {activityStatus: status}).then(
        function(result) {
          return new DistributionProtocol(result.data);
        }
      );
    }
    
    DistributionProtocol.getOrders = function(params) {
      return $http.get(DistributionProtocol.url() + 'orders', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    DistributionProtocol.prototype.historyExportUrl = function() {
      var params = '?dpId=' + this.$id() + '&groupBy=specimenType,anatomicSite,pathologyStatus';
      return DistributionProtocol.url() + '/orders-report' + params;
    }
    
    return DistributionProtocol;
  });

