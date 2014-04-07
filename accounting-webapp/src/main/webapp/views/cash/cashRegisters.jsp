

<div ng-controller="CashRegistersController">

  <div class="row">
    <div class="span12">
      <h4>Lista kas</h4>

      <div simple-search query="query"></div>

      <table class="table table-condensed table-hover table-striped input-table">
        <thead>
        <tr>
          <th>Nazwa</th>
          <th>Podmiot</th>
          <th>Nip</th>
          <th class="number">Aktualny stan</th>
          <th class="number">Ostatnie rozliczenie</th>
          <th class="number">Nierozliczone</th>
          <th>Funkcje</th>
        </tr>
        </thead>

        <tbody>
          <tr ng-repeat="cashRegister in cashRegisters | filter:query">
            <td>{{cashRegister.code}}</td>
            <td>{{cashRegister.reckoningParty.code}}</td>
            <td>{{cashRegister.reckoningParty.nip}}</td>
            <td class="number">{{cashRegister.value | number:2}} zł</td>
            <td class="number">{{cashRegister.report.endingBalance || 0  | number:2}} zł</td>
            <td class="number">{{cashRegister.notReckonedSummary | number:2}} zł</td>
            <td>
              <a href="#/cash/cashRegisterReports?idCashRegister={{cashRegister.id}}"><i class="icon-list-ul"></i></a>
              <a href="#/cash/cashRegisterReport?idCashRegister={{cashRegister.id}}"><i class="icon-file-text"></i></a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

</div>

