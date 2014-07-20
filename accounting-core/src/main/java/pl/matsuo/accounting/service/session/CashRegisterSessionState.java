package pl.matsuo.accounting.service.session;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.springframework.context.annotation.ScopedProxyMode.*;


/**
 * Created by tunguski on 16.09.13.
 */
@Component
@Order(0)
@Scope(value = "wideSession", proxyMode = TARGET_CLASS)
public class CashRegisterSessionState {


  private Integer idCashRegister;


  public Integer getIdCashRegister() {
    return idCashRegister;
  }
  public void setIdCashRegister(Integer idCashRegister) {
    this.idCashRegister = idCashRegister;
  }
}

