package pl.matsuo.accounting.web.controller.print;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.service.print.InvoiceService;
import pl.matsuo.accounting.test.TestCashRegisterSessionState;
import pl.matsuo.core.test.NumerationConfig;
import pl.matsuo.core.web.controller.AbstractControllerTest;

@ContextConfiguration(
    classes = {
      NumerationConfig.class,
      InvoiceService.class,
      TestCashRegisterSessionState.class,
      CashDocumentController.class
    })
public class TestCashDocumentController extends AbstractControllerTest {

  @Autowired CashDocumentController controller;

  @Test
  public void testFindCashDocumentService() throws Exception {
    assertNotNull(controller.findCashDocumentService("invoice"));
  }

  @Test
  public void testListQuery() throws Exception {
    ICashDocumentParams params =
        facadeBuilder.createFacade(new HashMap<>(), ICashDocumentParams.class);

    assertNotNull(controller.list(params));
  }
}
