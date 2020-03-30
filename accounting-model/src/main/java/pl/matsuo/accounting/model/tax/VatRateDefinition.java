package pl.matsuo.accounting.model.tax;

import java.math.BigDecimal;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;

/**
 * Definicje dostępnych stawek VAT.
 *
 * @since Jul 16, 2013
 */
@Entity
@Getter
@Setter
public class VatRateDefinition extends AbstractEntity {

  /** Nazwa definicji stawki. */
  private String name;
  /** Wysokość stawki podatku VAT. */
  private BigDecimal rate;
  /** Czy stawka nadal obowiązuje, czy też jest historyczna. */
  private boolean actual = true;
}
