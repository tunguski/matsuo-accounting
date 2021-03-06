package pl.matsuo.accounting.web.controller.print;

import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static pl.matsuo.core.util.NumberUtil.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.InvoicePosition;
import pl.matsuo.accounting.service.print.InvoiceService;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.KeyValuePrintElement;
import pl.matsuo.core.model.print.initializer.PrintInitializer;

/**
 * Klasa specjalnie nie jest transakcyjna! Testuje poprawność transakcji w kontenerze.
 *
 * @since Aug 21, 2013
 */
@ContextConfiguration(classes = {CashDocumentController.class, InvoiceService.class})
public class TestInvoiceController extends AbstractPrintControllerTest {

  @Autowired CashDocumentController controller;

  private KeyValuePrintElement addRandomPosition(KeyValuePrint print) {
    KeyValuePrintElement printElement = new KeyValuePrintElement();
    printElement.getFields().put("key", "" + Math.random());

    print.getElements().add(printElement);

    Invoice invoice = createFacade(print);
    InvoicePosition invoicePosition = invoice.getElements().get(invoice.getElements().size() - 1);
    invoicePosition.setCount(bd("1"));
    invoicePosition.setPrice(bd("100"));
    invoicePosition.setTaxRate("7");

    return printElement;
  }

  @Test
  public void createAndUpdateInvoice() throws Exception {
    AccountingPrint print = createPrint(Invoice.class);

    addRandomPosition(print);
    addRandomPosition(print);

    clinicSessionState.setIdCashRegister(database.findAll(CashRegister.class).get(0).getId());

    HttpEntity<AccountingPrint> httpEntity =
        controller.create("invoice", print, new StringBuffer());
    String url = httpEntity.getHeaders().getLocation().toString();
    String id = url.substring(url.lastIndexOf("/") + 1);

    AccountingPrint savedPrint =
        database.findById(AccountingPrint.class, i(id), new PrintInitializer());
    database.evict(savedPrint);

    MockClientHttpRequest request = new MockClientHttpRequest();
    converter.write(savedPrint, APPLICATION_JSON, request);
    MockClientHttpResponse response = new MockClientHttpResponse(request.getBodyAsBytes(), OK);
    AccountingPrint resultObject =
        (AccountingPrint) converter.read(AccountingPrint.class, response);

    controller.update(resultObject);

    AccountingPrint updatedPrint = database.findById(AccountingPrint.class, i(id));

    assertEquals(print.getId(), updatedPrint.getId());
  }

  @Test(expected = RestProcessingException.class)
  public void noCashRegisterInState() throws Exception {
    AccountingPrint print = createPrint(Invoice.class);
    // clear buyer id in invoice - when no cashRegister is set, this is natuarl situation
    Invoice invoice = createFacade(print);
    invoice.getBuyer().setId(null);

    addRandomPosition(print);
    addRandomPosition(print);

    HttpEntity<AccountingPrint> httpEntity = controller.create(print, new StringBuffer());
    String url = httpEntity.getHeaders().getLocation().toString();
    String id = url.substring(url.lastIndexOf("/") + 1);

    AccountingPrint savedPrint =
        database.findById(AccountingPrint.class, i(id), new PrintInitializer());
    database.evict(savedPrint);

    MockClientHttpRequest request = new MockClientHttpRequest();
    converter.write(savedPrint, APPLICATION_JSON, request);
    MockClientHttpResponse response = new MockClientHttpResponse(request.getBodyAsBytes(), OK);
    AccountingPrint resultObject =
        (AccountingPrint) converter.read(AccountingPrint.class, response);

    controller.update(resultObject);

    AccountingPrint updatedPrint = database.findById(AccountingPrint.class, i(id));

    assertEquals(print.getId(), updatedPrint.getId());
  }
}
