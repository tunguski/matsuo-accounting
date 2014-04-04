package pl.matsuo.accounting.web.controller.print;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.InvoiceCommon;
import pl.matsuo.clinic.model.medical.appointment.Appointment;
import pl.matsuo.clinic.model.print.cash.InvoiceCommon;
import pl.matsuo.clinic.test.TestClinicSessionState;
import pl.matsuo.clinic.test.data.CashRegisterTestData;
import pl.matsuo.clinic.test.data.PayersTestData;
import pl.matsuo.clinic.util.PrintMethods;
import pl.matsuo.clinic.web.controller.AbstractControllerTest;
import pl.matsuo.core.model.organization.Company;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.service.facade.FacadeBuilderMethods;
import pl.matsuo.core.service.facade.IFacadeBuilder;
import pl.matsuo.core.service.print.PrintMethods;
import pl.matsuo.core.web.controller.AbstractControllerTest;
import pl.matsuo.core.web.mvc.MvcConfig;

import static pl.matsuo.clinic.model.medical.appointment.AppointmentStatus.*;
import static pl.matsuo.clinic.model.print.Print.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 02.02.14.
 */
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfig.class, TestClinicSessionState.class, CashRegisterTestData.class })
public abstract class AbstractPrintControllerTest extends AbstractControllerTest
    implements PrintMethods, FacadeBuilderMethods {

  @Autowired
  TestClinicSessionState clinicSessionState;
  @Autowired
  MappingJackson2HttpMessageConverter converter;


  @Override
  public IFacadeBuilder getFacadeBuilder() {
    return facadeBuilder;
  }


  protected AccountingPrint createPrint(Class<? extends InvoiceCommon> clazz) {
    Appointment appointment = database.findOne(query(Appointment.class, eq("status", NEW)));
    AccountingPrint print = print(clazz, appointment.getId()).get();

    InvoiceCommon invoice = createFacade(print);
    Company company = database.findOne(query(Company.class, eq("code", PayersTestData.MEDIQ)));
    Person person = database.findOne(query(Person.class, eq("pesel", "42041428579")));
    invoice.getBuyer().setId(person.getId());
    invoice.getSeller().setId(company.getId());

    return print;
  }
}

