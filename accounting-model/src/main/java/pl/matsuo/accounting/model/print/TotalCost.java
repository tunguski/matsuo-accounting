package pl.matsuo.accounting.model.print;

import static java.math.BigDecimal.*;
import static pl.matsuo.core.util.NumberUtil.*;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/** Całkowity koszt Klasa zawierająca informację o koszcie netto, podatku i koszcie brutto */
@Getter
@Setter
public class TotalCost {

  /** Wartość */
  private BigDecimal value = bd("0");
  /** Podatek */
  private BigDecimal tax = bd("0");

  public void addToValue(BigDecimal value) {
    this.value = this.value.add(value);
  }

  public void addToTax(BigDecimal tax) {
    this.tax = this.tax.add(tax);
  }

  public BigDecimal getSum() {
    return this.value.add(this.tax);
  }
}
