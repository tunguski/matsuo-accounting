package pl.matsuo.accounting.service.print;


import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.DepositSlip;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.test.AbstractPrintTest;

import static org.junit.Assert.*;
import static pl.matsuo.accounting.service.print.CashDocumentTestUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;


@ContextConfiguration(classes = { DepositSlipService.class })
public class TestDepositSlip extends AbstractPrintTest<DepositSlip> {


  @Test
  public void full() throws Exception {
    testCreatePDF(getFullTestDepositSlip(), (html, pdf) -> {
          assertTrue(html.contains("Ryszard Bawełna"));
        });
  }


  @Test
  public void empty() throws Exception {
    testCreatePDF(facadeBuilder.createFacade(new KeyValuePrint(), DepositSlip.class));
  }


  @Test
  public void fullInterface() throws Exception {
    testCreatePDF(getFullTestDepositSlip());
  }


  private DepositSlip getFullTestDepositSlip() {// kp
    return initializeFacade(DepositSlip.class, null,
        depositSlip -> {
          slipTestData.accept(depositSlip);
          depositSlip.setAccountant("Krzysztof Jarzyna");
        },
        position("lek. med. Leszek Kowalski - USG Głowy", bd("12345"), bd("3.21")),
        position("lek. med. Leszek Kowalski - RTG Uda", bd("12345"), bd("5.24")),
        position("lek. med. Leszek Kowalski - RTG Klatki piersiowej", bd("12345"), bd("8.1234"))
    );
  }


  @Override
  protected String getPrintFileName() {
    return "/print/depositSlip.ftl";
  }
}

