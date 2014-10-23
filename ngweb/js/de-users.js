
var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.Users = function() {
  var baseUrl = '/openspecimen/rest/ng/users/';

  var userCacheMap = {};
 
  var defaultList = [];

  var signedInUser;

  this.getUsers = function(queryTerm) {
    var deferred = $.Deferred();
    if (!queryTerm && defaultList.length > 0) {
      deferred.resolve(defaultList);
      return deferred.promise();
    }

    var xhr;
    if (queryTerm) {
      xhr = $.ajax({type: 'GET', url: baseUrl, data: {searchString: queryTerm, sortBy:'lastName,firstName'}});
    } else if (this.getAllUsersXhr) {
      xhr = this.getAllUsersXhr;
    } else {
      xhr = this.getAllUsersXhr = $.ajax({type: 'GET', url: baseUrl, data: {sortBy:'lastName,firstName'}});
    }
   
   
    xhr.done(
      function(data) {
        var result = [];
        var users = data.users;
        for (var i = 0; i < users.length; ++i) {
          result.push({id: users[i].id, text: users[i].lastName + ', ' + users[i].firstName});
        }

        if (!queryTerm) {
          defaultList = result;
        }

        deferred.resolve(result);
      }
    ).fail(
      function(data) {
        alert("Failed to load users list");
        deferred.resolve([]);
      }
    );

    return deferred.promise();
  };

  this.getUser = function(userId, callback) {
    var deferred = $.Deferred(); 
    var user = userCacheMap[userId];
    if (user) {
      deferred.resolve(user);
      return deferred.promise();
    }

    for (var i = 0; i < defaultList.length; ++i) {
      if (defaultList[i].id == userId) { 
        deferred.resolve(defaultList[i]);
        return deferred.promise();
      }
    }

    $.ajax({type: 'GET', url: baseUrl + userId})
      .done(function(data) {
        var result = {id: data.id, text: data.lastName + ', ' + data.firstName};
        userCacheMap[userId] = result;
        deferred.resolve(result);
      })
      .fail(function(data) {
        alert("Failed to retrieve user")
        deferred.resolve(undefined);
      });

    return deferred.promise();
  };

  this.getSignedInUser = function() {
    var deferred = $.Deferred(); 
    if (signedInUser) {
      deferred.resolve(signedInUser);
      return deferred.promise();
    }

    var that = this;
    $.ajax({type: 'GET', url: baseUrl + '/signed-in-user'})
      .done(function(data) {
        var result = {id: data.id, text: data.lastName + ', ' + data.firstName};
        userCacheMap[data.id] = result;
        signedInUser = result;
        deferred.resolve(result);
      })
      .fail(function(data) {
        alert("Failed to retrieve user");
        deferred.resolve(undefined);
      });

    return deferred.promise();
  };
};

var usersSvc = new openspecimen.ui.fancy.Users();

openspecimen.ui.fancy.UserField = edu.common.de.LookupField.extend({
  getDefaultValue: function() {
    return usersSvc.getSignedInUser();
  },

  lookup: function(userId) {
    return usersSvc.getUser(userId);
  },

  search: function(qTerm) {
    return usersSvc.getUsers(qTerm);
  }
});

edu.common.de.FieldManager.getInstance()
  .register({
    name: "userField", 
    displayName: "User Dropdown",
    fieldCtor: openspecimen.ui.fancy.UserField
  }); 
