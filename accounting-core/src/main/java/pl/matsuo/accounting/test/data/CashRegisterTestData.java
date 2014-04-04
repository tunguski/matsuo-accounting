package pl.matsuo.accounting.test.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.core.conf.DiscoverTypes;
import pl.matsuo.core.model.organization.Company;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.test.data.AbstractTestData;
import pl.matsuo.core.test.data.PayersTestData;

import static java.math.BigDecimal.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.NumberUtil.*;


/**
 * Created by tunguski on 15.09.13.
 */
@Component
@DiscoverTypes({ PayersTestData.class, PrintTestData.class })
@Order(60)
public class CashRegisterTestData extends AbstractTestData {


  @Autowired
  protected FacadeBuilder facadeBuilder;


  @Override
  public void execute() {
    Company mediq = database.findOne(query(Company.class, eq("code", PayersTestData.MEDIQ)));
    Company onet = database.findOne(query(Company.class, eq("code", PayersTestData.ONET)));

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
    for (AccountingPrint print : cashRegisterReport.getPrints()) {
      cashRegisterReport.setEndingBalance(cashRegisterReport.getEndingBalance().add(
          facadeBuilder.createFacade(print, CashDocument.class).getCashRegisterAmount()));
    }


    database.create(cashRegisterReport);
  }


  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }
}

