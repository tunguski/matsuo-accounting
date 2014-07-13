package pl.matsuo.accounting.service.report;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.service.report.cash.CashRegisterReportService;
import pl.matsuo.accounting.service.report.cash.ICashRegisterReportParams;
import pl.matsuo.accounting.test.data.CashRegisterTestData;
import pl.matsuo.core.test.AbstractReportTest;

import java.util.HashMap;
import java.util.Map;


/**
 * Testy wszystkich druków w systemie.
 * @author Marek Romanowski
 * @since Aug 22, 2013
 */
@ContextConfiguration(classes = { CashRegisterReportService.class, CashRegisterTestData.class })
public class TestCashRegisterReportService extends AbstractReportTest<ICashRegisterReportParams> {


    @Autowired
    CashRegisterReportService cashRegisterReportService;


    @Test
    public void empty() throws Exception {
      // FIXME: naprawić generowanie druku tak, aby bez dokumentu dało się wygenerować pusty szkieletowy plik
//      Map<String, String> params = new HashMap<>();
//      testCreatePDF(facadeBuilder.createFacade(params, ICashRegisterReportParams.class));
    }


    @Test
    public void full() throws Exception {
      Map<String, String> params = new HashMap<>();
      params.put("idReport", "" + database.findAll(CashRegisterReport.class).get(0).getId());

      testCreatePDF(facadeBuilder.createFacade(params, ICashRegisterReportParams.class));
    }


  @Override
  protected String getPrintFileName() {
    return "/print/cashRegisterReport.ftl";
  }
}

