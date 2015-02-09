package pl.matsuo.accounting.service.print;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CorrectiveInvoice;
import pl.matsuo.accounting.model.print.Invoice;


@ContextConfiguration(classes = { CorrectiveInvoiceService.class })
public class TestCorrectiveInvoiceService extends AbstractAccountingPrintServiceTest {


  @Autowired
  CorrectiveInvoiceService correctiveInvoiceService;


  @Test
  public void testNumerationName() throws Exception {
    AccountingPrint print = new AccountingPrint();
    CorrectiveInvoice invoice = facadeBuilder.createFacade(print, CorrectiveInvoice.class);
    invoice.getSeller().setId(mediq.getId());
    invoice.getBuyer().setId(mediq.getId());
    print.setIdBucket(mediq.getIdBucket());
    correctiveInvoiceService.numerationName(print, invoice);
  }


  @Test
  public void testForInvoice() throws Exception {
    AccountingPrint print = new AccountingPrint();
    print.setIdBucket(mediq.getIdBucket());
    print.setPrintClass(Invoice.class);
    database.create(print);

    correctiveInvoiceService.forInvoice(print.getId());
  }


  @Test
  public void testFillDocument() throws Exception {
    AccountingPrint print = new AccountingPrint();
    CorrectiveInvoice invoice = facadeBuilder.createFacade(print, CorrectiveInvoice.class);
    invoice.getSeller().setId(mediq.getId());
    invoice.getBuyer().setId(mediq.getId());
    print.setIdBucket(mediq.getIdBucket());
    correctiveInvoiceService.fillDocument(print, invoice);
  }
}

