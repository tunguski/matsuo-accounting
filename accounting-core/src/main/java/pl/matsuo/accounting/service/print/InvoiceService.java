package pl.matsuo.accounting.service.print;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.TotalCost;

import java.math.BigDecimal;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


public class InvoiceService extends CashDocumentService<Invoice> {


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

