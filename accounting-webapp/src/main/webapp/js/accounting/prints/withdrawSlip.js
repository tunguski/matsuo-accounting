restFactory("WithdrawSlip");


function WithdrawSlipController($scope, WithdrawSlip, CashRegisterReport, $routeParams) {


  function createEmptyEntity(scope) {
    scope.entity = new WithdrawSlip({
      fields: {
        issuanceDate: moment().toISOString(),
        sellDate: moment().toISOString(),
        dueDate: moment().toISOString()
      },
      elements: []
    });

    if($routeParams.idCashReport) {
      CashRegisterReport.get({idCashRegisterReport: $routeParams.idCashReport}, function(report) {
        $scope.addWithdrawSlipPosition({
          fields : {
            serviceName : 'Przeniesiono do sejfu',
            price : report.endingBalance,
            accountNumber : 0
          }
        });
      });
    }

    $scope._loadData.resolve();
  }


  $scope.addWithdrawSlipPosition = function(position) {
    $scope.entity.elements.push(position ? position : {
      fields : {
        serviceName : '',
        price : 0,
        accountNumber: ''
      }
    });
    $scope.recalculateSummaries();
  };


  function recalculateSummaries() {
    var summary = 0;
    angular.forEach($scope.entity.elements, function(value) {
      summary = summary + parseInt(value.fields.price);
    });

    $scope.positions = {
      invoice: {
        elements: $scope.entity.elements,
        summaryBrutto: summary
      }
    };
  };


  $scope.pluginPrintLogic(function (scope) {
    scope.recalculateSummaries = recalculateSummaries;
    scope.createEmptyEntity = createEmptyEntity;
  });
}

