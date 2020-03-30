package pl.matsuo.accounting.service.print;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.WithdrawSlip;

@ContextConfiguration(classes = {WithdrawSlipService.class})
public class TestWithdrawSlipService extends AbstractAccountingPrintServiceTest {

  @Autowired WithdrawSlipService withdrawSlipService;

  @Test
  public void testNumerationName() throws Exception {
    AccountingPrint print = new AccountingPrint();
    WithdrawSlip withdrawSlip = facadeBuilder.createFacade(print, WithdrawSlip.class);
    withdrawSlip.getSeller().setId(mediq.getId());
    withdrawSlip.getBuyer().setId(mediq.getId());
    print.setIdBucket(mediq.getIdBucket());
    withdrawSlipService.numerationName(print, withdrawSlip);
  }

  @Test
  public void testFillDocument() throws Exception {
    AccountingPrint print = new AccountingPrint();
    WithdrawSlip withdrawSlip = facadeBuilder.createFacade(print, WithdrawSlip.class);
    withdrawSlip.getSeller().setId(mediq.getId());
    withdrawSlip.getBuyer().setId(mediq.getId());
    print.setIdBucket(mediq.getIdBucket());
    withdrawSlipService.fillDocument(print, withdrawSlip);
  }
}
