package pl.matsuo.accounting.web.controller.print;

import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.service.session.CashRegisterSessionState;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.web.controller.print.AbstractPrintController;


public abstract class AbstractAccountingPrintController<F extends IPrintFacade>
    extends AbstractPrintController<F, AccountingPrint> {


  @Autowired
  protected CashRegisterSessionState cashRegisterSessionState;
}

