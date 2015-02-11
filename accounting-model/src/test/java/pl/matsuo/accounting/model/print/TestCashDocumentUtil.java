package pl.matsuo.accounting.model.print;

import org.junit.Test;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.print.PrintParty;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static pl.matsuo.accounting.model.print.CashDocumentUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;


public class TestCashDocumentUtil {


  @Test
  public void testRewriteParty() throws Exception {
    PrintParty cashDocumentParty = mock(PrintParty.class);
    AbstractParty party = new Person();
    rewriteParty(cashDocumentParty, party);
  }


  @Test
  public void testSumCashRegisterAmount() throws Exception {
    List<AccountingPrint> prints = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      AccountingPrint print = new AccountingPrint();
      print.setIdBucket(13);
      print.getFields().put("seller.id", "" + ((i % 2) == 0 ? i + 1 : 13));

      print.setValue(bd(i));
      prints.add(print);
    }

    assertEquals(bd(5), sumCashRegisterAmount(prints));
  }
}

