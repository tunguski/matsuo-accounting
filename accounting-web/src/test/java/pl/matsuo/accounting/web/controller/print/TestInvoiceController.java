package pl.matsuo.accounting.web.controller.print;


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
import pl.matsuo.clinic.model.cash.CashRegister;
import pl.matsuo.clinic.model.medical.appointment.Appointment;
import pl.matsuo.clinic.model.print.cash.Invoice;
import pl.matsuo.clinic.model.print.cash.InvoicePosition;
import pl.matsuo.clinic.model.print.initializer.PrintInitializer;
import pl.matsuo.clinic.service.medical.services.ServicesService;
import pl.matsuo.clinic.service.numeration.NumerationServiceImpl;
import pl.matsuo.clinic.test.data.AppointmentTestData;
import pl.matsuo.clinic.test.data.NumerationTestData;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.KeyValuePrintElement;
import pl.matsuo.core.model.print.initializer.PrintInitializer;
import pl.matsuo.core.web.controller.exception.RestProcessingException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static pl.matsuo.clinic.model.medical.appointment.AppointmentStatus.*;
import static pl.matsuo.core.util.NumberUtil.*;


/**
 * Klasa specjalnie nie jest transakcyjna! Testuje poprawność transakcji w kontenerze.
 * @author Marek Romanowski
 * @since Aug 21, 2013
 */
@ContextConfiguration(classes = { InvoiceController.class, ServicesService.class,
                                  NumerationServiceImpl.class, AppointmentTestData.class, NumerationTestData.class })
public class TestInvoiceController extends AbstractPrintControllerTest {


  @Autowired
  InvoiceController controller;


  private KeyValuePrintElement addRandomPosition(KeyValuePrint print) {
    KeyValuePrintElement printElement = new PrintElement();
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

    clinicSessionState.setCashRegister(database.findAll(CashRegister.class).get(0));

    HttpEntity<Print> httpEntity = controller.create(print, new StringBuffer());
    String url = httpEntity.getHeaders().getLocation().toString();
    String id = url.substring(url.lastIndexOf("/") + 1);

    AccountingPrint savedPrint = database.findById(AccountingPrint.class, i(id), new PrintInitializer());
    database.evict(savedPrint);

    Appointment appointment = database.findById(Appointment.class, print.getIdEntity());
    assertEquals(AWAITING, appointment.getStatus());

    MockClientHttpRequest request = new MockClientHttpRequest();
    converter.write(savedPrint, APPLICATION_JSON, request);
    MockClientHttpResponse response = new MockClientHttpResponse(request.getBodyAsBytes(), OK);
    Print resultObject = (Print) converter.read(Print.class, response);

    controller.update(resultObject);

    Print updatedPrint = database.findById(Print.class, i(id));

    assertEquals(print.getId(), updatedPrint.getId());
  }


  @Test(expected = RestProcessingException.class)
  public void noCashRegisterInState() throws Exception {
    Print print = createPrint(Invoice.class);

    addRandomPosition(print);
    addRandomPosition(print);

    HttpEntity<Print> httpEntity = controller.create(print, new StringBuffer());
    String url = httpEntity.getHeaders().getLocation().toString();
    String id = url.substring(url.lastIndexOf("/") + 1);

    Print savedPrint = database.findById(Print.class, i(id), new PrintInitializer());
    database.evict(savedPrint);

    MockClientHttpRequest request = new MockClientHttpRequest();
    converter.write(savedPrint, APPLICATION_JSON, request);
    MockClientHttpResponse response = new MockClientHttpResponse(request.getBodyAsBytes(), OK);
    Print resultObject = (Print) converter.read(Print.class, response);

    controller.update(resultObject);

    Print updatedPrint = database.findById(Print.class, i(id));

    assertEquals(print.getId(), updatedPrint.getId());
  }
}

