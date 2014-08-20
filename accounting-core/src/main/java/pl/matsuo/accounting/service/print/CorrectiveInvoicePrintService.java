package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.CorrectiveInvoice;
import pl.matsuo.core.service.print.AbstractPrintService;

import java.util.HashMap;
import java.util.Map;

import static pl.matsuo.accounting.util.PrintUtil.*;


@Service
public class CorrectiveInvoicePrintService extends AbstractAccountingPrintService<CorrectiveInvoice> {


  @Override
  protected void buildAccountingModel(CorrectiveInvoice print, Map<String, Object> dataModel) {
    Map<String, Object> total = new HashMap<>();
    Map<String, Object> taxes = new HashMap<>();

    total.put("" + false, sumInvoicePositions(print));
    total.put("" + true, sumCorrectedInvoicePositions(print));

    taxes.put("" + false, createTaxRatesList(print));
    taxes.put("" + true, createCorrectedTaxRatesList(print));

    dataModel.put("taxRateList", taxes);
    dataModel.put("total", total);
  }


  @Override
  public String getFileName(CorrectiveInvoice print) {
    return "correctiveInvoice_" + print.getNumber();
  }
}

