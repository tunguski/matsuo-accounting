package pl.matsuo.accounting.web.controller.print;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.service.print.ICashDocumentService;
import pl.matsuo.accounting.service.print.InvoiceService;
import pl.matsuo.accounting.test.TestCashRegisterSessionState;
import pl.matsuo.core.service.numeration.NumerationService;
import pl.matsuo.core.service.numeration.NumerationServiceImpl;
import pl.matsuo.core.web.controller.AbstractControllerTest;

import static org.junit.Assert.*;


@ContextConfiguration(classes = { NumerationServiceImpl.class, InvoiceService.class, TestCashRegisterSessionState.class,
                                  CashDocumentController.class })
public class TestCashDocumentController extends AbstractControllerTest {


  @Autowired
  CashDocumentController controller;


  @Test
  public void testFindCashDocumentService() throws Exception {
    assertNotNull(controller.findCashDocumentService("invoice"));
  }
}