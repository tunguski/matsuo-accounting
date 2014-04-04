package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.CorrectiveInvoice;
import pl.matsuo.core.service.print.AbstractPrintService;

import java.util.Map;

import static pl.matsuo.accounting.util.PrintUtil.*;


@Service
public class CorrectiveInvoiceService extends AbstractPrintService<CorrectiveInvoice> {


  @Override
  protected void buildModel(CorrectiveInvoice print, Map<String, Object> dataModel) {
    dataModel.put("taxRateList", createTaxRatesList(print));
    dataModel.put("total", sumInvoicePositions(print));
    dataModel.put("taxRateListAfterCorrection", createCorrectedTaxRatesList(print));
    dataModel.put("totalAfterCorrection", sumCorrectedInvoicePositions(print));
  }


  @Override
  public String getFileName(CorrectiveInvoice print) {
    return "correctiveInvoice_" + print.getNumber();
  }
}

