

angular.module('matsuo.baseApp')
    .factory('userGroupConfiguration', ['$route', '$rootScope', '$location', 'routeConfiguration',
      function($route, $rootScope, $location, routeConfiguration) {

        var userGroupConfiguration = {
          refreshAppUserConfiguration: function () {
            var groups = _.pluck($rootScope.user.groups, 'name');
            if (_.contains(groups, 'ADMIN')) {
              routeConfiguration.defaultRoute = '/base/registration'
            } else if (_.contains(groups, 'DOCTOR')) {
              routeConfiguration.defaultRoute = '/doctors/schedule_today'
            } else {
              routeConfiguration.defaultRoute = '/base/registration'
            }

            $route.routes['null'] = routeConfiguration.defaultRoute;
            $location.url(routeConfiguration.defaultRoute);
          }
        };
        return userGroupConfiguration;
      }])
    .factory('routeConfiguration', function() {
      return {
        rootPath: 'views',
        extension: '.jsp'
      }
    });
