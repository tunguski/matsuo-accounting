package pl.matsuo.accounting.model.print;


import pl.matsuo.core.model.print.IPrintElementFacade;

import java.math.BigDecimal;


/**
 * tunguski
 */
public interface CashDocumentPosition extends IPrintElementFacade {
  
  String getServiceName();
  void setServiceName(String serviceName);
  
  BigDecimal getPrice();
  void setPrice(BigDecimal price);
  
  BigDecimal getAccountNumber();
  void setAccountNumber(BigDecimal accountNumber);
}
