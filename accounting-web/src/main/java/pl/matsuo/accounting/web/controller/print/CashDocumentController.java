package pl.matsuo.accounting.web.controller.print;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.accounting.model.print.CashDocumentParty;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.exception.RestProcessingException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.accounting.model.print.CashDocumentUtil.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;


@RestController
@RequestMapping("/cashDocuments")
public class CashDocumentController<D extends CashDocument> extends AbstractAccountingPrintController<D> {


  @Override
  @RequestMapping(method = POST, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  public HttpEntity<AccountingPrint> create(@RequestBody AccountingPrint entity,
                                            @Value("#{request.requestURL}") StringBuffer parentUri) {

    D cashDocument = facadeBuilder.createFacade(entity, printType);

    Integer idCashRegister = cashRegisterSessionState.getIdCashRegister();

    if (entity.getIdCashRegisterReport() != null) {
      CashRegisterReport cashRegisterReport =
          database.findOne(query(CashRegisterReport.class, eq("id", entity.getIdCashRegisterReport())));
      idCashRegister = cashRegisterReport.getCashRegister().getId();

      cashRegisterReport.setEndingBalance(
          cashRegisterReport.getEndingBalance().add(cashDocument.getCashRegisterAmount()));
      database.update(cashRegisterReport);
    } else if (idCashRegister == null) {
      throw new RestProcessingException("no_cash_register_set");
    }

    entity.setPrintClass(printType);
    entity = fillDocument(entity);

    entity.setIdCashRegister(idCashRegister);

    CashRegister cashRegister = database.findById(CashRegister.class, idCashRegister);
    cashRegister.setValue(cashRegister.getValue().add(cashDocument.getCashRegisterAmount()));
    database.update(cashRegister);

    preCreate(entity, cashDocument);

    return super.create(entity, parentUri);
  }


  protected void preCreate(AccountingPrint print, D cashDocument) {
  }


  @Override
  @RequestMapping(method = PUT, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void update(@RequestBody @Valid AccountingPrint entity) {
    super.update(fillDocument(entity));
  }


  @Override
  @RequestMapping(value = "/{id}", method = PUT)
  @ResponseStatus(NO_CONTENT)
  public void update(@PathVariable("id") Integer id, @RequestBody AccountingPrint entity) {
    super.update(id, fillDocument(entity));
  }


  protected final AccountingPrint fillDocument(AccountingPrint print) {
    return fillDocument(print, facadeBuilder.createFacade(print, printType));
  }


  protected AccountingPrint fillDocument(AccountingPrint print, D cashDocument) {
    fillParty(cashDocument.getBuyer());
    fillParty(cashDocument.getSeller());
    return print;
  }


  protected void fillParty(CashDocumentParty cashDocumentParty) {
    with(database.findById(AbstractParty.class, cashDocumentParty.getId()), party -> rewriteParty(cashDocumentParty, party));
  }
}

