package pl.matsuo.accounting.web.controller.print;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.TotalCost;

import java.math.BigDecimal;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@RestController
@RequestMapping("/invoices")
public class InvoiceController extends CashDocumentController<Invoice> {


  protected String numerationName(Invoice invoice) {
    return invoice.getIsReceipt() != null && invoice.getIsReceipt() ? "RECEIPT" : "INVOICE";
  }


  protected AccountingPrint fillDocument(AccountingPrint print, Invoice facade) {
    super.fillDocument(print, facade);

    if (facade.getIsReceipt() == null) {
      facade.setIsReceipt(false);
    }

    TotalCost sum = sumInvoicePositions(facade);
    BigDecimal amountPaid = new BigDecimal(0);
    facade.setTotalAmount(sum.getSum());
    facade.setCashRegisterAmount(sum.getSum());

    facade.setAmountAlreadyPaid(amountPaid);
    facade.setAmountDue(sum.getSum().subtract(amountPaid));
    facade.setAmountDueInWords(speakCashAmount(sum.getSum().subtract(amountPaid)));

    facade.setAuthenticityText("ORYGINAŁ");
    facade.setAreCommentsVisible(true);

    return print;
  }
}

