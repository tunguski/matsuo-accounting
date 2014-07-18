package pl.matsuo.accounting.web.controller.print;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.InvoicePosition;
import pl.matsuo.accounting.service.print.InvoiceService;
import pl.matsuo.accounting.test.TestCashRegisterSessionState;
import pl.matsuo.accounting.test.data.CashRegisterTestData;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.print.KeyValuePrintElement;
import pl.matsuo.core.service.numeration.NumerationServiceImpl;
import pl.matsuo.core.test.data.NumerationTestData;
import pl.matsuo.core.test.data.PayersTestData;
import pl.matsuo.core.test.data.PersonTestData;
import pl.matsuo.core.web.controller.AbstractDbControllerRequestTest;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.matsuo.accounting.model.print.AccountingPrint.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.NumberUtil.*;
import static pl.matsuo.core.web.controller.ControllerTestUtil.*;


@ContextConfiguration(classes = { CashDocumentController.class, InvoiceService.class, NumerationServiceImpl.class,
                                  TestCashRegisterSessionState.class, PersonTestData.class, CashRegisterTestData.class,
                                  NumerationTestData.class})
public class TestInvoiceControllerRequest extends AbstractDbControllerRequestTest {


  @Autowired
  protected TestCashRegisterSessionState clinicSessionState;


  private AccountingPrint createPrint() {
    AccountingPrint print = print(Invoice.class, null).get();

    Invoice invoice = facadeBuilder.createFacade(print);

    OrganizationUnit organizationUnit = database.findOne(query(OrganizationUnit.class, eq("code", PayersTestData.MEDIQ)));
    Person person = database.findOne(query(Person.class, eq("pesel", "42041428579")));
    invoice.getBuyer().setId(person.getId());
    invoice.getSeller().setId(organizationUnit.getId());

    return print;
  }


  private KeyValuePrintElement addRandomPosition(AccountingPrint print) {
    KeyValuePrintElement printElement = new KeyValuePrintElement();
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
    AccountingPrint print = createPrint();
    addRandomPosition(print);
    addRandomPosition(print);

    clinicSessionState.setIdCashRegister(database.findAll(CashRegister.class).get(0).getId());

    ResultActions resultActions = mockMvc.perform(post("/cashDocuments/invoice", print));
    resultActions.andExpect(status().isCreated());

    addRandomPosition(print);
    addRandomPosition(print);

    mockMvc.perform(put("/cashDocuments/" + idFromLocation(resultActions))
         .content(objectMapper.writeValueAsString(print)).contentType(APPLICATION_JSON))
         .andExpect(status().isNoContent());
  }
}

