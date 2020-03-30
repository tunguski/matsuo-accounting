package pl.matsuo.accounting.service.print;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.TotalCost;

@Service
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
    print.setTotalAmount(sum.getSum());
    print.setValue(sum.getSum());

    facade.setAmountAlreadyPaid(amountPaid);
    facade.setAmountDue(sum.getSum().subtract(amountPaid));
    facade.setAmountDueInWords(speakCashAmount(sum.getSum().subtract(amountPaid)));

    facade.setAuthenticityText("ORYGINAŁ");
    facade.setAreCommentsVisible(true);

    return print;
  }
}
