package pl.matsuo.accounting.service.report.cash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.cashregister.initializer.CashRegisterReportInitializer;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.report.AbstractReportService;
import pl.matsuo.core.service.report.DataModelBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.FreemarkerUtils.*;


/**
 * Serwis tworzenia raportu kasowego.
 *
 * @author Mateusz Orlik
 * @since 15/08/2013
 */
@Service
public class CashRegisterReportService extends AbstractReportService<ICashRegisterReportParams> {


  @Autowired
  protected FacadeBuilder facadeBuilder;


  /**
   */
  @Override
  protected void injectModel(DataModelBuilder dataModel, ICashRegisterReportParams params) {
    if (params != null) {
      CashRegisterReport cashRegisterReport = database.findById(CashRegisterReport.class,
                                                                   params.getIdReport(),
                                                                   new CashRegisterReportInitializer());

      List<CashRegisterReport> cashRegisterReports = database.find(query(CashRegisterReport.class,
          eq("cast(createdTime as date)", date(cashRegisterReport.getCreatedTime(), 0, 0))).orderBy("createdTime"));

      dataModel.put("cashRegisterReport", cashRegisterReport);

      Map<String, BigDecimal> printTypeSummary = new HashMap<>();
      Map<Integer, CashDocument> printFacades = new HashMap<>();

      for (AccountingPrint print : cashRegisterReport.getPrints()) {
        CashDocument cashDocument = facadeBuilder.createFacade(print);
        printFacades.put(print.getId(), cashDocument);


        String printType = print.getPrintClass().getSimpleName();
        if (printTypeSummary.get(printType) == null) {
          printTypeSummary.put(printType, ZERO);
        }

        printTypeSummary.put(printType, printTypeSummary.get(printType).add(cashDocument.getCashRegisterAmount()));
      }

      CashDocument documentInfo = facadeBuilder.createFacade(new HashMap<>(), CashDocument.class);
      dataModel.put("print", documentInfo);

      dataModel.put("prints", mapWrapper(printFacades));

      for (CashRegisterReport registerReport : cashRegisterReports) {
        if (cashRegisterReport.getId().equals(registerReport.getId())) {
          dataModel.put("reportNumber", cashRegisterReports.indexOf(registerReport) + 1);
          break;
        }
      }

      Map<String, Object> summary = new HashMap<>();
      dataModel.put("summary", summary);

      summary.put("resume", cashRegisterReport.prints.size());
      summary.put("printTypeSummary", printTypeSummary);
    }
  }
}

