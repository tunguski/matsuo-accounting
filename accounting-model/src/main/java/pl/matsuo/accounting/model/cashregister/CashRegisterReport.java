package pl.matsuo.accounting.model.cashregister;

import org.hibernate.validator.constraints.NotEmpty;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.core.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by tunguski on 16.09.13.
 */
@Entity
public class CashRegisterReport extends AbstractEntity {


  protected BigDecimal startingBalance;
  protected BigDecimal endingBalance;
  @NotNull
  @OneToOne
  protected CashRegister cashRegister;
  @NotEmpty
  @OneToMany
  @JoinColumn(name = "idCashRegisterReport")
  public Set<AccountingPrint> prints = new HashSet<>();


  public BigDecimal getStartingBalance() {
    return startingBalance;
  }
  public void setStartingBalance(BigDecimal startingBalance) {
    this.startingBalance = startingBalance;
  }
  public BigDecimal getEndingBalance() {
    return endingBalance;
  }
  public void setEndingBalance(BigDecimal endingBalance) {
    this.endingBalance = endingBalance;
  }
  public CashRegister getCashRegister() {
    return cashRegister;
  }
  public void setCashRegister(CashRegister cashRegister) {
    this.cashRegister = cashRegister;
  }
  public Set<AccountingPrint> getPrints() {
    return prints;
  }
  public void setPrints(Set<AccountingPrint> prints) {
    this.prints = prints;
  }
}

