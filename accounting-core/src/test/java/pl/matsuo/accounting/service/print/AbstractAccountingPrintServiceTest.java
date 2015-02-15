package pl.matsuo.accounting.service.print;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import pl.matsuo.accounting.test.TestCashRegisterSessionState;
import pl.matsuo.accounting.test.data.MediqCashRegisterTestData;
import pl.matsuo.accounting.test.data.NumerationTestData;
import pl.matsuo.core.AbstractDbTest;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.service.numeration.MonthlyNumerationSchemaStrategy;
import pl.matsuo.core.service.numeration.NumerationServiceImpl;
import pl.matsuo.core.test.data.MediqTestData;

import static pl.matsuo.core.model.query.QueryBuilder.*;

/**
 * Created by marek on 09.02.15.
 */
@ContextConfiguration(classes = { TestCashRegisterSessionState.class, NumerationServiceImpl.class,
    MonthlyNumerationSchemaStrategy.class, MediqCashRegisterTestData.class, NumerationTestData.class })
public abstract class AbstractAccountingPrintServiceTest extends AbstractDbTest {


  OrganizationUnit mediq;


  @Before
  public void setUp() {
    mediq = database.findOne(query(OrganizationUnit.class, eq(OrganizationUnit::getCode, MediqTestData.MEDIQ)));
  }
}
