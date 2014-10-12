package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.Invoice;

import java.util.HashMap;
import java.util.Map;

import static pl.matsuo.accounting.util.PrintUtil.*;


/**
 * Serwis tworzenia modelu dla faktury.
 * @author Marek Romanowski
 * @since Aug 28, 2013
 */
@Service
public class InvoicePrintService extends AbstractAccountingPrintService<Invoice> {


  @Override
  protected void buildAccountingModel(Invoice invoice, Map<String, Object> dataModel) {
    Map<String, Object> total = new HashMap<>();
    Map<String, Object> taxes = new HashMap<>();

    total.put("" + false, sumInvoicePositions(invoice));
    taxes.put("" + false, createTaxRatesList(invoice));

    dataModel.put("taxRateList", taxes);
    dataModel.put("total", total);
  }


  @Override
  public String getFileName(Invoice print) {
    return "invoice_" + print.getNumber();
  }
}

