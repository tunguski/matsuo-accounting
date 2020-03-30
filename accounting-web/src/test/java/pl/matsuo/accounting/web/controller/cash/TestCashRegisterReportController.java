package pl.matsuo.accounting.web.controller.cash;

import static org.junit.Assert.*;
import static pl.matsuo.accounting.model.print.AccountingPrint.*;
import static pl.matsuo.accounting.web.controller.report.CashRegisterReportController.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.NumberUtil.*;
import static pl.matsuo.core.web.controller.ControllerTestUtil.*;

import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.test.TestCashRegisterSessionState;
import pl.matsuo.accounting.test.data.MediqCashRegisterTestData;
import pl.matsuo.accounting.test.data.NumerationTestData;
import pl.matsuo.accounting.web.controller.report.CashRegisterReportController;
import pl.matsuo.core.test.data.PayersTestData;
import pl.matsuo.core.web.controller.AbstractControllerTest;
import pl.matsuo.core.web.mvc.MvcConfig;

@WebAppConfiguration
@ContextConfiguration(
    classes = {
      MvcConfig.class, CashRegisterReportController.class, TestCashRegisterSessionState.class,
      PayersTestData.class, MediqCashRegisterTestData.class, NumerationTestData.class
    })
public class TestCashRegisterReportController extends AbstractControllerTest {

  @Autowired CashRegisterReportController controller;

  @Test
  public void testGeneratingNewReport() {
    CashRegisterReport cashRegisterReport =
        controller.reportForCashRegister(database.findOne(query(CashRegisterReport.class)).getId());

    assertEquals(0, cashRegisterReport.getPrints().size());
    assertTrue(
        cashRegisterReport.getStartingBalance().compareTo(cashRegisterReport.getEndingBalance())
            == 0);
  }

  public AccountingPrint createPrint(BigDecimal sum, Integer idCashRegister) {
    AccountingPrint print = print(Invoice.class, null).get();
    print.setIdCashRegister(idCashRegister);
    print.setIdUserCreated(0);
    print.setIdBucket(1);

    Invoice facade = facadeBuilder.createFacade(print);
    facade.getSeller().setId(1);
    print.setValue(sum);

    database.create(print);

    return print;
  }

  @Test
  public void testFindingLastReport() {
    Integer idCashRegister = database.findOne(query(CashRegister.class)).getId();

    createPrint(bd("273"), idCashRegister);

    HttpEntity<CashRegisterReport> httpEntity =
        controller.create(
            controller.reportForCashRegister(idCashRegister), new StringBuffer("/test"));
    Integer id = idFromLocation(httpEntity);

    List<CashRegisterReport> list =
        controller.list(
            queryFacade(ICashRegisterReportControllerQueryRequestParams.class, "last", "true"));
    assertEquals(1, list.size());
    assertEquals(id, list.get(0).getId());

    createPrint(bd("273"), idCashRegister);
    Integer id2 =
        idFromLocation(
            controller.create(
                controller.reportForCashRegister(idCashRegister), new StringBuffer("")));

    List<CashRegisterReport> list2 =
        controller.list(
            queryFacade(ICashRegisterReportControllerQueryRequestParams.class, "last", "true"));
    assertEquals(1, list2.size());
    assertEquals(id2, list2.get(0).getId());
  }

  @Test
  public void testCashRegisterPrintsSummary() {
    Integer idCashRegister = database.findOne(query(CashRegister.class)).getId();
    BigDecimal bigDecimal = controller.cashRegisterPrintsSummary(idCashRegister);

    assertEquals(bd("0"), bigDecimal);
  }
}
