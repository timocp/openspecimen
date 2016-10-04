angular.module('os.common.audit')
  .factory('Audit', function(osModel, $http) {
    var Audit = osModel('audit');

    Audit.getRevisions = function(entityName, entityId, opts) {
      var params = {
        entityName: entityName, entityId: entityId,
        maxResults: (opts && opts.maxRevs) || 100
      }

      return $http.get(Audit.url() + 'revisions', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    Audit.exportRevisions = function(entityName, entityId, opts) {
      var op = {
        entityName: entityName, entityId: entityId,
        from: opts.from, to: opts.to
      }

      return $http.post(Audit.url() + 'export-revisions', op).then(
        function(resp) {
          return resp.data.status;
        }
      );
    }

    return Audit;
  });
