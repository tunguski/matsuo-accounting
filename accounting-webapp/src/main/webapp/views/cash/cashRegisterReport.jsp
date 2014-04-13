

<div ng-controller="CashRegisterReportController">

  <div class="row">
    <div class="span12">
      <h4>Filtruj druki</h4>

      <div simple-search query="query"></div>

      <h4>Szczegóły raportu</h4>

      <table class="table table-condensed table-hover table-striped input-table">
        <thead>
          <tr>
            <th>Lp.</th>
            <th>Typ</th>
            <th>Utworzony</th>
            <th>Użytkownik</th>
            <th>Opłacił</th>
            <th>Usługi</th>
            <th class="number">Wartość</th>
          </tr>
        </thead>

        <tbody>
          <tr ng-repeat="print in entity.prints | orderBy:'id' | filter:query">
            <td>{{$index + 1}}</td>
            <td>{{"cashDocument.shortName." + printTypeService.printTypeSpecial(print) | translate}}</td>
            <td><a href="#/prints/cashDocument/{{print.id}}">{{print.createdTime | formatDate}} {{print.createdTime | formatTime}}</a></td>
            <td>{{print.userCreated.username}}</td>
            <td>{{print.fields.buyer}}</td>
            <td>
              <ul>
                <li ng-repeat="position in print.elements">{{position.fields.serviceName}}</li>
              </ul>
            </td>
            <td class="number">{{ print.fields.cashRegisterAmount | number:2 }} zł</td>
          </tr>

          <tr>
            <td colspan="5"></td>
            <td class="number"><b>Suma:</b></td>
            <td class="number"><b>{{ sumCashDocuments() | number:2 }} zł</b></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <div class="row">
    <div class="span4">
      <h4>Podsumowanie</h4>

      <table class="table table-condensed table-hover table-striped input-table">
        <tbody>
        <tr>
          <th>Liczba pozycji</th>
          <td class="number">{{entity.prints.length}}</td>
          <td class="mini"></td>
        </tr>
        <tr>
          <th>Data raportu</th>
          <td class="number"> {{entity.createdTime | formatDate}}</td>
          <td class="mini"></td>
        </tr>
        <tr>
          <th>Wartość początkowa</th>
          <td class="number">{{entity.startingBalance | number:2}}</td>
          <td class="mini">zł</td>
        </tr>
        <tr>
          <th>Wartość końcowa</th>
          <td class="number">{{entity.endingBalance | number:2}}</td>
          <td class="mini">zł</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>

  <div class="form-actions">
    <button type="submit" ng-disabled="entity.id || entity.prints.length == 0"
            ng-click="save()" class="btn btn-primary">Zapisz raport</button>
    <a class="btn" href="javascript:;" onclick="window.history.back()" translate="cancel"></a>
    <a ng-if="entity.id" class="btn btn-warning" ng-href="#/prints/cashDocument?type=withdrawSlip&idCashReport={{entity.id}}">Wydaj do sejfu</a>
    <a ng-if="entity.id" class="btn btn-info" ng-href="/api/reports/pdf/cashRegisterReport?idReport={{entity.id}}" target="_blank">Pobierz pdf</a>
    <a ng-if="entity.id" class="btn btn-info" ng-href="/api/reports/xls/cashRegisterReport?idReport={{entity.id}}" target="_blank">Pobierz xls</a>
  </div>
</div>

