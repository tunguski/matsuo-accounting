package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CorrectiveInvoice;
import pl.matsuo.accounting.model.print.CorrectiveInvoicePosition;
import pl.matsuo.accounting.model.print.InvoiceCommon;
import pl.matsuo.accounting.model.print.TotalCost;
import pl.matsuo.core.model.print.KeyValuePrintElement;
import pl.matsuo.core.model.print.initializer.PrintInitializer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static pl.matsuo.accounting.model.print.PaymentType.*;
import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@Service
public class CorrectiveInvoiceService extends CashDocumentService<CorrectiveInvoice> {


  @Override
  protected String numerationName(AccountingPrint print, CorrectiveInvoice invoice) {
    return invoice.getIsReceipt() != null && invoice.getIsReceipt() ? "CorrectiveReceipt" : "CorrectiveInvoice";
  }


  public AccountingPrint forInvoice(@PathVariable("id") Integer id) {
    // basing on invoice print content - in controller we can modify it without worry, because it's not connected to
    // session
    AccountingPrint print = database.findById(AccountingPrint.class, id, new PrintInitializer());
    print.setId(null);
    print.setCreatedTime(null);

    print.setIdCashRegister(null);
    print.setIdCashRegisterReport(null);
    print.setPrintClass(CorrectiveInvoice.class);

    CorrectiveInvoice correctiveInvoice = facadeBuilder.createFacade(print, CorrectiveInvoice.class);

    print.setIssuanceDate(date(new Date(), 0, 0));
    print.setSellDate(date(new Date(), 0, 0));
    print.setDueDate(date(new Date(), 0, 0));
    correctiveInvoice.setPaymentType(CASH);

    List<KeyValuePrintElement> copies = new ArrayList<>();
    for (KeyValuePrintElement printElement : print.getElements()) {
      printElement.setId(null);
      KeyValuePrintElement copy = new KeyValuePrintElement();
      copy.getFields().putAll(printElement.getFields());
      facadeBuilder.createFacade(copy, CorrectiveInvoicePosition.class).setIsAfterCorrection(true);
      copies.add(copy);
    }
    print.getElements().addAll(copies);

    // nadanie testowego numeru faktury
    printNumer(print, correctiveInvoice, true);

    return print;
  }


  protected AccountingPrint fillDocument(AccountingPrint print, CorrectiveInvoice facade) {
    super.fillDocument(print, facade);

    if (facade.getIsReceipt() == null) {
      facade.setIsReceipt(false);
    }

    TotalCost sum = sumInvoicePositions(facade);
    TotalCost correctedSum = sumCorrectedInvoicePositions(facade);
    BigDecimal correctedValue = correctedSum.getSum().subtract(sum.getSum());


    BigDecimal amountPaid = new BigDecimal(0);
    print.setTotalAmount(sum.getSum());
    print.setCashRegisterAmount(correctedValue);

    facade.setAmountAlreadyPaid(amountPaid);
    facade.setAmountDue(correctedValue);
    facade.setAmountDueInWords(speakCashAmount(correctedValue));

    facade.setAuthenticityText("ORYGINAŁ");
    facade.setAreCommentsVisible(true);

    return print;
  }
}

