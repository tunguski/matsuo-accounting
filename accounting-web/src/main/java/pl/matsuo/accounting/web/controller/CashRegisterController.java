package pl.matsuo.accounting.web.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.service.session.CashRegisterSessionState;
import pl.matsuo.core.web.controller.AbstractSimpleController;

@RestController
@RequestMapping("/cashRegisters")
public class CashRegisterController extends AbstractSimpleController<CashRegister> {

  @Autowired protected CashRegisterSessionState sessionState;

  @RequestMapping(value = "/chooseCashRegister/{id}", method = POST)
  @ResponseStatus(OK)
  public void chooseCashRegister(@PathVariable("id") Integer idCashRegister) {
    // searches CashRegister in db to be sure it exists
    sessionState.setIdCashRegister(database.findById(CashRegister.class, idCashRegister).getId());
  }

  @RequestMapping(value = "/actualCashRegister", method = GET)
  public CashRegister actualCashRegister() {
    if (sessionState.getIdCashRegister() == null) {
      // if there is only one cash register for the company, set it automatically as default
      List<CashRegister> cashRegisters = database.findAll(CashRegister.class);
      if (cashRegisters.size() == 1) {
        sessionState.setIdCashRegister(cashRegisters.get(0).getId());
        return cashRegisters.get(0);
      }
    } else {
      return database.findById(CashRegister.class, sessionState.getIdCashRegister());
    }

    return null;
  }
}
