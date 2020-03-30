package pl.matsuo.accounting.model.cashregister.initializer;

import static org.mockito.Mockito.*;

import org.junit.Test;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;

public class TestCashRegisterReportInitializer {

  @Test
  public void testInit() throws Exception {
    CashRegisterReport report = new CashRegisterReport();
    AccountingPrint print = mock(AccountingPrint.class);
    report.getPrints().add(print);

    new CashRegisterReportInitializer().init(report);

    verify(print, times(2)).getFields();
  }
}
