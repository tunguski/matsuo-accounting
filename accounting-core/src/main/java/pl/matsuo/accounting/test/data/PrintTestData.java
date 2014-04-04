package pl.matsuo.accounting.test.data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.accounting.model.print.CashDocumentUtil;
import pl.matsuo.accounting.model.print.DepositSlip;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.InvoicePosition;
import pl.matsuo.accounting.model.print.PaymentType;
import pl.matsuo.accounting.model.print.SlipPosition;
import pl.matsuo.accounting.model.print.TotalCost;
import pl.matsuo.accounting.model.print.WithdrawSlip;
import pl.matsuo.accounting.util.PrintUtil;
import pl.matsuo.core.model.organization.Company;
import pl.matsuo.core.model.organization.address.Address;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.print.KeyValuePrintElement;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.facade.IFacadeBuilder;
import pl.matsuo.core.service.print.PrintMethods;
import pl.matsuo.core.test.data.AbstractTestData;
import pl.matsuo.core.test.data.PayersTestData;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.*;
import static pl.matsuo.accounting.model.print.PaymentType.*;
import static pl.matsuo.core.model.print.KeyValuePrint.*;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.test.data.PayersTestData.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;
import static pl.matsuo.core.util.NumberUtil.*;


@Component
@Order(50)
public class PrintTestData extends AbstractTestData implements PrintMethods {


  @Autowired
  protected IFacadeBuilder facadeBuilder;


  Function<Address, String> addressText = address -> {
    return PrintUtil.noNull(address.getZipCode()) + " " + PrintUtil.noNull(address.getTown()) + ", " + PrintUtil.noNull(address.getStreet()); };


  @Override
  public void execute() {
//    List<Appointment> appointments = database.findAll(Appointment.class);
//    sort(appointments);

    savePrints(createTestInvoice(null),
        createTestInvoice_2(null),
        createTestDepositSlip(null),
        createTestWithdrawSlip(null));
  }


