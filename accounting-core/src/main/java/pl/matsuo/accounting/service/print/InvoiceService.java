package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.core.service.print.AbstractPrintService;

import java.util.Map;

import static pl.matsuo.accounting.util.PrintUtil.*;


/**
 * Serwis tworzenia modelu dla faktury.
 * @author Marek Romanowski
 * @since Aug 28, 2013
 */
@Service
public class InvoiceService extends AbstractPrintService<Invoice> {


  @Override
  protected void buildModel(Invoice invoice, Map<String, Object> dataModel) {
    // tworzy liste klas podatkowych
    dataModel.put("taxRateList", createTaxRatesList(invoice));
    // tworzy wiersz RAZEM
    dataModel.put("total", sumInvoicePositions(invoice));
  }


  @Override
  public String getFileName(Invoice print) {
    return "invoice_" + print.getNumber();
  }
}

