var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.Users = edu.common.de.LookupSvc.extend({
  getApiUrl: function() {
    return '/openspecimen/rest/ng/users/';
  },

  searchRequest: function(searchTerm) {
    return {searchString: searchTerm, sortBy: 'lastName,firstName'};
  },

  formatResults: function(data) {
    var result = [];
    var users = data.users;
    for (var i = 0; i < users.length; ++i) {
      result.push({id: users[i].id, text: users[i].lastName + ', ' + users[i].firstName});
    }

    return result;
  },

  formatResult: function(data) {
    return {id: data.id, text: data.lastName + ', ' + data.firstName};
  },

  getDefaultValue: function() {
    return $.ajax({type: 'GET', url: '/openspecimen/rest/ng/users/signed-in-user'});
  }
});

openspecimen.ui.fancy.UserField = edu.common.de.LookupField.extend({
  svc: new openspecimen.ui.fancy.Users()
});

edu.common.de.FieldManager.getInstance()
  .register({
    name: "userField", 
    displayName: "User Dropdown",
    fieldCtor: openspecimen.ui.fancy.UserField
  }); 
