package pl.matsuo.accounting.model.print;

public interface CorrectiveInvoicePosition extends InvoicePosition {

  // null == false
  Boolean getIsAfterCorrection();

  void setIsAfterCorrection(Boolean isAfterCorrection);
}
