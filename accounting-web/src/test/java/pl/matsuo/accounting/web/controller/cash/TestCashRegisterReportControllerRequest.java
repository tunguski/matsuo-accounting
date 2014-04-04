package pl.matsuo.accounting.web.controller.cash;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import pl.matsuo.clinic.model.cash.CashRegister;
import pl.matsuo.clinic.model.cash.CashRegisterReport;
import pl.matsuo.clinic.model.print.cash.Invoice;
import pl.matsuo.clinic.test.data.CashRegisterTestData;
import pl.matsuo.clinic.web.controller.AbstractControllerRequestTest;
import pl.matsuo.core.service.execution.ExecutionServiceImpl;

import java.math.BigDecimal;

import static java.util.Arrays.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.matsuo.clinic.model.print.Print.*;
import static pl.matsuo.clinic.web.controller.ControllerTestUtil.*;
import static pl.matsuo.core.util.NumberUtil.*;


/**
 * Created by tunguski on 18.09.13.
 */
@Transactional
@ContextConfiguration(classes = { CashRegisterReportController.class, ExecutionServiceImpl.class,
                                  CashRegisterTestData.class })
public class TestCashRegisterReportControllerRequest extends AbstractControllerRequestTest {


  public Print createPrint(BigDecimal sum, Integer idCashRegister) {
    Print print = print(Invoice.class, null).get();
    print.setIdCashRegister(idCashRegister);

    Invoice facade = facadeBuilder.createFacade(print);
    facade.setCashRegisterAmount(sum);

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

