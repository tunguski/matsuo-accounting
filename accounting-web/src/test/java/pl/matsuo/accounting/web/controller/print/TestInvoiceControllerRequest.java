package pl.matsuo.accounting.web.controller.print;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import pl.matsuo.clinic.model.cash.CashRegister;
import pl.matsuo.clinic.model.print.cash.Invoice;
import pl.matsuo.clinic.model.print.cash.InvoicePosition;
import pl.matsuo.clinic.service.medical.services.ServicesService;
import pl.matsuo.clinic.service.numeration.NumerationServiceImpl;
import pl.matsuo.clinic.test.data.AppointmentTestData;
import pl.matsuo.clinic.test.data.CashRegisterTestData;
import pl.matsuo.clinic.test.data.NumerationTestData;
import pl.matsuo.clinic.test.data.PayersTestData;
import pl.matsuo.clinic.web.controller.AbstractControllerRequestTest;
import pl.matsuo.core.model.organization.Company;
import pl.matsuo.core.model.organization.Person;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.matsuo.clinic.model.print.Print.*;
import static pl.matsuo.clinic.web.controller.ControllerTestUtil.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.NumberUtil.*;


@ContextConfiguration(classes = { InvoiceController.class, ServicesService.class, NumerationServiceImpl.class,
                                  NumerationTestData.class, CashRegisterTestData.class, AppointmentTestData.class })
public class TestInvoiceControllerRequest extends AbstractControllerRequestTest {


  private Print createPrint() {
    Print print = print(Invoice.class, null).get();

    Invoice invoice = facadeBuilder.createFacade(print);

    Company company = database.findOne(query(Company.class, eq("code", PayersTestData.MEDIQ)));
    Person person = database.findOne(query(Person.class, eq("pesel", "42041428579")));
    invoice.getBuyer().setId(person.getId());
    invoice.getSeller().setId(company.getId());

    return print;
  }


  private PrintElement addRandomPosition(Print print) {
    PrintElement printElement = new PrintElement();
    printElement.getFields().put("key", "" + Math.random());

    print.getElements().add(printElement);

    Invoice invoice = facadeBuilder.createFacade(print);
    InvoicePosition invoicePosition = invoice.getElements().get(invoice.getElements().size() - 1);
    invoicePosition.setCount(bd("1"));
    invoicePosition.setPrice(bd("100"));
    invoicePosition.setTaxRate("7");

    return printElement;
  }


  @Test
  public void listAppointments() throws Exception {
    Print print = createPrint();
    addRandomPosition(print);
    addRandomPosition(print);

    clinicSessionState.setCashRegister(database.findAll(CashRegister.class).get(0));

    ResultActions resultActions = mockMvc.perform(post("/invoices", print));
    resultActions.andExpect(status().isCreated());

    addRandomPosition(print);
    addRandomPosition(print);

    mockMvc.perform(put("/invoices/" + idFromLocation(resultActions))
         .content(objectMapper.writeValueAsString(print)).contentType(APPLICATION_JSON))
         .andExpect(status().isNoContent());
  }
}

