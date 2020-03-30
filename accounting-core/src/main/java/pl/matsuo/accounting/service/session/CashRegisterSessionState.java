package pl.matsuo.accounting.service.session;

import static org.springframework.context.annotation.ScopedProxyMode.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@Scope(value = "wideSession", proxyMode = TARGET_CLASS)
@Getter
@Setter
public class CashRegisterSessionState {

  private Integer idCashRegister;
}
