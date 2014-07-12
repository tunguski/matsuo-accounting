package pl.matsuo.accounting.service.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.core.conf.DbConfig;
import pl.matsuo.core.conf.TestDataExecutionConfig;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.db.EntityInterceptorService;
import pl.matsuo.core.service.db.interceptor.IdBucketInterceptor;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.test.data.TestSessionState;
import pl.matsuo.core.test.data.UserTestData;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DbConfig.class, TestDataExecutionConfig.class,
                                  AccountingLoginServiceExtension.class, TestSessionState.class })
public class TestAccountingLoginServiceExtension {

  @Autowired
  AccountingLoginServiceExtension extension;
  @Autowired
  Database database;

  @Test
  public void testCreateAccount() throws Exception {
    OrganizationUnit organizationUnit = new OrganizationUnit();
    organizationUnit.setFullName("TEST");
    database.create(organizationUnit);

    extension.createAccount(organizationUnit, null);

    List<CashRegister> cashRegisters = database.findAll(CashRegister.class);
    assertEquals(1, cashRegisters.size());
    assertEquals(organizationUnit.getId(), cashRegisters.get(0).getReckoningParty().getId());
  }
}