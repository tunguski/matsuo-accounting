package pl.matsuo.accounting.service.print;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.SlipPosition;
import pl.matsuo.accounting.model.print.WithdrawSlip;

import static pl.matsuo.accounting.service.print.CashDocumentTestUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;


@ContextConfiguration(classes = { WithdrawSlipPrintService.class })
public class TestWithdrawSlip extends AbstractAccountingPrintTest<WithdrawSlip, SlipPosition> {


  @Test
  public void full() throws Exception {
    testCreatePDF(getFullWithdrawSlip());
  }


  private WithdrawSlip getFullWithdrawSlip() { // kw
    return createAccountingPrint(
        withdrawSlip -> slipTestData.accept(withdrawSlip),
        cashPrintBaseData,
        position("lek. med. Leszek Kowalski - USG Głowy", bd("12345"), bd("3.21")),
        position("lek. med. Leszek Kowalski - RTG Uda", bd("12345"), bd("5.24")),
        position("lek. med. Leszek Kowalski - RTG Klatki piersiowej", bd("12345"), bd("8.1234"))
    );
  }
}

