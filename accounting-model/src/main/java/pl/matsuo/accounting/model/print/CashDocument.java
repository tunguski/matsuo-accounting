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

  /**
   * Data wystawienia
   */
  Date getIssuanceDate();
  void setIssuanceDate(Date issuanceDate);

  /**
   * Termin płatności
   */
  Date getDueDate();
  void setDueDate(Date issuanceDate);

  String getSellPlace();
  void setSellPlace(String sellPlace);

  /**
   * Data sprzedaży
   */
  Date getSellDate();
  void setSellDate(Date sellDate);

  /**
   * Wartość rozliczenia w raporcie kasowym (przyjęto)
   */
  BigDecimal getCashRegisterAmount();
  void setCashRegisterAmount(BigDecimal totalAmount);

  /**
   * Pełna wartość dokumentu
   */
  BigDecimal getTotalAmount();
  void setTotalAmount(BigDecimal totalAmount);

  String getAuthenticityText();
  void setAuthenticityText(String authenticityText);
}

