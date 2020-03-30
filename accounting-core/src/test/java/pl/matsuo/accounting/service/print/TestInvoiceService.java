package pl.matsuo.accounting.service.print;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;

@ContextConfiguration(classes = {InvoiceService.class})
public class TestInvoiceService extends AbstractAccountingPrintServiceTest {

  @Autowired InvoiceService invoiceService;

  @Test
  public void testNumerationName() throws Exception {
    AccountingPrint print = new AccountingPrint();
    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);
    invoice.getSeller().setId(mediq.getId());
    invoice.getBuyer().setId(mediq.getId());
    print.setIdBucket(mediq.getIdBucket());
    invoiceService.numerationName(print, invoice);
  }

  @Test
  public void testFillDocument() throws Exception {
    AccountingPrint print = new AccountingPrint();
    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);
    invoice.getSeller().setId(mediq.getId());
    invoice.getBuyer().setId(mediq.getId());
    print.setIdBucket(mediq.getIdBucket());
    invoiceService.fillDocument(print, invoice);
  }
}
