package pl.matsuo.accounting.web.controller.cash;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.clinic.model.cash.CashRegister;
import pl.matsuo.clinic.model.cash.CashRegisterReport;
import pl.matsuo.clinic.model.print.cash.Invoice;
import pl.matsuo.clinic.service.medical.services.ServicesService;
import pl.matsuo.clinic.service.numeration.NumerationServiceImpl;
import pl.matsuo.clinic.service.session.ClinicSessionState;
import pl.matsuo.clinic.test.data.CashRegisterTestData;
import pl.matsuo.clinic.test.data.NumerationTestData;
import pl.matsuo.clinic.test.data.PatientTestData;
import pl.matsuo.clinic.test.data.PayersTestData;
import pl.matsuo.clinic.web.controller.AbstractControllerTest;
import pl.matsuo.core.web.mvc.MvcConfig;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static pl.matsuo.clinic.model.print.Print.*;
import static pl.matsuo.clinic.web.controller.ControllerTestUtil.*;
import static pl.matsuo.clinic.web.controller.cash.CashRegisterReportController.*;
import static pl.matsuo.core.util.NumberUtil.*;


/**
 * Created by tunguski on 18.09.13.
 */
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfig.class,
                                    CashRegisterReportController.class, ClinicSessionState.class,
                                    ServicesService.class, NumerationServiceImpl.class, PatientTestData.class,
                                    PayersTestData.class, CashRegisterTestData.class, NumerationTestData.class })
public class TestCashRegisterReportController extends AbstractControllerTest {


  @Autowired
  CashRegisterReportController controller;


  @Test
  public void testGeneratingNewReport() {
    CashRegisterReport cashRegisterReport = controller.reportForCashRegister(
        database.findOne(query(CashRegisterReport.class)).getId());

    assertEquals(0, cashRegisterReport.getPrints().size());
    assertTrue(cashRegisterReport.getStartingBalance().compareTo(cashRegisterReport.getEndingBalance()) == 0);
  }


  public Print createPrint(BigDecimal sum, Integer idCashRegister) {
    Print print = print(Invoice.class, null).get();
    print.setIdCashRegister(idCashRegister);
    print.setIdUserCreated(0);

    Invoice facade = facadeBuilder.createFacade(print);
    facade.setCashRegisterAmount(sum);

    database.create(print);

    return print;
  }


  @Test
  public void testFindingLastReport() {
    Integer idCashRegister = database.findOne(query(CashRegister.class)).getId();

    createPrint(bd("273"), idCashRegister);

    HttpEntity<CashRegisterReport> httpEntity = controller.create(
        controller.reportForCashRegister(idCashRegister), new StringBuffer("/test"));
    Integer id = idFromLocation(httpEntity);

    List<CashRegisterReport> list =
        controller.list(queryFacade(ICashRegisterReportControllerQueryRequestParams.class, "last", "true"));
    assertEquals(1, list.size());
    assertEquals(id, list.get(0).getId());

    createPrint(bd("273"), idCashRegister);
    Integer id2 = idFromLocation(controller.create(
        controller.reportForCashRegister(idCashRegister), new StringBuffer("")));

    List<CashRegisterReport> list2 =
      controller.list(queryFacade(ICashRegisterReportControllerQueryRequestParams.class, "last", "true"));
    assertEquals(1, list2.size());
    assertEquals(id2, list2.get(0).getId());
  }
}

