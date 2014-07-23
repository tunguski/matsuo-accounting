package pl.matsuo.accounting.model.print;

import javax.persistence.Enumerated;
import java.math.BigDecimal;

import static javax.persistence.EnumType.*;


/**
 * Elementy wspolne dla faktury i faktury po korekcie
 * @since Oct 6, 2013
 */
public interface InvoiceCommon<E extends CashDocumentPosition> extends CashDocument<E> {

  @Enumerated(STRING)
  PaymentType getPaymentType();
  void setPaymentType(PaymentType paymentType);

  String getBankAccountNumber();
  void setBankAccountNumber(String bankAccountNumber);

  BigDecimal getAmountAlreadyPaid();
  void setAmountAlreadyPaid(BigDecimal amount);

  BigDecimal getAmountDue();
  void setAmountDue(BigDecimal amountDue);

  String getAmountDueInWords();
  void setAmountDueInWords(String text);

  String getComments();
  void setComments(String comments);

  Boolean getAreCommentsVisible();
  void setAreCommentsVisible(Boolean value);

  Boolean getIsReceipt();
  void setIsReceipt(Boolean isReceipt);
}
