<%@page import="pl.matsuo.accounting.model.print.Invoice"%>
<%@page import="pl.matsuo.core.web.view.BootstrapRenderer"%>
<%!  BootstrapRenderer renderer = BootstrapRenderer.renderer(); %>

<div class="row">
  <div class="span6">
    <%= renderer.create(Invoice.class).entityName("entity.fields").render("issuanceDate", "sellPlace", "sellDate", "dueDate") %>
  </div>

  <div ng-show="isInvoice" class="span6">
    <%= renderer.create(Invoice.class).entityName("entity.fields").render("number", "paymentType", "bankAccountNumber", "comments") %>
  </div>
  <div ng-show="!isInvoice" class="span6">
    <%= renderer.create(Invoice.class).entityName("entity.fields")
                .attribute("translate", "entity.fields.receiptNumber")
                .attribute("translate-placeholder", "entity.fields.receiptNumber")
                .render("number") %>
  </div>
</div>
