
restFactory("CashRegisterReport", {
  additionalFunctions: {
    reportForCashRegister: {
      url: "/reportForCashRegister/:idCashRegister"
    }
  }
});

function CashRegisterReportsController($scope, $http, $routeParams, $timeout, CashRegister, CashRegisterReport) {

  $scope.setTitle("Raporty kasowe");

  $scope.cashRegisterReports = CashRegisterReport.query({}, function (data) {
    $scope.filterCashReports();
  });

  $scope.cashRegisters = CashRegister.query({}, function() {
    if ($routeParams.idCashRegister) {
      $scope.cashRegister.value = _.find($scope.cashRegisters, function(cashRegister) {
        return cashRegister.id === parseInt($routeParams.idCashRegister);
      });
    }
  });

  $scope.filterCashReports = function (n) {
    if (n) {
      $scope.filteredReports = _.filter($scope.cashRegisterReports, function (report) {
        return report.cashRegister.id === n.id;
      });
    } else {
      $scope.filteredReports = $scope.cashRegisterReports;
    }
  };

  $scope.$watch('cashRegister.value', $scope.filterCashReports);


  initializeSelect2($scope, "cashRegister", null, "cashRegister", {
    allowClear: true,
    definedElements: $scope.cashRegisters
  });
}

