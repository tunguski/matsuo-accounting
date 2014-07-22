package pl.matsuo.accounting.web.controller.cash;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.service.session.CashRegisterSessionState;
import pl.matsuo.accounting.test.data.CashRegisterTestData;
import pl.matsuo.accounting.web.controller.report.CashRegisterReportController;
import pl.matsuo.core.service.numeration.NumerationServiceImpl;
import pl.matsuo.core.test.data.NumerationTestData;
import pl.matsuo.core.test.data.PayersTestData;
import pl.matsuo.core.web.controller.AbstractControllerTest;
import pl.matsuo.core.web.mvc.MvcConfig;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static pl.matsuo.accounting.model.print.AccountingPrint.*;
import static pl.matsuo.accounting.web.controller.report.CashRegisterReportController.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.NumberUtil.*;
import static pl.matsuo.core.web.controller.ControllerTestUtil.*;


/**
 * Created by tunguski on 18.09.13.
 */
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfig.class, CashRegisterReportController.class, CashRegisterSessionState.class,
                                  NumerationServiceImpl.class, PayersTestData.class, CashRegisterTestData.class,
                                  NumerationTestData.class })
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


  public AccountingPrint createPrint(BigDecimal sum, Integer idCashRegister) {
    AccountingPrint print = print(Invoice.class, null).get();
    print.setIdCashRegister(idCashRegister);
    print.setIdUserCreated(0);

    Invoice facade = facadeBuilder.createFacade(print);
    print.setCashRegisterAmount(sum);

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

