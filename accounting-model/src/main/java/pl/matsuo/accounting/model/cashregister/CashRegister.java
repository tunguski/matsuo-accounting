package pl.matsuo.accounting.model.cashregister;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.AbstractParty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;


/**
 * Model danych kasy (np.: fiskalnej).
 * Created by tunguski on 16.09.13.
 */
@Entity
public class CashRegister extends AbstractEntity {

  protected String code;
  protected BigDecimal value;
  @ManyToOne
  protected AbstractParty reckoningParty;


  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public BigDecimal getValue() {
    return value;
  }
  public void setValue(BigDecimal value) {
    this.value = value;
  }
  public AbstractParty getReckoningParty() {
    return reckoningParty;
  }
  public void setReckoningParty(AbstractParty reckoningParty) {
    this.reckoningParty = reckoningParty;
  }
}

