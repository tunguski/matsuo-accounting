
angular.module('mt.cashRegisterService', [])
    /**
     * Zarządzanie wybraną kasą, w poczet której rejestrowane są druki.
     */
    .factory('cashRegisterService', function($q, $http, $rootScope) {
      var cashRegisterService = {
        getCashRegister: function() {
          var deferred = $q.defer();
          $http.get('/api/cashRegisters/actualCashRegister').success(function (cashRegister) {
            cashRegisterService.cashRegister = cashRegister;
            if (cashRegister) {
              deferred.resolve(cashRegister);
            } else {
              deferred.reject();
            }
          });
          return deferred.promise;
        }
      };

      cashRegisterService.getCashRegister();

      $rootScope.$on('loggedIn', cashRegisterService.getCashRegister);

      return cashRegisterService;
    })
;

