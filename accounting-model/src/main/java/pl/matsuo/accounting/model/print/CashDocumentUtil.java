package pl.matsuo.accounting.model.print;

import static java.math.BigDecimal.*;
import static pl.matsuo.core.model.organization.address.AddressUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;

import java.math.BigDecimal;
import java.util.List;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.print.PrintParty;

public class CashDocumentUtil {

  public static void rewriteParty(PrintParty cashDocumentParty, AbstractParty party) {
    cashDocumentParty.setId(party.getId());
    cashDocumentParty.setName(party.getName());
    cashDocumentParty.setAddress(htmlAddress(party.getAddress()));
    cashDocumentParty.setNip(party.getNip());

    if (party instanceof Person) {
      cashDocumentParty.setPesel(((Person) party).getPesel());
    }
  }

  public static <E> BigDecimal sumCashRegisterAmount(List<AccountingPrint> prints) {
    return prints.stream().map(print -> print.getCashRegisterAmount()).reduce(ZERO, sumBigDecimal);
  }
}
