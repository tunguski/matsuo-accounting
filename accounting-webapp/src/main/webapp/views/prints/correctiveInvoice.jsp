<div ng-controller="CorrectiveInvoiceController">

  <jsp:include page="invoiceGeneralData.jsp"></jsp:include>

  <jsp:include page="invoiceTable.jsp">
    <jsp:param name="readonly" value="true" />
  </jsp:include>

  <jsp:include page="invoiceTable.jsp">
    <jsp:param name="corrective" value="true" />
  </jsp:include>

  <div class="row">
    <div class="span3 offset8">
      <table class="table input-table">
        <tr>
          <td class="number invoice-summary">Wartość korekty:</td>
          <td class="number summary invoice-summary">{{correctionValue() | number:2 }} zł</td>
        </tr>
      </table>
    </div>
  </div>
</div>
