package pl.matsuo.accounting.model.print;

import pl.matsuo.core.model.print.IPrintFacade;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by tunguski on 17.09.13.
 */
public interface CashDocument<E extends CashDocumentPosition> extends IPrintFacade<E> {


  CashDocumentParty getSeller();
  CashDocumentParty getBuyer();


  String getSellPlace();
  void setSellPlace(String sellPlace);

  String getAuthenticityText();
  void setAuthenticityText(String authenticityText);

  String getNumber();
  void setNumber(String number);
}

