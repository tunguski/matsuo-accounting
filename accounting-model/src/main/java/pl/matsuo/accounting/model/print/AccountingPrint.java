package pl.matsuo.accounting.model.print;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.core.model.kv.KeyValueEntity;
import pl.matsuo.core.model.print.IPrintFacade;
import pl.matsuo.core.model.print.KeyValuePrint;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.validation.EntityReference;
import pl.matsuo.core.service.facade.IFacadeAware;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static javax.persistence.CascadeType.*;


@Entity
public class AccountingPrint extends KeyValuePrint {


  @EntityReference(CashRegister.class)
  private Integer idCashRegister;
  @EntityReference(CashRegisterReport.class)
  private Integer idCashRegisterReport;


  public static Supplier<? extends AccountingPrint> print(Class<? extends IPrintFacade> clazz, Integer id) {
    return () -> { return (AccountingPrint) printInitializer(clazz, id).apply(new AccountingPrint()); };
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
}

