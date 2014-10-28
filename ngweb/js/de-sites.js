var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.Sites = edu.common.de.LookupSvc.extend({
  getApiUrl: function() {
    return '/openspecimen/rest/ng/sites/';
  },

  searchRequest: function(searchTerm) {
    return {name: searchTerm};
  },

  formatResults: function(sites) {
    var result = [];
    for (var i = 0; i < sites.length; ++i) {
      result.push({id: sites[i].id, text: sites[i].name});
    }

    return result;
  },

  formatResult: function(site) {
    return {id: site.id, text: site.name};
  },

  getDefaultValue: function() {
    var deferred = $.Deferred();
    deferred.resolve({id: '',name: ''});
    return deferred.promise();
  }
});

openspecimen.ui.fancy.SiteField = edu.common.de.LookupField.extend({
  svc: new openspecimen.ui.fancy.Sites()
});

edu.common.de.FieldManager.getInstance()
  .register({
    name: "siteField",
    displayName: "Site Dropdown",
    fieldCtor: openspecimen.ui.fancy.SiteField
  });
