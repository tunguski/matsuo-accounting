package pl.matsuo.accounting.web.controller.print;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.InvoiceCommon;
import pl.matsuo.accounting.test.TestCashRegisterSessionState;
import pl.matsuo.accounting.test.data.CashRegisterTestData;
import pl.matsuo.core.model.organization.Company;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.service.facade.FacadeBuilderMethods;
import pl.matsuo.core.service.numeration.NumerationServiceImpl;
import pl.matsuo.core.service.print.PrintMethods;
import pl.matsuo.core.test.data.NumerationTestData;
import pl.matsuo.core.test.data.PayersTestData;
import pl.matsuo.core.test.data.PersonTestData;
import pl.matsuo.core.web.controller.AbstractControllerTest;
import pl.matsuo.core.web.mvc.MvcConfig;

import static pl.matsuo.accounting.model.print.AccountingPrint.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 02.02.14.
 */
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfig.class, NumerationServiceImpl.class, TestCashRegisterSessionState.class,
                                  PersonTestData.class, CashRegisterTestData.class, NumerationTestData.class })
public abstract class AbstractPrintControllerTest extends AbstractControllerTest
    implements PrintMethods, FacadeBuilderMethods {

  @Autowired
  TestCashRegisterSessionState clinicSessionState;
  @Autowired
  MappingJackson2HttpMessageConverter converter;


  protected AccountingPrint createPrint(Class<? extends InvoiceCommon> clazz) {
    AccountingPrint print = print(clazz, null).get();

    InvoiceCommon invoice = createFacade(print);
    Company company = database.findOne(query(Company.class, eq("code", PayersTestData.MEDIQ)));
    Person person = database.findOne(query(Person.class, eq("pesel", "42041428579")));
    invoice.getBuyer().setId(person.getId());
    invoice.getSeller().setId(company.getId());

    return print;
  }
}

