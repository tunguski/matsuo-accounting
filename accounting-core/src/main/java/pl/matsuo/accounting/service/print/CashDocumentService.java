package pl.matsuo.accounting.service.print;

import static org.springframework.core.GenericTypeResolver.*;
import static pl.matsuo.accounting.model.print.CashDocumentUtil.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;

import org.springframework.beans.factory.annotation.Autowired;
import pl.matsuo.accounting.model.cashregister.CashRegister;
import pl.matsuo.accounting.model.cashregister.CashRegisterReport;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.accounting.service.session.CashRegisterSessionState;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.print.PrintParty;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.numeration.NumerationService;

public abstract class CashDocumentService<D extends CashDocument> implements ICashDocumentService {

  @Autowired protected Database database;
  @Autowired protected FacadeBuilder facadeBuilder;
  @Autowired protected CashRegisterSessionState cashRegisterSessionState;
  @Autowired protected NumerationService numerationService;

  @SuppressWarnings("unchecked")
  protected final Class<D> printType =
      (Class<D>) resolveTypeArgument(getClass(), CashDocumentService.class);

  public Class<D> printType() {
    return printType;
  }

  @Override
  public AccountingPrint create(AccountingPrint entity) {

    D cashDocument = facadeBuilder.createFacade(entity, printType());

    Integer idCashRegister = cashRegisterSessionState.getIdCashRegister();

    if (entity.getIdCashRegisterReport() != null) {
      CashRegisterReport cashRegisterReport =
          database.findOne(
              query(
                  CashRegisterReport.class,
                  eq(CashRegisterReport::getId, entity.getIdCashRegisterReport())));
      idCashRegister = cashRegisterReport.getCashRegister().getId();

      cashRegisterReport.setEndingBalance(
          cashRegisterReport.getEndingBalance().add(entity.getCashRegisterAmount()));
      database.update(cashRegisterReport);
    } else if (idCashRegister == null) {
      throw new RestProcessingException("no_cash_register_set");
    }

    entity.setPrintClass(printType());
    entity = fillDocument(entity);

    entity.setIdCashRegister(idCashRegister);

    CashRegister cashRegister = database.findById(CashRegister.class, idCashRegister);
    cashRegister.setValue(cashRegister.getValue().add(entity.getCashRegisterAmount()));
    database.update(cashRegister);

    preCreate(entity, cashDocument);

    return entity;
  }

  protected void preCreate(AccountingPrint print, D cashDocument) {
    printNumer(print, cashDocument, false);
  }

  protected void printNumer(AccountingPrint print, D cashDocument, boolean preview) {
    cashDocument.setNumber(
        numerationService.getNumber(
            numerationName(print, cashDocument), print.getIssuanceDate(), preview));
  }

  protected String numerationName(AccountingPrint print, D cashDocument) {
    return printType.getSimpleName();
  }

  @Override
  public AccountingPrint update(AccountingPrint entity) {
    return fillDocument(entity);
  }

  protected final AccountingPrint fillDocument(AccountingPrint print) {
    return fillDocument(print, facadeBuilder.createFacade(print, printType()));
  }

  protected AccountingPrint fillDocument(AccountingPrint print, D cashDocument) {
    fillParty(cashDocument.getBuyer());
    fillParty(cashDocument.getSeller());
    return print;
  }

  protected void fillParty(PrintParty cashDocumentParty) {
    with(
        database.findById(AbstractParty.class, cashDocumentParty.getId()),
        party -> rewriteParty(cashDocumentParty, party));
  }
}
