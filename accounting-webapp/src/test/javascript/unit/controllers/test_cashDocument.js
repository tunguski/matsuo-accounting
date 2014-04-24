describe('CashDocument controllers', function () {
  describe('CashDocumentController with WithdrawSlipController', function () {
    var documentController;

    beforeEach(inject(function ($httpBackend, $rootScope, $controller) {
      var routeParams = {
        idEntity: 1
      };

      var kw = {
        "id": 4,
        "createdTime": "2014-01-10T21:35:57Z",
        "fields": {
          "approvingPerson": "Maciej Gołębiowski",
          "authenticityText": "ORYGINAŁ",
          "number": "123551/125",
          "buyer.address": " Legionowo, Sielankowa",
          "seller.name": "Mediq sp. z o.o.",
          "cashRegisterAmount": "-16.57",
          "seller.address": " Legionowo, Piłsudskiego 20",
          "cashReportReference": "Nr 123, poz. 12",
          "creator": "Maciej Stępień",
          "totalAmountInWords": "szesnaście złotych pięćdziesiąt siedem groszy",
          "seller.id": "898",
          "totalAmount": "16.57",
          "buyer.name": "PHU REMONT",
          "buyer.id": "912",
          "sellPlace": "Warszawa",
          "sellDate": "2013-09-08T22:00:00Z"
        },
        "printClass": "pl.matsuo.clinic.model.print.cash.WithdrawSlip",
        "idEntity": 1,
        "idCashRegisterReport": 1,
        "elements": [
          {"id": 10, "fields": {"accountNumber": "12345", "price": "3.21", "serviceName": "Morfologia"}},
          {"id": 11, "fields": {"accountNumber": "12345", "price": "5.24", "serviceName": "APTT"}},
          {"id": 12, "fields": {"accountNumber": "12345", "price": "8.12", "serviceName": "OB"}}
        ]};

      http.expectGET('/api/cashRegisters/actualCashRegister').respond({});
      http.expectGET('/api/cashDocuments/1').respond(kw);
      http.expectGET('/api/payers/912').respond({});
      http.expectGET('/api/payers/898').respond({});


      controller = $controller(CashDocumentController, { $scope: scope, $routeParams: routeParams });
      documentController = $controller(WithdrawSlipController, {$scope: scope, $routeParams: routeParams });

      http.flush();
    }));


    it('summaries are correct', function () {
      expect(scope.positions['invoice'].elements.length).toBe(3);
      expect(scope.positions['invoice'].summaryBrutto).toBe(16);
      expect(scope.positions.corrective).not.toBeDefined();
    });
  });


  describe('CashDocumentController with InvoiceController', function () {
    var documentController;

    beforeEach(inject(function ($httpBackend, $rootScope, $controller) {
      var routeParams = {
        idEntity: 1
      };

      var invoice = {"id": 1, "createdTime": "2014-01-11T11:59:31Z",
        "fields": {
          "areCommentsVisible": "true",
          "paymentType": "TRANSFER",
          "seller.nip": "5361188849",
          "number": "LEG/FV/2013/123456",
          "authenticityText": "ORYGINAŁ",
          "buyer.address": " Legionowo, Sielankowa",
          "seller.name": "Mediq sp. z o.o.",
          "buyer.pesel": "0",
          "cashRegisterAmount": "13.55",
          "seller.address": " Legionowo, Piłsudskiego 20",
          "amountAlreadyPaid": "13.55",
          "seller.id": "898",
          "issuanceDate": "2013-09-08T22:00:00Z",
          "totalAmount": "30.1312",
          "buyer.name": "PHU REMONT",
          "amountDueInWords": "szesnaście złotych pięćdziesiąt osiem groszy",
          "bankAccountNumber": "26 1050 1445 1000 0022 7647 0461",
          "amountDue": "16.5812",
          "sellPlace": "Warszawa",
          "buyer.id": "912",
          "buyer.nip": "1230826891",
          "dueDate": "2013-09-18T22:00:00Z",
          "sellDate": "2013-09-08T22:00:00Z",
          "comments": "komentarz"
        },

        "printClass": "pl.matsuo.clinic.model.print.cash.Invoice",
        "idEntity": 1,
        "idCashRegisterReport": 1,
        "elements": [
          {"id": 1, "fields": {"price": "3.21", "taxRate": "22", "count": "2", "serviceName": "APTT"}},
          {"id": 2, "fields": {"price": "5.21", "taxRate": "7", "count": "4", "serviceName": "OB"}}
        ]};

      http.expectGET('/api/cashRegisters/actualCashRegister').respond({});
      http.expectGET('/api/cashDocuments/1').respond(invoice);
      http.expectGET('/api/payers/912').respond({});
      http.expectGET('/api/payers/898').respond({});


      controller = $controller(CashDocumentController, { $scope: scope, $routeParams: routeParams });
      documentController = $controller(InvoiceController, {$scope: scope, $routeParams: routeParams });

      http.flush();
    }));


    it('summaries are correct', function () {
      expect(scope.positions['invoice'].elements.length).toBe(2);
      expect(scope.positions['invoice'].summaryBrutto).toBe(30.1312);
      expect(scope.positions['corrective'].elements.length).toBe(0);
    });
  });
});
