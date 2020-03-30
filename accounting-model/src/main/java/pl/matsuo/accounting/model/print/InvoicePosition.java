package pl.matsuo.accounting.model.print;

import java.math.BigDecimal;

public interface InvoicePosition extends CashDocumentPosition {

  String getPKWiU();

  void setPKWiU(String pkwiu);

  String getJM();

  void setJM(String jm);

  BigDecimal getCount();

  void setCount(BigDecimal count);

  String getTaxRate();

  void setTaxRate(String taxRate);
}
