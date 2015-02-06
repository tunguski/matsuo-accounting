package pl.matsuo.accounting.web.controller.cash;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.test.data.MediqCashRegisterTestData;
import pl.matsuo.accounting.web.controller.report.CashRegisterReportController;
import pl.matsuo.core.service.execution.ExecutionServiceImpl;
import pl.matsuo.core.web.controller.AbstractDbControllerRequestTest;

import java.math.BigDecimal;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.matsuo.accounting.model.print.AccountingPrint.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.NumberUtil.*;
import static pl.matsuo.core.web.controller.ControllerTestUtil.*;


/**
 * Created by tunguski on 18.09.13.
 */
@Transactional
@ContextConfiguration(classes = { CashRegisterReportController.class, ExecutionServiceImpl.class,
                                  MediqCashRegisterTestData.class })
public class TestCashRegisterReportControllerRequest extends AbstractDbControllerRequestTest {


  public AccountingPrint createPrint(BigDecimal sum, Integer idCashRegister) {
    AccountingPrint print = print(Invoice.class, null).get();
    print.setIdCashRegister(idCashRegister);
    print.setIdBucket(1);

    Invoice facade = facadeBuilder.createFacade(print);
    facade.getSeller().setId(1);
    print.setValue(sum);

    database.create(print);

    return print;
  }


  @Test
  public void createCashRegisterReport() throws Exception {
    CashRegister cashRegister = database.findOne(query(CashRegister.class));

    CashRegisterReport cashRegisterReport = new CashRegisterReport();
    cashRegisterReport.setCashRegister(cashRegister);
    cashRegisterReport.getCashRegister().setReckoningParty(null);
    cashRegisterReport.getPrints().addAll(asList(
        createPrint(bd("120"), cashRegister.getId()),
        createPrint(bd("75"), cashRegister.getId()),
        createPrint(bd("200"), cashRegister.getId())));

    ResultActions result = mockMvc.perform(post("/cashRegisterReports/", cashRegisterReport));
    result.andExpect(status().isCreated());
    Integer id = idFromLocation(result);

    result = mockMvc.perform(get("/cashRegisterReports/" + id));
    result.andExpect(status().isOk());

    CashRegisterReport resultReport = objectMapper.readValue(
        result.andReturn().getResponse().getContentAsString(), CashRegisterReport.class);

    assertEquals(3, resultReport.getPrints().size());
  }
}

