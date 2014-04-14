package pl.matsuo.accounting.web.controller.print;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.accounting.model.print.CashDocumentParty;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.web.controller.exception.RestProcessingException;

import javax.validation.Valid;

import static java.util.Arrays.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.accounting.model.print.CashDocumentUtil.*;
import static pl.matsuo.core.model.organization.address.AddressUtil.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;


@Controller
@RequestMapping("/cashDocuments")
public class CashDocumentController<D extends CashDocument> extends AbstractAccountingPrintController<D> {


  @Override
  @RequestMapping(method = POST, consumes = { APPLICATION_JSON_VALUE })
  @ResponseStatus(CREATED)
  public HttpEntity<AccountingPrint> create(@RequestBody AccountingPrint entity,
                                  @Value("#{request.requestURL}") StringBuffer parentUri) {

    D cashDocument = facadeBuilder.createFacade(entity, printType);

    CashRegister cashRegister = cashRegisterSessionState.getCashRegister();

    if (entity.getIdCashRegisterReport() != null) {
      CashRegisterReport cashRegisterReport =
          database.findOne(query(CashRegisterReport.class, eq("id", entity.getIdCashRegisterReport())));
      cashRegister = cashRegisterReport.getCashRegister();

      cashRegisterReport.setEndingBalance(
          cashRegisterReport.getEndingBalance().add(cashDocument.getCashRegisterAmount()));
      database.update(cashRegisterReport);
    } else if (cashRegister == null) {
      throw new RestProcessingException("no_cash_register_set");
    }

    entity.setPrintClass(printType);
    entity = fillDocument(entity);

    entity.setIdCashRegister(cashRegister.getId());

    cashRegister.setValue(cashRegister.getValue().add(cashDocument.getCashRegisterAmount()));
    database.update(cashRegister);

    preCreate(entity, cashDocument);

    return super.create(entity, parentUri);
  }


  protected void preCreate(AccountingPrint print, D cashDocument) {
  }


  @Override
  @RequestMapping(method = PUT, consumes = { APPLICATION_JSON_VALUE })
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

