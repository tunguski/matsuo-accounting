package pl.matsuo.accounting.service.print;

import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.accounting.model.print.CashDocumentParty;
import pl.matsuo.accounting.model.print.InvoiceCommon;
import pl.matsuo.accounting.model.print.SlipCommon;
import pl.matsuo.accounting.model.print.SlipPosition;
import pl.matsuo.accounting.model.print.TotalCost;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.function.Function;

import static pl.matsuo.accounting.model.print.PaymentType.*;
import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;
import static pl.matsuo.core.util.NumberUtil.*;

/**
 * Created by marek on 30.03.14.
 */
public class CashDocumentTestUtil {


  public static final Consumer<CashDocument> partyFiller(Integer id, String name, String address, String nip,
                                                         Function<CashDocument, CashDocumentParty> partyProvider) {
    return invoice -> {
      with(partyProvider.apply(invoice), party -> {
        party.setId(id);
        party.setName(name);
        party.setAddress(address);
        party.setNip(nip);
      });
    };
  }


  public static final Consumer<CashDocument> seller = partyFiller(
      1, "seller full name", "seller<br/>full address", "seller nip", CashDocument::getSeller);
  public static final Consumer<CashDocument> buyer = partyFiller(
      1, "Ryszard Bawełna", "buyer<br/>full address", "buyer nip", CashDocument::getBuyer);


  public static final Consumer<InvoiceCommon> basicData = invoice -> {
    invoice.setBankAccountNumber("26 1050 1445 1000 0022 7647 0461");
    invoice.setDueDate(date(2013, 8, 19));
    invoice.setNumber("LEG/FV/2013/123456");
    invoice.setIssuanceDate(date(2013, 8, 9));
    invoice.setPaymentType(TRANSFER);
    invoice.setSellDate(date(2013, 8, 9));
    invoice.setSellPlace("Warszawa");
    invoice.setAuthenticityText("ORYGINAŁ");
    invoice.setIsReceipt(false);
    invoice.setComments("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod "
        + "temporincididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis "
        + "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis "
        + "aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat "
        + "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui "
        + "officia deserunt mollit anim id est laborum.");
    invoice.setAreCommentsVisible(true);

    TotalCost sum = sumInvoicePositions(invoice);
    BigDecimal amountPaid = bd("13.55");
    invoice.setTotalAmount(sum.getSum());
    invoice.setAmountAlreadyPaid(amountPaid);
    invoice.setAmountDue(sum.getSum().subtract(amountPaid));
    invoice.setAmountDueInWords(speakCashAmount(sum.getSum().subtract(amountPaid)));
  };


  public static final Consumer<SlipCommon> slipTestData = slip -> {
    buyer.accept(slip);
    seller.accept(slip);
    slip.setSellDate(date(2013, 8, 9));
    slip.setAuthenticityText("ORYGINAŁ");

    slip.setCreator("Maciej Stępień");
    slip.setApprovingPerson("Maciej Gołębiowski");
    slip.setCashReportReference("Nr 123, poz. 12");
    slip.setNumber("123551/125");
    slip.setSellPlace("Warszawa");

    BigDecimal sum = sumSlipPositions(slip);
    slip.setTotalAmount(sum);
    slip.setTotalAmountInWords(speakCashAmount(sum));
  };


  public static final Consumer<SlipPosition> position(String serviceName, BigDecimal accountNumber, BigDecimal price) {
    return position -> {
      position.setServiceName(serviceName);
      position.setAccountNumber(accountNumber);
      position.setPrice(price);
    };
  }
}
