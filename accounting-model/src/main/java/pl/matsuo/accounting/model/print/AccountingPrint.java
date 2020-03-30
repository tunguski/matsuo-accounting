package pl.matsuo.accounting.model.print;

import static org.springframework.util.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Supplier;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.validation.EntityReference;

@Entity
@Getter
@Setter
public class AccountingPrint extends KeyValuePrint {

  @EntityReference(CashRegister.class)
  private Integer idCashRegister;

  @EntityReference(CashRegisterReport.class)
  private Integer idCashRegisterReport;

  private AccountingPrintStatus status = AccountingPrintStatus.OPEN;
  private Date issuanceDate;
  private Date dueDate;
  private Date sellDate;
  /** Sumaryczna wartość wszystkich pozycji. */
  private BigDecimal value;
  /** Wartość rozliczenia w raporcie kasowym (przyjęto). */
  private BigDecimal cashRegisterAmount;
  /** Pełna wartość dokumentu */
  private BigDecimal totalAmount;

  public static Supplier<? extends AccountingPrint> print(
      Class<? extends IPrintFacade> clazz, Integer id) {
    return () -> {
      return (AccountingPrint) printInitializer(clazz, id).apply(new AccountingPrint());
    };
  }

  public boolean isCost() {
    // print has to be referenced to some party to define is it cost
    notNull(getIdBucket());
    // FIXME: should reference property not field by string
    return !getFields().get("seller.id").equals("" + getIdBucket());
  }

  public BigDecimal getCashRegisterAmount() {
    return isCost() ? getValue().negate() : getValue();
  }

  public void setValue(BigDecimal value) {
    this.value = value;
    cashRegisterAmount = isCost() ? value.negate() : value;
  }
}
