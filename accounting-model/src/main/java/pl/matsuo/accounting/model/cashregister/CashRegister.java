package pl.matsuo.accounting.model.cashregister;

import static java.math.BigDecimal.*;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.AbstractParty;

/** Model danych kasy (np.: fiskalnej). */
@Entity
@Getter
@Setter
public class CashRegister extends AbstractEntity {

  protected String code;
  protected BigDecimal value = ZERO;
  @ManyToOne protected AbstractParty reckoningParty;
}
