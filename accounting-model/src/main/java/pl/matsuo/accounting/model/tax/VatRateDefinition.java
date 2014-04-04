package pl.matsuo.accounting.model.tax;

import pl.matsuo.core.model.AbstractEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;


/**
 * Definicje dostępnych stawek VAT.
 * @author Marek Romanowski
 * @since Jul 16, 2013
 */
@Entity
public class VatRateDefinition extends AbstractEntity {


  /**
   * Nazwa definicji stawki.
   */
  private String name;
  /**
   * Wysokość stawki podatku VAT.
   */
  private BigDecimal rate;
  /**
   * Czy stawka nadal obowiązuje, czy też jest historyczna.
   */
  private boolean actual = true;


  // getters
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public BigDecimal getRate() {
    return rate;
  }
  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }
  public boolean isActual() {
    return actual;
  }
  public void setActual(boolean actual) {
    this.actual = actual;
  }
}

