package pl.matsuo.accounting.test.data;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.core.conf.DiscoverTypes;
import pl.matsuo.core.model.numeration.NumerationSchema;
import pl.matsuo.core.service.numeration.MonthlyNumerationSchemaStrategy;
import pl.matsuo.core.test.data.AbstractTestData;
import pl.matsuo.core.test.data.PayersTestData;

@Component
@Order(20)
@DiscoverTypes({PayersTestData.class})
public class NumerationTestData extends AbstractTestData {

  @Override
  public void execute() {
    createNumerationSchema("Invoice", "FV/${numerationYear}/${numerationMonth}/${value}");
    createNumerationSchema("Receipt", "PAR/${numerationYear}/${numerationMonth}/${value}");

    createNumerationSchema(
        "CorrectiveInvoice", "FV-K/${numerationYear}/${numerationMonth}/${value}");
    createNumerationSchema(
        "CorrectiveReceipt", "PAR-K/${numerationYear}/${numerationMonth}/${value}");

    createNumerationSchema("WithdrawSlip", "KW/${numerationYear}/${numerationMonth}/${value}");
    createNumerationSchema("DepositSlip", "KP/${numerationYear}/${numerationMonth}/${value}");
  }

  private void createNumerationSchema(String code, String pattern) {
    NumerationSchema numeration = new NumerationSchema();

    numeration.setValue(1);
    numeration.setMinValue(1);
    numeration.setCode(code);
    numeration.setPattern(pattern);
    numeration.setCreationStrategy(MonthlyNumerationSchemaStrategy.class.getSimpleName());

    database.create(numeration);
  }

  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }
}
