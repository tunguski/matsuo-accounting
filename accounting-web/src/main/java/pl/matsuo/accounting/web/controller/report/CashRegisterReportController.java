package pl.matsuo.accounting.web.controller.report;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.cashregister.initializer.CashRegisterReportInitializer;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.core.IQueryRequestParams;
import pl.matsuo.core.model.api.Initializer;
import pl.matsuo.core.model.print.initializer.PrintInitializer;
import pl.matsuo.core.web.controller.AbstractController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.*;
import static java.util.Arrays.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.accounting.model.print.CashDocumentUtil.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.StringUtil.*;


/**
 * Created by tunguski on 15.09.13.
 */
@RestController
@RequestMapping("/cashRegisterReports")
public class CashRegisterReportController
    extends AbstractController<CashRegisterReport, CashRegisterReportController.ICashRegisterReportControllerQueryRequestParams> {


  public static interface ICashRegisterReportControllerQueryRequestParams extends IQueryRequestParams {
    String getLast();
  }


  @Override
  protected List<? extends Initializer<CashRegisterReport>> entityInitializers() {
    return asList(new CashRegisterReportInitializer());
  }


  @Override
  @RequestMapping(method = GET)
  public List<CashRegisterReport> list(ICashRegisterReportControllerQueryRequestParams params) {
    List<CashRegisterReport> list = super.list(params);

    // specjalny przypadek - pobieranie ostatnich raportów dla każdej z kas
    if (notEmpty(params.getLast())) {
      // mapa kasa -> ostatni ze znalezionych raportów dla danej kasy
      Map<CashRegister, CashRegisterReport> reports = new HashMap<>();
      for (CashRegisterReport report : list) {
        // jeśli przetwarzany raport został utworzony później niż ostatni wybrany, nadpisuje wpis w mapie
        if (reports.get(report.getCashRegister()) == null
            || reports.get(report.getCashRegister()).getCreatedTime().compareTo(report.getCreatedTime()) < 0) {
          reports.put(report.getCashRegister(), report);
        }
      }

      return new ArrayList<>(reports.values());
    }

    return list;
  }


  @RequestMapping(method = POST, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  public HttpEntity<CashRegisterReport> create(@RequestBody @Valid CashRegisterReport entity,
                                               @Value("#{request.requestURL}") StringBuffer parentUri) {
    CashRegisterReport report = reportForCashRegister(entity.getCashRegister().getId());

    HttpEntity<CashRegisterReport> httpEntity = super.create(report, parentUri);

    return httpEntity;
  }


  @RequestMapping(value = "/reportForCashRegister/{id}", method = GET, consumes = {APPLICATION_OCTET_STREAM_VALUE})
  public CashRegisterReport reportForCashRegister(@PathVariable("id") Integer idCashRegister) {
    CashRegister cashRegister = database.findOne(query(CashRegister.class, eq("id", idCashRegister)));

    CashRegisterReport lastReport = database.findOne(query(CashRegisterReport.class,
        eq("cashRegister.id", idCashRegister)).orderBy("createdTime DESC").limit(1));

    List<AccountingPrint> prints = database.find(query(AccountingPrint.class,
        eq("idCashRegister", idCashRegister), isNull("idCashRegisterReport"))
        .initializer(new PrintInitializer()));

    CashRegisterReport report = new CashRegisterReport();
    report.setCreatedTime(new Date());
    report.setCashRegister(cashRegister);
    report.getPrints().addAll(prints);

    report.setStartingBalance(lastReport != null ? lastReport.getEndingBalance() : ZERO);
    report.setEndingBalance(lastReport != null ? lastReport.getEndingBalance() : ZERO);

    report.setEndingBalance(report.getEndingBalance().add(sumCashRegisterAmount(prints)));

    return report;
  }


  /**
   * Return not reckoned prints summary
   */
  @RequestMapping(value = "/cashRegisterPrintsSummary/{id}", method = GET, consumes = {APPLICATION_OCTET_STREAM_VALUE})
  public BigDecimal cashRegisterPrintsSummary(@PathVariable("id") Integer idCashRegister) {
    return sumCashRegisterAmount(database.find(query(AccountingPrint.class, eq("idCashRegister", idCashRegister),
        isNull("idCashRegisterReport")).initializer(new PrintInitializer())));
  }
}

