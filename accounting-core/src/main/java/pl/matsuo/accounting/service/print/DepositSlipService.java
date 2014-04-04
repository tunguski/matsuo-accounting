package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.DepositSlip;
import pl.matsuo.core.service.print.AbstractPrintService;

import java.util.Map;


/**
 * Serwis tworzenia modelu dla "Kasa przyjmie".
 * @author Marek Romanowski
 * @since Aug 28, 2013
 */
@Service
public class DepositSlipService extends AbstractPrintService<DepositSlip> {


  @Override
  protected void buildModel(DepositSlip print, Map<String, Object> dataModel) {

  }


  @Override
  public String getFileName(DepositSlip print) {
    return "depositSlip";
  }
}

