package pl.matsuo.accounting.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.service.session.CashRegisterSessionState;
import pl.matsuo.core.web.controller.AbstractSimpleController;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


/**
 * Created by tunguski on 15.09.13.
 */
@RestController
@RequestMapping("/cashRegisters")
public class CashRegisterController extends AbstractSimpleController<CashRegister> {


  @Autowired
  protected CashRegisterSessionState sessionState;


  @RequestMapping(value = "/chooseCashRegister/{id}", method = POST)
  @ResponseStatus(OK)
  public void chooseCashRegister(@PathVariable("id") Integer idCashRegister) {
    sessionState.setCashRegister(database.findById(CashRegister.class, idCashRegister));
  }


  @RequestMapping(value = "/actualCashRegister", method = GET)
  public CashRegister actualCashRegister() {
    return sessionState.getCashRegister();
  }
}

