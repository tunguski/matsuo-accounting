package pl.matsuo.accounting.model.print;

import java.math.BigDecimal;
import pl.matsuo.core.model.print.IPrintElementFacade;

public interface CashDocumentPosition extends IPrintElementFacade {

  String getServiceName();

  void setServiceName(String serviceName);

  BigDecimal getPrice();

  void setPrice(BigDecimal price);

  BigDecimal getAccountNumber();

  void setAccountNumber(BigDecimal accountNumber);
}
