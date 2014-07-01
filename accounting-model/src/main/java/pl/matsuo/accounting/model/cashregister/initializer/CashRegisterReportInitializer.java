package pl.matsuo.accounting.model.cashregister.initializer;

import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.core.model.Initializer;
import pl.matsuo.core.model.print.initializer.PrintInitializer;


/**
 * Created by tunguski on 20.09.13.
 */
public class CashRegisterReportInitializer implements Initializer<CashRegisterReport> {


  PrintInitializer printInitializer = new PrintInitializer();


  @Override
  public void init(CashRegisterReport report) {
    if (report.getPrints() != null) {
      for (AccountingPrint print : report.getPrints()) {
        printInitializer.init(print);
      }
    }
  }
}

