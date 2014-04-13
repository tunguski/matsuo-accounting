
function CashRegisterReportController($scope, $routeParams, $location, printTypeService, CashRegisterReport, User) {


  $scope.printTypeService = printTypeService;


  $scope.sumCashDocuments = function () {
    var sum = 0.0;

    if ($scope.entity) {
      angular.forEach($scope.entity.prints, function (print) {
        sum = sum + parseFloat(print.fields.cashRegisterAmount);
      });
    }

    return sum;
  }


  $scope.save = function () {
    $scope.entity.cashRegister.reckoningParty = null;
    $scope.entity.$save(function (entity, headers) {
      $location.replace();
      $location.url('/cash/cashRegisterReport/' + lastUrlElement(headers));
    });
  }


  function configureReport(report) {
    angular.forEach(report.prints, function (print) {
      print.printSimpleClassName = print.printClass.split('.').pop();
    });
    loadAndInject(report.prints, User, "userCreated", "id");
  }


  if ($routeParams.idEntity) {
    $scope.setTitle("<span><span class='comment'>Raport kasowy kasy </span>{{entity.cashRegister.code}} " +
            "<span class='comment'>z dnia</span> {{entity.createdTime | formatDate}}</span>", $scope);

    $scope.entity = CashRegisterReport.get({ idCashRegisterReport : $routeParams.idEntity }, configureReport);

  } else if ($routeParams.idCashRegister) {
    $scope.setTitle("<span><span class='comment'>Nowy raport kasowy dla: </span>{{entity.cashRegister.code}}</span>", $scope);

    $scope.entity = CashRegisterReport.reportForCashRegister({ idCashRegister : $routeParams.idCashRegister }, configureReport);
  }
}

