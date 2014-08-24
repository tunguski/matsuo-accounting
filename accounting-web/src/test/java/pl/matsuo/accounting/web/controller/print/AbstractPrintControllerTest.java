package pl.matsuo.accounting.web.controller.print;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.InvoiceCommon;
import pl.matsuo.accounting.test.TestCashRegisterSessionState;
import pl.matsuo.accounting.test.data.MediqCashRegisterTestData;
import pl.matsuo.accounting.test.data.NumerationTestData;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.service.facade.FacadeBuilderMethods;
import pl.matsuo.core.service.print.PrintMethods;
import pl.matsuo.core.test.NumerationConfig;
import pl.matsuo.core.test.data.MediqTestData;
import pl.matsuo.core.test.data.PersonTestData;
import pl.matsuo.core.web.controller.AbstractControllerTest;
import pl.matsuo.core.web.mvc.MvcConfig;

import java.util.Date;

import static java.util.Calendar.*;
import static pl.matsuo.accounting.model.print.AccountingPrint.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.DateUtil.*;


/**
 * Created by tunguski on 02.02.14.
 */
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfig.class, NumerationConfig.class, TestCashRegisterSessionState.class,
                                  PersonTestData.class, MediqCashRegisterTestData.class, NumerationTestData.class })
public abstract class AbstractPrintControllerTest extends AbstractControllerTest
    implements PrintMethods, FacadeBuilderMethods {

  @Autowired
  TestCashRegisterSessionState clinicSessionState;
  @Autowired
  MappingJackson2HttpMessageConverter converter;


  protected AccountingPrint createPrint(Class<? extends InvoiceCommon> clazz) {
    AccountingPrint print = print(clazz, null).get();

    print.setIssuanceDate(new Date());
    print.setDueDate(addTime(new Date(), DATE, 14));
    print.setSellDate(new Date());

    InvoiceCommon invoice = createFacade(print);
    OrganizationUnit organizationUnit =
        database.findOne(query(OrganizationUnit.class, eq("code", MediqTestData.MEDIQ)));
    Person person = database.findOne(query(Person.class, eq("pesel", "42041428579")));
    invoice.getBuyer().setId(person.getId());
    invoice.getSeller().setId(organizationUnit.getId());

    return print;
  }
}

