restFactory("CashDocument");
restFactory("Payer");


angular.module('mt.core')
    .factory('printTypeService', function() {
      var service = {
        /**
         * Returns string defining print type. Cash document view shows proper print basing on it.
         */
        printType: function (print) {
          if (print && print.printClass) {
            return _.uncapitalize(print.printClass.split('.').slice(-1)[0]);
          }
        },
        /**
         * Returns special version of print type. It adds '_receipt' suffix to type if print is receipt.
         */
        printTypeSpecial: function (print) {
          return service.printType(print)
              + ((print.fields.isReceipt  === 'true' || print.fields.isReceipt  === true) ? "_receipt" : "");
        },
        /**
         * Returns url to view showing passed kind of print.
         */
        printPageUrl: function (print) {
          if (print.fields['buyer.id']) {
            // cashDocument
            return '/#/prints/cashDocument/' + print.id;
          } else {
            // for all others view has to be /print/<print_type>/<id>
            return '/#/prints/' + service.printType(print) + '/' + print.id;
          }
        }
      };
      return service;
    });


function CashDocumentController($scope, $routeParams, $location, $filter, $q, cashRegisterService, printTypeService,
                                CashDocument, Payer, CashRegisterReport) {
  $scope.cashRegisterService = cashRegisterService;
  $scope.printTypeService = printTypeService;

  function sellerFromCashRegister(cashRegister) {
    if ($scope.entity && cashRegister && !$scope.entity.id) {
      $scope.idSeller.value = cashRegister.reckoningParty;
      $scope.entity.fields.sellPlace = cashRegister.reckoningParty.address.town;
    }
  }

  if (!$routeParams.idCashReport) {
    $scope.$watch('cashRegisterService.cashRegister', sellerFromCashRegister);
  }

  $scope.loadPayer = function(field) {
    return function (idEntity) {
      Payer.get({ idPayer: idEntity }, scopeSetter($scope, field + '.value'));
    }
  }

  $scope.loadSeller = $scope.loadPayer('idSeller');
  $scope.loadBuyer = $scope.loadPayer('idBuyer');


  $scope.recalculateSummaries = angular.noop;


  $scope.guestPrintClass = function() {
    return 'pl.matsuo.clinic.model.print.cash.' + ($routeParams.idCorrectedPrint
        ? "Corrective" + _.capitalize(printTypeService.printType($scope.correctedEntity)) : _.capitalize($routeParams.type));
  }


  $scope.pluginPrintLogic = function (pluginFunction) {
    pluginFunction($scope);

    function updateCashRegister() {
      $scope.entity.idCashRegisterReport = $routeParams.idCashReport;

      // dodawanie tworzonego dokumentu do z góry ustalonego raportu kasowego
      if ($routeParams.idCashReport) {
        CashRegisterReport.get({ idCashRegisterReport: $routeParams.idCashReport }, function (cashReport) {
          sellerFromCashRegister(cashReport.cashRegister);
        });
      } else {
        sellerFromCashRegister(cashRegisterService.cashRegister);
      }
    }

    function initEmptyEntity() {
      $scope.createEmptyEntity($scope);
      if (!$scope.entity.printClass) {
        // hakerski default
        $scope.entity.printClass = $scope.guestPrintClass();
      }

      $scope._loadData.promise.then(function () { updateCashRegister(); });
    }

    if (!$routeParams.idEntity) {
      if ($routeParams.idCorrectedPrint) {
        $scope.correctedEntity.$promise.then(initEmptyEntity);
      } else {
        initEmptyEntity();
      }

      $scope.setTitle("<span translate='cashDocument.newTitle.{{printTypeService.printTypeSpecial(entity)}}'></span>", $scope);
    }

    $scope._loadData.promise.then(function () {
      $scope.recalculateSummaries();
    });
  }


  $scope._loadData = $q.defer();


  if ($routeParams.idEntity) {
    CashDocument.get({ idCashDocument: $routeParams.idEntity }, function(print) {
      $scope.entity = print;
      $scope._loadData.resolve();

      $scope.isInvoice = print.fields.isReceipt !== 'true';
      $scope.setTitle("<span><span class='comment' translate='cashDocument.title.{{printTypeService.printTypeSpecial(entity)}}'></span>"
          + "<span class='comment'>: </span>{{entity.fields.number}}</span>", $scope);

      $scope.cashDocumentBodyUrl = '/views/prints/' + printTypeService.printType(print) + '.jsp';
      $scope.loadBuyer($scope.entity.fields['buyer.id']);

      $scope.loadSeller(print.fields['seller.id']);
      $scope.recalculateSummaries();
    });
  } else if ($routeParams.type) {
    $scope.cashDocumentBodyUrl = '/views/prints/' + $routeParams.type + '.jsp';
    // bez $scope._loadData.resolve(), ponieważ poszczególne druki mają różne zachowanie
    // implementować w createEmptyEntity()
  } else if ($routeParams.idCorrectedPrint) {
    $scope.correctedEntity = CashDocument.get({ idCashDocument: $routeParams.idCorrectedPrint }, function(print) {
      $scope.cashDocumentBodyUrl = '/views/prints/corrective' + _.capitalize(printTypeService.printType(print)) + '.jsp';
      $scope._loadData.resolve();
    });
  } else {
    throw new Error('Document type not defined');
  }

  initializeSelect2($scope, "entity.idSeller", "/api/payers", "party", {
    bindEntity: function(seller) { $scope.entity.fields['seller.id'] = seller.id; },
    initSelection: function(element, callback) {
      callback($scope.seller);
    }
  });

  initializeSelect2($scope, "entity.idBuyer", "/api/payers", "party", {
    bindEntity: function(buyer) { $scope.entity.fields['buyer.id'] = buyer.id; },
    initSelection: function(element, callback) {
      callback($scope.buyer);
    }
  });

  $scope.save = saveOrUpdate($scope, 'entity',
      function(entity, headers) {
        toastr.success($filter('translate')("cashDocument.saved." + printTypeService.printTypeSpecial($scope.entity)));
        $scope.entity.id = parseInt(lastUrlElement(headers));
        $location.url("/prints/cashDocument/" + $scope.entity.id);
        $location.replace();
      },
      function() {
        toastr.success($filter('translate')("cashDocument.updated." + printTypeService.printTypeSpecial($scope.entity)));
      });

  $scope.remove = function(position) {
    $scope.entity.elements = _.without($scope.entity.elements, position);
    $scope.recalculateSummaries();
  }
}

