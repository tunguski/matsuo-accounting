package pl.matsuo.accounting.model.print;

import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.PrintParty;

public interface CashDocument<E extends CashDocumentPosition> extends IPrintFacade<E> {

  PrintParty getSeller();

  PrintParty getBuyer();

  String getSellPlace();

  void setSellPlace(String sellPlace);

  String getAuthenticityText();

  void setAuthenticityText(String authenticityText);

  String getNumber();

  void setNumber(String number);
}
