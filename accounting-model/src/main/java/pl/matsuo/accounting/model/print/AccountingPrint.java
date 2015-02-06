package pl.matsuo.accounting.model.print;

import org.springframework.util.Assert;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.validation.EntityReference;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Supplier;

import static org.springframework.util.Assert.*;


@Entity
public class AccountingPrint extends KeyValuePrint {


  @EntityReference(CashRegister.class)
  private Integer idCashRegister;
  @EntityReference(CashRegisterReport.class)
  private Integer idCashRegisterReport;
  private AccountingPrintStatus status = AccountingPrintStatus.OPEN;
  private Date issuanceDate;
  private Date dueDate;
  private Date sellDate;
  /**
   * Sumaryczna wartość wszystkich pozycji.
   */
  private BigDecimal value;
  /**
   * Wartość rozliczenia w raporcie kasowym (przyjęto).
   */
  private BigDecimal cashRegisterAmount;
  /**
   * Pełna wartość dokumentu
   */
  private BigDecimal totalAmount;

  public static Supplier<? extends AccountingPrint> print(Class<? extends IPrintFacade> clazz, Integer id) {
    return () -> { return (AccountingPrint) printInitializer(clazz, id).apply(new AccountingPrint()); };
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


  public Integer getIdCashRegister() {
    return idCashRegister;
  }
  public void setIdCashRegister(Integer idCashRegister) {
    this.idCashRegister = idCashRegister;
  }
  public Integer getIdCashRegisterReport() {
    return idCashRegisterReport;
  }
  public void setIdCashRegisterReport(Integer idCashRegisterReport) {
    this.idCashRegisterReport = idCashRegisterReport;
  }
  public AccountingPrintStatus getStatus() {
    return status;
  }
  public void setStatus(AccountingPrintStatus status) {
    this.status = status;
  }
  public Date getIssuanceDate() {
    return issuanceDate;
  }
  public void setIssuanceDate(Date issuanceDate) {
    this.issuanceDate = issuanceDate;
  }
  public Date getDueDate() {
    return dueDate;
  }
  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }
  public Date getSellDate() {
    return sellDate;
  }
  public void setSellDate(Date sellDate) {
    this.sellDate = sellDate;
  }
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }
  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }
  public BigDecimal getValue() {
    return value;
  }
}

