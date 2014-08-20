package pl.matsuo.accounting.service.print;

import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.core.service.print.AbstractPrintService;

import java.util.Map;


/**
 * Created by marek on 20.08.14.
 */
public abstract class AbstractAccountingPrintService<E extends CashDocument> extends AbstractPrintService<E> {


  @Override
  protected final void buildModel(E print, Map<String, Object> dataModel) {
    buildAccountingModel(print, dataModel);

    dataModel.put("company", print.getSeller());
  }


  protected void buildAccountingModel(E print, Map<String, Object> dataModel) {

  }
}

