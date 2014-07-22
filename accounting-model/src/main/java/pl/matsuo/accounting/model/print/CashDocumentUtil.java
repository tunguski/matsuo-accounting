package pl.matsuo.accounting.model.print;

import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.math.BigDecimal.*;
import static pl.matsuo.core.model.organization.address.AddressUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;

/**
 * Created by marek on 01.04.14.
 */
public class CashDocumentUtil {


  public static void rewriteParty(CashDocumentParty cashDocumentParty, AbstractParty party) {
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

