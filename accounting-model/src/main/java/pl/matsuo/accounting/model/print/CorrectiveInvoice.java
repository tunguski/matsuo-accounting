/**
 *
 */
package pl.matsuo.accounting.model.print;

import java.math.BigDecimal;


public interface CorrectiveInvoice extends InvoiceCommon<CorrectiveInvoicePosition> {

  BigDecimal getAmountDueAfterCorrection();
  void setAmountDueAfterCorrection(BigDecimal amountDue);

  String getAmountDueAfterCorrectionInWords();
  void setAmountDueAfterCorrectionInWords(String text);

  String getReasonOfCorrection();
  void setReasonOfCorrection(String reason);
}

