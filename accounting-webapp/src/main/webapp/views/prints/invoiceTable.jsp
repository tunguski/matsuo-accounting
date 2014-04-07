<%
  String collectionName = request.getParameter("corrective") != null ? "corrective" : "invoice";
  Boolean readonly = request.getParameter("readonly") != null;
%>

<div class="row">
  <div class="span12">
    <h4 translate="cashDocument.positions.<%=collectionName%>_{{!!invoice.fields.isReceipt}}">Pozycje</h4>

    <table class="table table-condensed table-hover table-striped input-table">
      <thead>
      <tr>
        <th class="number">Nr</th>
        <th style="width: 50%;">Nazwa towaru</th>
        <th ng-if="isInvoice">PKWiU</th>
        <th>J.m.</th>
        <th class="number">Ilość</th>
        <th class="number">Jedn. Netto</th>
        <th class="number">{{ isInvoice ? "Netto" : "Brutto" }}</th>
        <th ng-if="isInvoice" class="number">VAT %</th>
        <th ng-if="isInvoice" class="number">VAT kwota</th>
        <th ng-if="isInvoice" class="number">Brutto</th>
        <th></th>
      </tr>
      </thead>
      <tbody>

      <tr ng-repeat="position in positions['<%=collectionName%>'].elements">
        <td class="number">{{$index + 1}}.</td>
        <td><input <%=readonly ? "ng-disabled=\"true\"" : ""%> type="text" ng-model="position.fields.serviceName"></td>
        <td ng-if="isInvoice">{{position.fields.pkwiu}}</td>
        <td>{{position.fields.jm}}</td>
        <td class="number"><input <%=readonly ? "ng-disabled=\"true\"" : ""%> type="text" ng-change="recalculateSummaries()" ng-model="position.fields.count"></td>
        <td class="number"><input <%=readonly ? "ng-disabled=\"true\"" : ""%> type="text" ng-change="recalculateSummaries()" ng-model="position.fields.price"></td>
        <td class="number">{{priceNetto(position) | number:2 }}</td>
        <td ng-if="isInvoice" class="number nowrap">
          <input <%=readonly ? "ng-disabled=\"true\"" : ""%> type="text" ng-change="recalculateSummaries()" ng-model="position.fields.taxRate" style="width: 30px;">
        </td>
        <td ng-if="isInvoice" class="number">{{taxValue(position) | number:2 }}</td>
        <td ng-if="isInvoice" class="number">{{priceBrutto(position) | number:2 }}</td>
        <td><i class="icon-remove <%=readonly ? "hidden" : ""%>" ng-click="remove(position)"></i> </td>
      </tr>

      <tr ng-if="isInvoice">
        <td colspan="5"></td>
        <td class="number">Razem:</td>
        <td class="number">{{ positions['<%=collectionName%>'].summaryNetto | number:2 }}</td>
        <td></td>
        <td></td>
        <td class="number">{{ positions['<%=collectionName%>'].summaryBrutto | number:2 }}</td>
        <td></td>
      </tr>

      <tr ng-if="isInvoice">
        <td colspan="5"></td>
        <td class="number">w tym</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>

      <tr ng-show="isInvoice" ng-repeat="taxSummary in positions['<%=collectionName%>'].taxSummaries">
        <td colspan="6"></td>
        <td class="number summary">{{ taxSummary.summaryNetto | number:2 }}</td>
        <td class="number summary nowrap">{{taxSummary.tax}}</td>
        <td class="number summary">{{ taxSummary.summaryTaxValue | number:2 }}</td>
        <td class="number summary">{{ taxSummary.summaryBrutto | number:2 }}</td>
        <td></td>
      </tr>

      <tr>
        <td ng-if="isInvoice" colspan="4"></td>
        <td colspan="3" class="number nowrap invoice-summary">Do zapłaty</td>
        <td class="number summary invoice-summary" colspan="3">{{ positions['<%=collectionName%>'].summaryBrutto | number:2 }}</td>
        <td></td>
      </tr>

      </tbody>
    </table>

    <h4><a class="icon-plus-sign <%=readonly ? "hidden" : ""%>" ng-click="addInvoicePosition()"></a></h4>
  </div>
</div>