  protected void savePrints(KeyValuePrint ... prints) {
    User user = database.findOne(query(User.class, eq("username", "admin")));

    for (KeyValuePrint print : prints) {
      print.setIdUserCreated(user.getId());
      database.create(print);
    }
  }


  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }


  private void rewriteParties(CashDocument cashDocument, String sellerCode, String buyerCode) {
    Object o1 = cashDocument.getBuyer();
    Object o2 = cashDocument.getSeller();
    CashDocumentUtil.rewriteParty(cashDocument.getBuyer(), database.findOne(query(Company.class, eq("code", buyerCode))));
    CashDocumentUtil.rewriteParty(cashDocument.getSeller(), database.findOne(query(Company.class, eq("code", sellerCode))));
  }


  private KeyValuePrint createTestWithdrawSlip(Integer id) { // kw
    KeyValuePrint print = print(WithdrawSlip.class, id).get();
    WithdrawSlip withdrawSlip = facadeBuilder.createFacade(print);
    SlipPosition printElementFacade;

    print.getElements().add(new KeyValuePrintElement());
    print.getElements().add(new KeyValuePrintElement());
    print.getElements().add(new KeyValuePrintElement());

    printElementFacade = withdrawSlip.getElements().get(0);
    printElementFacade.setServiceName("Morfologia");
    printElementFacade.setAccountNumber(bd("12345"));
    printElementFacade.setPrice(bd("3.21"));

    printElementFacade = withdrawSlip.getElements().get(1);
    printElementFacade.setServiceName("APTT");
    printElementFacade.setAccountNumber(bd("12345"));
    printElementFacade.setPrice(bd("5.24"));

    printElementFacade = withdrawSlip.getElements().get(2);
    printElementFacade.setServiceName("OB");
    printElementFacade.setAccountNumber(bd("12345"));
    printElementFacade.setPrice(bd("8.12"));

    rewriteParties(withdrawSlip, "REMONT", MEDIQ);

    withdrawSlip.setSellDate(date(2013, 8, 9));
    withdrawSlip.setAuthenticityText("ORYGINAŁ");

    withdrawSlip.setCreator("Maciej Stępień");
    withdrawSlip.setApprovingPerson("Maciej Gołębiowski");
    withdrawSlip.setCashReportReference("Nr 123, poz. 12");
    withdrawSlip.setNumber("123551/125");
    withdrawSlip.setSellPlace("Warszawa");

    BigDecimal sum = PrintUtil.sumSlipPositions(withdrawSlip);
    withdrawSlip.setTotalAmount(sum);
    withdrawSlip.setCashRegisterAmount(sum.negate());
    withdrawSlip.setTotalAmountInWords(speakCashAmount(sum));
    return print;
  }


  private KeyValuePrint createTestDepositSlip(Integer id) { // kp
    return initializePrint(DepositSlip.class, id, depositSlip -> {
          rewriteParties(depositSlip, "REMONT", MEDIQ);

          depositSlip.setSellDate(date(2013, 8, 9));
          depositSlip.setAuthenticityText("ORYGINAŁ");

          depositSlip.setCreator("Maciej Stępień");
          depositSlip.setAccountant("Krzysztof Jarzyna");
          depositSlip.setApprovingPerson("Maciej Gołębiowski");
          depositSlip.setCashReportReference("Nr 123, poz. 12");
          depositSlip.setNumber("123551/125");
          depositSlip.setSellPlace("Warszawa");

          BigDecimal sum = PrintUtil.sumSlipPositions(depositSlip);
          depositSlip.setTotalAmount(sum);
          depositSlip.setCashRegisterAmount(sum);
          depositSlip.setTotalAmountInWords(speakCashAmount(sum));
        },

        printElementFacade -> {
          printElementFacade.setServiceName("Morfologia");
          printElementFacade.setAccountNumber(bd("12345"));
          printElementFacade.setPrice(bd("3.21"));
        },
        printElementFacade -> {
          printElementFacade.setServiceName("APTT");
          printElementFacade.setAccountNumber(bd("12345"));
          printElementFacade.setPrice(bd("5.24"));
        },
        printElementFacade -> {
          printElementFacade.setServiceName("OB");
          printElementFacade.setAccountNumber(bd("12345"));
          printElementFacade.setPrice(bd("8.12"));
        }
    );
  }


  private KeyValuePrint createTestInvoice_2(Integer id) {
    KeyValuePrint print = print(Invoice.class, id).get();

    Invoice invoice = facadeBuilder.createFacade(print);
    InvoicePosition printElementFacade;

    print.getElements().add(new KeyValuePrintElement());
    printElementFacade = invoice.getElements().get(0);
    printElementFacade.setServiceName("Wapń w moczu");
    printElementFacade.setCount(bd("5"));
    printElementFacade.setPrice(bd("3.21"));
    printElementFacade.setTaxRate("22");

    print.getElements().add(new KeyValuePrintElement());
    printElementFacade = invoice.getElements().get(1);
    printElementFacade.setServiceName("Morfologia");
    printElementFacade.setCount(bd("1"));
    printElementFacade.setPrice(bd("3.21"));
    printElementFacade.setTaxRate("22");

    print.getElements().add(new KeyValuePrintElement());
    printElementFacade = invoice.getElements().get(2);
    printElementFacade.setServiceName("APTT");
    printElementFacade.setCount(bd("2"));
    printElementFacade.setPrice(bd("3.21"));
    printElementFacade.setTaxRate("22");

    print.getElements().add(new KeyValuePrintElement());
    printElementFacade = invoice.getElements().get(3);
    printElementFacade.setServiceName("OB");
    printElementFacade.setCount(bd("4"));
    printElementFacade.setPrice(bd("5.21"));
    printElementFacade.setTaxRate("7");

    rewriteParties(invoice, "REMONT", MEDIQ);

    invoice.setBankAccountNumber("26 1050 1445 1000 0022 7647 0461");
    invoice.setDueDate(date(2013, 8, 19));
    invoice.setNumber("LEG/FV/2013/123456");
    invoice.setIssuanceDate(date(2013, 8, 9));
    invoice.setPaymentType(TRANSFER);
    invoice.setSellDate(date(2013, 8, 9));
    invoice.setSellPlace("Warszawa");
    invoice.setAuthenticityText("ORYGINAŁ");
    invoice.setComments("komentarz");
    invoice.setAreCommentsVisible(true);

    TotalCost sum = PrintUtil.sumInvoicePositions(invoice);
    BigDecimal amountPaid = bd("13.55");
    invoice.setTotalAmount(sum.getSum());
    invoice.setAmountAlreadyPaid(amountPaid);
    invoice.setCashRegisterAmount(amountPaid);
    invoice.setAmountDue(sum.getSum().subtract(amountPaid));
    invoice.setAmountDueInWords(speakCashAmount(sum.getSum().subtract(amountPaid)));

    return print;
  }


  private KeyValuePrint createTestInvoice(Integer id) {
    KeyValuePrint print = print(Invoice.class, id).get();

    Invoice invoice = facadeBuilder.createFacade(print);
    InvoicePosition printElementFacade;

    print.getElements().add(new KeyValuePrintElement());
    printElementFacade = invoice.getElements().get(0);
    printElementFacade.setServiceName("APTT");
    printElementFacade.setCount(bd("2"));
    printElementFacade.setPrice(bd("3.21"));
    printElementFacade.setTaxRate("22");

    print.getElements().add(new KeyValuePrintElement());
    printElementFacade = invoice.getElements().get(1);
    printElementFacade.setServiceName("OB");
    printElementFacade.setCount(bd("4"));
    printElementFacade.setPrice(bd("5.21"));
    printElementFacade.setTaxRate("7");

    rewriteParties(invoice, "REMONT", MEDIQ);

    invoice.setBankAccountNumber("26 1050 1445 1000 0022 7647 0461");
    invoice.setDueDate(date(2013, 8, 19));
    invoice.setNumber("LEG/FV/2013/123456");
    invoice.setIssuanceDate(date(2013, 8, 9));
    invoice.setPaymentType(TRANSFER);
    invoice.setSellDate(date(2013, 8, 9));
    invoice.setSellPlace("Warszawa");
    invoice.setAuthenticityText("ORYGINAŁ");
    invoice.setComments("komentarz");
    invoice.setAreCommentsVisible(true);

    TotalCost sum = PrintUtil.sumInvoicePositions(invoice);
    BigDecimal amountPaid = bd("13.55");
    invoice.setTotalAmount(sum.getSum());
    invoice.setAmountAlreadyPaid(amountPaid);
    invoice.setCashRegisterAmount(amountPaid);
    invoice.setAmountDue(sum.getSum().subtract(amountPaid));
    invoice.setAmountDueInWords(speakCashAmount(sum.getSum().subtract(amountPaid)));

    return print;
  }

  @Override
  public IFacadeBuilder getFacadeBuilder() {
    return facadeBuilder;
  }
}

