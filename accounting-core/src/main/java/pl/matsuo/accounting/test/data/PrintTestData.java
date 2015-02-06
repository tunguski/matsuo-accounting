package pl.matsuo.accounting.test.data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.accounting.model.print.DepositSlip;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.InvoicePosition;
import pl.matsuo.accounting.model.print.SlipPosition;
import pl.matsuo.accounting.model.print.TotalCost;
import pl.matsuo.accounting.model.print.WithdrawSlip;
import pl.matsuo.accounting.util.PrintUtil;
import pl.matsuo.core.conf.DiscoverTypes;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.address.Address;
import pl.matsuo.core.model.print.KeyValuePrintElement;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.facade.IFacadeBuilder;
import pl.matsuo.core.service.print.PrintMethods;
import pl.matsuo.core.test.data.AbstractMediqTestData;
import pl.matsuo.core.test.data.PayersTestData;

import java.math.BigDecimal;
import java.util.function.Function;

import static pl.matsuo.accounting.model.print.AccountingPrint.*;
import static pl.matsuo.accounting.model.print.CashDocumentUtil.*;
import static pl.matsuo.accounting.model.print.PaymentType.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.test.data.MediqTestData.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;
import static pl.matsuo.core.util.NumberUtil.*;


@Component
@Order(50)
@DiscoverTypes({ PayersTestData.class })
public class PrintTestData extends AbstractMediqTestData implements PrintMethods {


  @Autowired
  protected IFacadeBuilder facadeBuilder;


  Function<Address, String> addressText = address -> {
    return PrintUtil.noNull(address.getZipCode()) + " " + PrintUtil.noNull(address.getTown()) + ", " + PrintUtil.noNull(address.getStreet()); };


  @Override
  public void internalExecute() {
    savePrints(createTestInvoice(null),
        createTestInvoice_2(null),
        createTestDepositSlip(null),
        createTestWithdrawSlip(null));
  }


  protected void savePrints(AccountingPrint ... prints) {
    User user = database.findAsAdmin(query(User.class, eq("username", "admin"))).get(0);

    for (AccountingPrint print : prints) {
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
    rewriteParty(cashDocument.getBuyer(), database.findOne(query(OrganizationUnit.class, eq("code", buyerCode))));
    rewriteParty(cashDocument.getSeller(), database.findOne(query(OrganizationUnit.class, eq("code", sellerCode))));
  }


  private AccountingPrint createTestWithdrawSlip(Integer id) { // kw
    AccountingPrint print = print(WithdrawSlip.class, id).get();
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

    print.setSellDate(date(2013, 8, 9));
    withdrawSlip.setAuthenticityText("ORYGINAŁ");

    withdrawSlip.setCreator("Maciej Stępień");
    withdrawSlip.setApprovingPerson("Maciej Gołębiowski");
    withdrawSlip.setCashReportReference("Nr 123, poz. 12");
    withdrawSlip.setNumber("123551/125");
    withdrawSlip.setSellPlace("Warszawa");

    BigDecimal sum = PrintUtil.sumSlipPositions(withdrawSlip);
    print.setTotalAmount(sum);
    print.setValue(sum);
    withdrawSlip.setTotalAmountInWords(speakCashAmount(sum));
    return print;
  }


  private AccountingPrint createTestDepositSlip(Integer id) { // kp
    AccountingPrint kp = print(DepositSlip.class, id).get();
    return initializePrint(kp, DepositSlip.class, depositSlip -> {
          rewriteParties(depositSlip, "REMONT", MEDIQ);

          kp.setSellDate(date(2013, 8, 9));
          depositSlip.setAuthenticityText("ORYGINAŁ");

          depositSlip.setCreator("Maciej Stępień");
          depositSlip.setAccountant("Krzysztof Jarzyna");
          depositSlip.setApprovingPerson("Maciej Gołębiowski");
          depositSlip.setCashReportReference("Nr 123, poz. 12");
          depositSlip.setNumber("123551/125");
          depositSlip.setSellPlace("Warszawa");

          BigDecimal sum = PrintUtil.sumSlipPositions(depositSlip);
          kp.setTotalAmount(sum);
          kp.setValue(sum);
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


  private AccountingPrint createTestInvoice_2(Integer id) {
    AccountingPrint print = print(Invoice.class, id).get();

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
    print.setDueDate(date(2013, 8, 19));
    invoice.setNumber("LEG/FV/2013/123456");
    print.setIssuanceDate(date(2013, 8, 9));
    invoice.setPaymentType(TRANSFER);
    print.setSellDate(date(2013, 8, 9));
    invoice.setSellPlace("Warszawa");
    invoice.setAuthenticityText("ORYGINAŁ");
    invoice.setComments("komentarz");
    invoice.setAreCommentsVisible(true);

    TotalCost sum = PrintUtil.sumInvoicePositions(invoice);
    BigDecimal amountPaid = bd("13.55");
    print.setTotalAmount(sum.getSum());
    invoice.setAmountAlreadyPaid(amountPaid);
    print.setValue(amountPaid);
    invoice.setAmountDue(sum.getSum().subtract(amountPaid));
    invoice.setAmountDueInWords(speakCashAmount(sum.getSum().subtract(amountPaid)));

    return print;
  }


  private AccountingPrint createTestInvoice(Integer id) {
    AccountingPrint print = print(Invoice.class, id).get();

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
    print.setDueDate(date(2013, 8, 19));
    invoice.setNumber("LEG/FV/2013/123456");
    print.setIssuanceDate(date(2013, 8, 9));
    invoice.setPaymentType(TRANSFER);
    print.setSellDate(date(2013, 8, 9));
    invoice.setSellPlace("Warszawa");
    invoice.setAuthenticityText("ORYGINAŁ");
    invoice.setComments("komentarz");
    invoice.setAreCommentsVisible(true);

    TotalCost sum = PrintUtil.sumInvoicePositions(invoice);
    BigDecimal amountPaid = bd("13.55");
    print.setTotalAmount(sum.getSum());
    invoice.setAmountAlreadyPaid(amountPaid);
    print.setValue(amountPaid);
    invoice.setAmountDue(sum.getSum().subtract(amountPaid));
    invoice.setAmountDueInWords(speakCashAmount(sum.getSum().subtract(amountPaid)));

    return print;
  }

  @Override
  public IFacadeBuilder getFacadeBuilder() {
    return facadeBuilder;
  }
}

