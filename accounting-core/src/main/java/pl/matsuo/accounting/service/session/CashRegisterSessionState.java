package pl.matsuo.accounting.service.session;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.accounting.model.cashregister.CashRegister;

import static org.springframework.context.annotation.ScopedProxyMode.*;


/**
 * Created by tunguski on 16.09.13.
 */
@Component
@Order(0)
@Scope(value = "session", proxyMode = TARGET_CLASS)
public class CashRegisterSessionState {


  private CashRegister cashRegister;


  public CashRegister getCashRegister() {
    return cashRegister;
  }
  public void setCashRegister(CashRegister cashRegister) {
    this.cashRegister = cashRegister;
  }
}

