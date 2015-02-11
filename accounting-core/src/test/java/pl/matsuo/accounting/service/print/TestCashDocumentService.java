package pl.matsuo.accounting.service.print;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.service.session.CashRegisterSessionState;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.query.QueryBuilder;
import pl.matsuo.core.util.DateUtil;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.DateUtil.*;


@ContextConfiguration(classes = { InvoiceService.class })
public class TestCashDocumentService extends AbstractAccountingPrintServiceTest {


  @Autowired
  InvoiceService cashDocumentService;
  @Autowired
  CashRegisterSessionState cashRegisterSessionState;


  @Test
  public void testPrintType() throws Exception {
    assertEquals(Invoice.class, cashDocumentService.printType());
  }


  @Test
  public void testCreate() throws Exception {
    AccountingPrint print = new AccountingPrint();
    print.setIssuanceDate(date(2015, 0, 1));
    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);

    Person person = database.findOne(query(Person.class));
    OrganizationUnit company = database.findOne(query(OrganizationUnit.class));

    invoice.getSeller().setId(company.getId());
    invoice.getBuyer().setId(person.getId());

    print.setIdBucket(company.getIdBucket());
    print.getFields().put("seller.id", "" + company.getIdBucket());

    cashRegisterSessionState.setIdCashRegister(database.findOne(query(CashRegister.class)).getId());

    AccountingPrint accountingPrint = cashDocumentService.create(print);
    assertEquals(Invoice.class, accountingPrint.getPrintClass());
  }


  @Test
  public void testPreCreate() throws Exception {
    AccountingPrint print = new AccountingPrint();
    print.setPrintClass(Invoice.class);
    print.setIssuanceDate(date(2015, 0, 1));
    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);

    cashDocumentService.preCreate(print, invoice);

    assertEquals("FV/2015/1/1", invoice.getNumber());
  }


  @Test
  public void testPrintNumer() throws Exception {
    AccountingPrint print = new AccountingPrint();
    print.setPrintClass(Invoice.class);
    print.setIssuanceDate(date(2015, 0, 1));
    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);

    cashDocumentService.printNumer(print, invoice, false);

    assertEquals("FV/2015/1/2", invoice.getNumber());
  }


  @Test
  public void testNumerationName() throws Exception {
    AccountingPrint print = new AccountingPrint();
    print.setPrintClass(Invoice.class);
    print.setIssuanceDate(date(2015, 0, 1));
    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);

    assertEquals("Invoice", cashDocumentService.numerationName(print, invoice));
  }


  @Test
  public void testUpdate() throws Exception {

  }


  @Test
  public void testFillDocument() throws Exception {
    AccountingPrint print = new AccountingPrint();
    print.setPrintClass(Invoice.class);
    print.setIssuanceDate(date(2015, 0, 1));
    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);

    Person person = database.findOne(query(Person.class));
    OrganizationUnit company = database.findOne(query(OrganizationUnit.class));

    invoice.getSeller().setId(company.getId());
    invoice.getBuyer().setId(person.getId());

    print.setIdBucket(company.getIdBucket());
    print.getFields().put("seller.id", "" + company.getIdBucket());

    cashDocumentService.fillDocument(print, invoice);

    assertNotNull(invoice.getBuyer().getName());
    assertNotNull(invoice.getSeller().getName());
  }


  @Test
  public void testFillDocument1() throws Exception {
    AccountingPrint print = new AccountingPrint();
    print.setPrintClass(Invoice.class);
    print.setIssuanceDate(date(2015, 0, 1));
    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);

    Person person = database.findOne(query(Person.class));
    OrganizationUnit company = database.findOne(query(OrganizationUnit.class));

    invoice.getSeller().setId(company.getId());
    invoice.getBuyer().setId(person.getId());

    print.setIdBucket(company.getIdBucket());
    print.getFields().put("seller.id", "" + company.getIdBucket());

    cashDocumentService.fillDocument(print);

    assertNotNull(invoice.getBuyer().getName());
    assertNotNull(invoice.getSeller().getName());
  }
}

