package pl.matsuo.accounting.service.print;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.WithdrawSlip;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.test.AbstractPrintTest;

import static pl.matsuo.accounting.service.print.CashDocumentTestUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;


@ContextConfiguration(classes = { WithdrawSlipService.class })
public class TestWithdrawSlip extends AbstractPrintTest<WithdrawSlip> {


  @Test
  public void full() throws Exception {
    testCreatePDF(facadeBuilder.createFacade(getFullWithdrawSlip(), WithdrawSlip.class));
  }


  @Test
  public void empty() throws Exception {
    testCreatePDF(facadeBuilder.createFacade(new KeyValuePrint(), WithdrawSlip.class));
  }


  private KeyValuePrint getFullWithdrawSlip() { // kw
    return initializePrint(WithdrawSlip.class, null, withdrawSlip -> slipTestData.accept(withdrawSlip),
        position("lek. med. Leszek Kowalski - USG Głowy", bd("12345"), bd("3.21")),
        position("lek. med. Leszek Kowalski - RTG Uda", bd("12345"), bd("5.24")),
        position("lek. med. Leszek Kowalski - RTG Klatki piersiowej", bd("12345"), bd("8.1234"))
    );
  }


  @Override
  protected String getPrintFileName() {
    return "/print/withdrawSlip.ftl";
  }
}

