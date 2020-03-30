package pl.matsuo.accounting.model.cashregister;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.core.model.AbstractEntity;

@Entity
@Getter
@Setter
public class CashRegisterReport extends AbstractEntity {

  protected BigDecimal startingBalance;
  protected BigDecimal endingBalance;
  @NotNull @OneToOne protected CashRegister cashRegister;

  @NotEmpty
  @OneToMany
  @JoinColumn(name = "idCashRegisterReport")
  public Set<AccountingPrint> prints = new HashSet<>();
}
