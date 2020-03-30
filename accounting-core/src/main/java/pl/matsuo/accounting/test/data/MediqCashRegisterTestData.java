package pl.matsuo.accounting.test.data;

import static java.math.BigDecimal.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.NumberUtil.*;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.core.conf.DiscoverTypes;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.test.data.AbstractMediqTestData;
import pl.matsuo.core.test.data.MediqTestData;
import pl.matsuo.core.test.data.PayersTestData;

@Component
@DiscoverTypes({PayersTestData.class, PrintTestData.class})
@Order(60)
public class MediqCashRegisterTestData extends AbstractMediqTestData {

  @Override
  public void internalExecute() {
    OrganizationUnit mediq =
        database.findOne(
            query(OrganizationUnit.class, eq(OrganizationUnit::getCode, MediqTestData.MEDIQ)));
    OrganizationUnit onet =
        database.findOne(
            query(OrganizationUnit.class, eq(OrganizationUnit::getCode, PayersTestData.ONET)));

    CashRegister cashRegister = new CashRegister();
    cashRegister.setCode("Kasa 1");
    cashRegister.setValue(bd("120.30"));
    cashRegister.setReckoningParty(mediq);
    database.create(cashRegister);

    cashRegister = new CashRegister();
    cashRegister.setCode("Kasa 2");
    cashRegister.setValue(bd("721.00"));
    cashRegister.setReckoningParty(mediq);
    database.create(cashRegister);

    cashRegister = new CashRegister();
    cashRegister.setCode("Kasa Test Onet");
    cashRegister.setValue(bd("1850.50"));
    cashRegister.setReckoningParty(onet);
    database.create(cashRegister);

    createCashRegisterReport();
  }

  private void createCashRegisterReport() {
    CashRegisterReport cashRegisterReport = new CashRegisterReport();
    cashRegisterReport.setCashRegister(database.findOne(query(CashRegister.class)));

    cashRegisterReport.getPrints().addAll(database.findAll(AccountingPrint.class));
    cashRegisterReport.setStartingBalance(ZERO);
    cashRegisterReport.setEndingBalance(ZERO);
    cashRegisterReport
        .getPrints()
        .forEach(
            print ->
                cashRegisterReport.setEndingBalance(
                    cashRegisterReport.getEndingBalance().add(print.getCashRegisterAmount())));

    database.create(cashRegisterReport);
  }

  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }
}
