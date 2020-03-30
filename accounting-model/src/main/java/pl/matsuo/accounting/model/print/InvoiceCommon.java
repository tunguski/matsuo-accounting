package pl.matsuo.accounting.model.print;

import static javax.persistence.EnumType.*;

import java.math.BigDecimal;
import javax.persistence.Enumerated;

/**
 * Elementy wspolne dla faktury i faktury po korekcie
 *
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
