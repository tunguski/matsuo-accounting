package pl.matsuo.accounting.service.print;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.junit.Test;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.core.model.print.IPrintElementFacade;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.test.AbstractPrintTest;

public abstract class AbstractAccountingPrintTest<
        E extends IPrintFacade<F>, F extends IPrintElementFacade>
    extends AbstractPrintTest<E> {

  @Test
  public void empty() throws Exception {
    testCreatePDF(facadeBuilder.createFacade(new AccountingPrint(), printType));
  }

  protected E createAccountingPrint(
      Consumer<? super E> initializeFacade,
      BiConsumer<AccountingPrint, ? super E> initializePrint,
      Consumer<? super F>... elementInitializations) {
    AccountingPrint accountingPrint = new AccountingPrint();
    E facade =
        getFacadeBuilder()
            .createFacade(
                initializePrint(
                    accountingPrint, printType, initializeFacade, elementInitializations),
                printType);

    initializePrint.accept(accountingPrint, facade);

    return facade;
  }
}
