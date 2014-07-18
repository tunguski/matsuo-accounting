package pl.matsuo.accounting.service.print;

import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CashDocument;

/**
 * Created by marek on 17.07.14.
 */
public interface ICashDocumentService<D extends CashDocument> {


  AccountingPrint create(AccountingPrint entity);
  AccountingPrint update(AccountingPrint entity);

  Class<D> printType();
}
