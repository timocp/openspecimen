var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.DistributionProtocols = edu.common.de.LookupSvc.extend({
  getApiUrl: function() {
    return '/openspecimen/rest/ng/distribution-protocols/';
  },

  searchRequest: function(searchTerm) {
    return {name: searchTerm};
  },

  formatResults: function(distributionProtocols) {
    var result = [];
    for (var i = 0; i < distributionProtocols.length; ++i) {
      result.push({id: distributionProtocols[i].id, text: distributionProtocols[i].shortTitle});
    }

    return result;
  },

  formatResult: function(distributionProtocol) {
    return {id: distributionProtocol.id, text: distributionProtocol.shortTitle};
  },

  getDefaultValue: function() {
    var deferred = $.Deferred();
    deferred.resolve({id: '', shortTitle: ''});
    return deferred.promise();
  }
});

openspecimen.ui.fancy.DistributionProtocolField = edu.common.de.LookupField.extend({
  svc: new openspecimen.ui.fancy.DistributionProtocols()
});

edu.common.de.FieldManager.getInstance()
  .register({
    name: "distributionProtocolField", 
    displayName: "Distribution Protocol Dropdown",
    fieldCtor: openspecimen.ui.fancy.DistributionProtocolField
  }); 
