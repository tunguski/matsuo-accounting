toastr = {
  success: function (msg) {
    console.log(msg);
  },
  info: function (msg) {
    console.log(msg);
  }
}


function createService(code) {
  return {
    service: {
      name: 'name ' + code,
      medicalSpecialization: 'specjalizacja ' + code,
      code: code
    }
  }
}


var testData = {
  services: ['AA', 'RTG', 'TK', 'MR', 'USG', 'KON']
};


function configureBaseRequests(http, responses) {
  responses = responses || {};
  angular.forEach(testData.services, function (element) {
    http.expectGET('/api/appointments/treatmentsList/' + element).respond(responses[element] || []);
  });
  http.expectGET('/api/services/icd9').respond([]);
  http.expectGET('/api/services/icd10').respond([]);
}


beforeEach(module('klinika'));


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

