package pl.matsuo.accounting.web.controller.print;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.CashDocument;
import pl.matsuo.accounting.service.print.ICashDocumentService;
import pl.matsuo.core.exception.RestProcessingException;
import pl.matsuo.core.web.controller.print.AbstractPrintController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
@RequestMapping("/cashDocuments")
public class CashDocumentController extends AbstractPrintController<CashDocument, AccountingPrint> {


  @Autowired
  ICashDocumentService[] cashDocumentServices;


  protected ICashDocumentService findCashDocumentService(String printType) {
    for (ICashDocumentService cashDocumentService : cashDocumentServices) {
      if (cashDocumentService.printType().getSimpleName().toLowerCase().equals(printType.toLowerCase())) {
        return cashDocumentService;
      }
    }

    throw new RestProcessingException("Unable to process print type: " + printType);
  }


  @RequestMapping(value = "/{printType}", method = POST, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  public HttpEntity<AccountingPrint> create(@PathVariable("printType") String printType,
                                            @RequestBody AccountingPrint entity,
                                            @Value("#{request.requestURL}") StringBuffer parentUri) {
    findCashDocumentService(printType).create(entity);
    return super.create(entity, parentUri);
  }

  @Override
  @RequestMapping(method = POST, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  public HttpEntity<AccountingPrint> create(@RequestBody AccountingPrint entity,
                                            @Value("#{request.requestURL}") StringBuffer parentUri) {
    throw new RestProcessingException("This method is blocked for CashDocumentController");
  }


    @Override
  @RequestMapping(method = PUT, consumes = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void update(@RequestBody @Valid AccountingPrint entity) {
    super.update(findCashDocumentService(entity.getPrintClass().getSimpleName()).update(entity));
  }


  @Override
  @RequestMapping(value = "/{id}", method = PUT)
  @ResponseStatus(NO_CONTENT)
  public void update(@PathVariable("id") Integer id, @RequestBody AccountingPrint entity) {
    super.update(id, findCashDocumentService(entity.getPrintClass().getSimpleName()).update(entity));
  }
}

