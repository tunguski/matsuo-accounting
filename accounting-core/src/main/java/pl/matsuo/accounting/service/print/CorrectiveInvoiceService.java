package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.accounting.model.print.PaymentType.*;
import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@Service
public class CorrectiveInvoiceService extends CashDocumentService<CorrectiveInvoice> {


  protected String numerationName(InvoiceCommon invoice) {
    return invoice.getIsReceipt() != null && invoice.getIsReceipt() ? "CORRECTIVE_RECEIPT" : "CORRECTIVE_INVOICE";
  }


  @Override
  protected void preCreate(AccountingPrint print, CorrectiveInvoice cashDocument) {
    // jeśli to faktura, dostaje swój numer
    cashDocument.setNumber(numerationService.getNumber(numerationName(cashDocument)));
  }


  public AccountingPrint forInvoice(@PathVariable("id") Integer id) {
    AccountingPrint invoice = database.findById(AccountingPrint.class, id, new PrintInitializer());
    invoice.setId(null);
    invoice.setCreatedTime(null);

    invoice.setIdCashRegister(null);
    invoice.setIdCashRegisterReport(null);
    invoice.setPrintClass(CorrectiveInvoice.class);

    CorrectiveInvoice correctiveInvoice = facadeBuilder.createFacade(invoice, CorrectiveInvoice.class);

    correctiveInvoice.setIssuanceDate(date(new Date(), 0, 0));
    correctiveInvoice.setSellDate(date(new Date(), 0, 0));
    correctiveInvoice.setDueDate(date(new Date(), 0, 0));
    correctiveInvoice.setPaymentType(CASH);

    List<KeyValuePrintElement> copies = new ArrayList<>();
    for (KeyValuePrintElement printElement : invoice.getElements()) {
      printElement.setId(null);
      KeyValuePrintElement copy = new KeyValuePrintElement();
      copy.getFields().putAll(printElement.getFields());
      facadeBuilder.createFacade(copy, CorrectiveInvoicePosition.class).setIsAfterCorrection(true);
      copies.add(copy);
    }
    invoice.getElements().addAll(copies);

    // nadanie testowego numeru faktury
    correctiveInvoice.setNumber(numerationService.getPreviewNumber(numerationName(correctiveInvoice)));

    return invoice;
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
    facade.setTotalAmount(sum.getSum());
    facade.setCashRegisterAmount(correctedValue);

    facade.setAmountAlreadyPaid(amountPaid);
    facade.setAmountDue(correctedValue);
    facade.setAmountDueInWords(speakCashAmount(correctedValue));

    facade.setAuthenticityText("ORYGINAŁ");
    facade.setAreCommentsVisible(true);

    return print;
  }
}

