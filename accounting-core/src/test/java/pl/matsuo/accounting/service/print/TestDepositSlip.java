package pl.matsuo.accounting.service.print;


import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.DepositSlip;
import pl.matsuo.accounting.model.print.SlipPosition;

import static org.junit.Assert.*;
import static pl.matsuo.accounting.service.print.CashDocumentTestUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;


@ContextConfiguration(classes = { DepositSlipPrintService.class })
public class TestDepositSlip extends AbstractAccountingPrintTest<DepositSlip, SlipPosition> {


  @Test
  public void full() throws Exception {
    testCreatePDF(getFullTestDepositSlip(), (html, pdf) -> {
          assertTrue(html.contains("Ryszard Bawełna"));
        });
  }


  private DepositSlip getFullTestDepositSlip() {
    return createAccountingPrint(
        depositSlip -> {
          slipTestData.accept(depositSlip);
          depositSlip.setAccountant("Krzysztof Jarzyna");
        },
        cashPrintBaseData,
        position("lek. med. Leszek Kowalski - USG Głowy", bd(12345), bd("3.21")),
        position("lek. med. Leszek Kowalski - RTG Uda", bd(12345), bd("5.24")),
        position("lek. med. Leszek Kowalski - RTG Klatki piersiowej", bd(12345), bd("8.1234"))
    );
  }
}

