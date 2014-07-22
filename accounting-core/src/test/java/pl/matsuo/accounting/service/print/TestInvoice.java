package pl.matsuo.accounting.service.print;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.InvoicePosition;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.*;
import static pl.matsuo.accounting.service.print.CashDocumentTestUtil.*;
import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;
import static pl.matsuo.core.util.collection.CollectionUtil.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;


/**
 * Testy generowania faktury.
 *
 * @author Marek Romanowski
 * @since Aug 22, 2013
 */
@ContextConfiguration(classes = { InvoicePrintService.class })
public class TestInvoice extends AbstractAccountingPrintTest<Invoice, InvoicePosition> {


  public static Function<Integer, Consumer<InvoicePosition>> createPosition = i ->
    invoicePosition("spaceholder " + i, bd(100).add(bd(i)), bd(100).add(bd(i)), "13");


  @Test
  public void full() throws Exception {
    testCreatePDF(getFullInvoice());
  }


  @Test
  public void receipt() throws Exception {
    Invoice invoice = getFullInvoice();
    invoice.setIsReceipt(true);
    testCreatePDF(invoice);
  }


  protected Invoice getFullInvoice() {
    return createAccountingPrint(
        compose(buyer, seller, basicData),
        printBaseData,
        positions());
  }


  Consumer<InvoicePosition>[] positions() {
    return flatten(asList(
        invoicePosition("lek. med. Leszek Kowalski - RTG Klatki piersiowej", bd("2"), bd("3.21"), "22"),
        invoicePosition("lek. med. Leszek Kowalski - USG szyi", bd("4"), bd("5.21"), "7"),
        invoicePosition("wydruk ksero", bd("24"), bd("0.21"), "zw")),
        range(3, 50).map(createPosition).collect(Collectors.toList())
    ).toArray(new Consumer[0]);
  }
}

