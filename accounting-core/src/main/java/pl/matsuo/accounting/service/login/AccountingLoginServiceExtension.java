package pl.matsuo.accounting.service.login;

import static java.math.BigDecimal.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.login.ILoginServiceExtension;
import pl.matsuo.core.service.session.SessionState;

@Service
public class AccountingLoginServiceExtension implements ILoginServiceExtension {

  @Autowired SessionState sessionState;
  @Autowired Database database;

  @Override
  public void createAccount(OrganizationUnit organizationUnit, User user) {
    CashRegister cashRegister = new CashRegister();
    cashRegister.setCode("CR-1");
    cashRegister.setReckoningParty(organizationUnit);

    database.create(cashRegister);
  }
}
