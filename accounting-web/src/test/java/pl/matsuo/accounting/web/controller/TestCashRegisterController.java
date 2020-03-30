package pl.matsuo.accounting.web.controller;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.service.session.CashRegisterSessionState;
import pl.matsuo.accounting.web.controller.print.AbstractPrintControllerTest;
import pl.matsuo.core.service.session.SessionState;

@ContextConfiguration(classes = {CashRegisterController.class})
public class TestCashRegisterController extends AbstractPrintControllerTest {

  @Autowired CashRegisterController cashRegisterController;
  @Autowired SessionState sessionState;
  @Autowired CashRegisterSessionState cashRegisterSessionState;

  @Test
  public void testChooseCashRegister() throws Exception {
    cashRegisterSessionState.setIdCashRegister(null);

    Integer id = database.findOne(query(CashRegister.class)).getId();
    cashRegisterController.chooseCashRegister(id);

    assertEquals(id, cashRegisterSessionState.getIdCashRegister());
  }

  @Test
  public void testActualCashRegister() throws Exception {
    Integer id = database.findOne(query(CashRegister.class)).getId();
    cashRegisterSessionState.setIdCashRegister(id);

    CashRegister cashRegister = cashRegisterController.actualCashRegister();
    assertEquals(id, cashRegister.getId());
  }

  @Test
  public void testActualCashRegister2() throws Exception {
    sessionState.setIdBucket(-10);
    cashRegisterSessionState.setIdCashRegister(null);

    database.create(new CashRegister());

    CashRegister cashRegister = cashRegisterController.actualCashRegister();
    assertNotNull(cashRegister);
  }

  @Test
  public void testActualCashRegister3() throws Exception {
    sessionState.setIdBucket(null);
    cashRegisterSessionState.setIdCashRegister(null);

    CashRegister cashRegister = cashRegisterController.actualCashRegister();
    assertNull(cashRegister);
  }
}
