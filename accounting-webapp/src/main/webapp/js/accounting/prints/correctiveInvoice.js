restFactory("CorrectiveInvoice");


function CorrectiveInvoiceController($scope, $routeParams, $http, CorrectiveInvoice) {
  abstractInvoiceController($scope, $routeParams, $http);


  $scope.addInvoicePosition = function() {
    $scope.entity.elements.push({ fields: {
      jm: "szt.",
      count: 1,
      price: 0,
      taxRate: 0,
      isAfterCorrection: true
    }});
    $scope.recalculateSummaries();
  };


  function createEmptyEntity(scope) {
    scope.entity = new CorrectiveInvoice();

    return $http.get('/api/correctiveInvoices/for_invoice/' + $routeParams.idCorrectedPrint).then(function(data) {
      $.extend(true, scope.entity, data.data);
      scope.recalculateSummaries();
      scope.isInvoice = $scope.entity.fields.isReceipt !== 'true';

      scope.loadBuyer(scope.entity.fields['buyer.id']);
    });

    $scope._loadData.resolve();
  }


  $scope.correctionValue = function() {
    return $scope.positions['corrective'].summaryBrutto - $scope.positions['invoice'].summaryBrutto;
  }


  $scope.pluginPrintLogic(function (scope) {
    scope.recalculateSummaries = $scope.recalculateSummaries;
    scope.createEmptyEntity = createEmptyEntity;
  });
}

