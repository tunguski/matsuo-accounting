

<div ng-controller="CashRegisterReportsController">

  <form class="form">
    <div class="row">
      <div class="span12">
        <h4>Lista raportów</h4>

        <input ui-select2="cashRegister.options" name="entity_cashRegister" ng-model="cashRegister.value" class="span4">
      </div>
    </div>
  </form>

  <div class="row">
    <div class="span12">

      <table class="table table-condensed table-hover table-striped input-table">
        <thead>
        <tr>
          <th>Kod</th>
          <th>Data raportu</th>
          <th>Godzina</th>
          <th class="number">Stan przed</th>
          <th class="number">Stan po</th>
        </tr>
        </thead>

        <tbody>
        <tr ng-repeat="cashRegisterReport in filteredReports">
          <td>{{cashRegisterReport.cashRegister.code}}</td>
          <td><a href="#/cash/cashRegisterReport/{{cashRegisterReport.id}}">{{cashRegisterReport.createdTime | formatDate}}</a></td>
          <td>{{cashRegisterReport.createdTime | formatTime}}</td>
          <td class="number">{{cashRegisterReport.startingBalance | number:2}} zł</td>
          <td class="number">{{cashRegisterReport.endingBalance | number:2}} zł</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>

</div>

