toastr = {
  success: function (msg) {
    console.log(msg);
  },
  info: function (msg) {
    console.log(msg);
  }
}

angular.module('mt.test_module', ['mt.core', 'mt.cashRegisterService']);

beforeEach(module('mt.test_module'));


beforeEach(function () {
  this.addMatchers({
    toEqualData: function (expected) {
      return angular.equals(this.actual, expected);
    }
  });
});


var controller;
var rootScope, scope, http, compile;

beforeEach(inject(function ($httpBackend, $rootScope, $compile) {
  http = $httpBackend;
  rootScope = $rootScope;
  scope = $rootScope.$new();
  compile = $compile;
}));

