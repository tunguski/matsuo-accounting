package pl.matsuo.accounting.service.print;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.DepositSlip;

@ContextConfiguration(classes = {DepositSlipService.class})
public class TestDepositSlipService extends AbstractAccountingPrintServiceTest {

  @Autowired DepositSlipService depositSlipService;

  @Test
  public void testNumerationName() throws Exception {
    AccountingPrint print = new AccountingPrint();
    DepositSlip depositSlip = facadeBuilder.createFacade(print, DepositSlip.class);
    depositSlip.getSeller().setId(mediq.getId());
    depositSlip.getBuyer().setId(mediq.getId());
    print.setIdBucket(mediq.getIdBucket());
    depositSlipService.numerationName(print, depositSlip);
  }

  @Test
  public void testFillDocument() throws Exception {
    AccountingPrint print = new AccountingPrint();
    DepositSlip depositSlip = facadeBuilder.createFacade(print, DepositSlip.class);
    depositSlip.getSeller().setId(mediq.getId());
    depositSlip.getBuyer().setId(mediq.getId());
    print.setIdBucket(mediq.getIdBucket());
    depositSlipService.fillDocument(print, depositSlip);
  }
}
