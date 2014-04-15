package pl.matsuo.accounting.model.print;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static pl.matsuo.core.util.NumberUtil.*;

/**
 * Całkowity koszt Klasa zawierająca informację o koszcie netto, podatku i koszcie brutto
 *
 * @author Mateusz Gałażyn <mateusz.galazyn@gmail.com>
 * @since 2013-09-08
 */
public class TotalCost {
  /**
   * Wartość
   */
  private BigDecimal value = bd("0");
  /**
   * Podatek
   */
  private BigDecimal tax = bd("0");

  public BigDecimal getValue() {
    return value.setScale(2, ROUND_HALF_DOWN);
  }
  public void addToValue(BigDecimal value) {
    this.value = this.value.add(value);
  }

  public BigDecimal getTax() {
    return tax.setScale(2, ROUND_HALF_DOWN);
  }
  public void addToTax(BigDecimal tax) {
    this.tax = this.tax.add(tax);
  }

  public BigDecimal getSum() {
    return this.value.add(this.tax).setScale(2, ROUND_HALF_DOWN);
  }
}
