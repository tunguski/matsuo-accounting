package pl.matsuo.accounting.web.controller.print;

import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.service.session.ClinicSessionState;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.Initializer;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.initializer.PrintInitializer;
import pl.matsuo.core.service.session.SessionState;
import pl.matsuo.core.web.controller.AbstractSimpleController;
import pl.matsuo.core.web.controller.print.AbstractPrintController;

import java.util.List;

import static java.util.Arrays.*;
import static org.springframework.core.GenericTypeResolver.*;


public abstract class AbstractAccountingPrintController<F extends IPrintFacade>
    extends AbstractPrintController<F, AccountingPrint> {


  @Autowired
  protected ClinicSessionState clinicSessionState;
}

