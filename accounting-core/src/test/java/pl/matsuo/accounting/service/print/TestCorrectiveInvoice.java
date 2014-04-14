package pl.matsuo.accounting.service.print;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.print.CorrectiveInvoice;
import pl.matsuo.accounting.model.print.CorrectiveInvoicePosition;
import pl.matsuo.accounting.model.print.TotalCost;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.service.facade.FacadeBuilderMethods;
import pl.matsuo.core.test.AbstractPrintTest;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.accounting.service.print.CashDocumentTestUtil.*;
import static pl.matsuo.core.util.collection.CollectionUtil.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;
import static pl.matsuo.core.util.NumberUtil.*;


@ContextConfiguration(classes = {CorrectiveInvoiceService.class})
public class TestCorrectiveInvoice extends AbstractPrintTest<CorrectiveInvoice> {


  public static Function<Integer, Consumer<CorrectiveInvoicePosition>> createCorrectivePosition = i -> {
    return correctiveInvoicePosition("spaceholder 2 " + i, bd("100").add(bd("" + i)), bd("0.1").add(bd("" + i)), "14");
  };


  @Test
  public void empty() throws Exception {
    testCreatePDF(facadeBuilder.createFacade(new KeyValuePrint(), CorrectiveInvoice.class));
  }


  @Test
  public void full() throws Exception {
    testCreatePDF(getFullCorrectiveInvoice(), (html, pdf) -> {
      // corrective element must be only in one table - splitting should return 2 parts
      assertEquals(2, html.split("spaceholder 2 3").length);
    });
  }


  protected CorrectiveInvoice getFullCorrectiveInvoice() {
    return initializeFacade(CorrectiveInvoice.class, null,
        compose(buyer, seller, basicData, invoice -> {
          TotalCost sumAfterCorrection = sumCorrectedInvoicePositions(invoice);
          invoice.setAmountDueAfterCorrection(sumAfterCorrection.getSum());
          invoice.setAmountDueAfterCorrectionInWords(speakCashAmount(sumAfterCorrection.getSum()));

          TotalCost sum = sumInvoicePositions(invoice);
          invoice.setAmountDue(sum.getSum());
          invoice.setAmountDueInWords(speakCashAmount(sum.getSum()));
        }), positions()
    );
  }


  Consumer<CorrectiveInvoicePosition>[] positions() {
    return flatten(asList(
            invoicePosition("lek. med. Leszek Kowalski - RTG Klatki piersiowej", bd("2"), bd("3.21"), "22"),
            invoicePosition("lek. med. Leszek Kowalski - USG szyi", bd("4"), bd("5.21"), "7"),
            invoicePosition("wydruk ksero", bd("24"), bd("0.21"), "zw")),
        range(3, 5).map(TestInvoice.createPosition).collect(Collectors.toList()),
        range(3, 5).map(createCorrectivePosition).collect(Collectors.toList())
    ).toArray(new Consumer[0]);
  }


  @Override
  protected String getPrintFileName() {
    return "/print/correctiveInvoice.ftl";
  }
}

