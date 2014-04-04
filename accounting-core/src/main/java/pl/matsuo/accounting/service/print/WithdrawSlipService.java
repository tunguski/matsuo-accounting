package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.WithdrawSlip;
import pl.matsuo.core.service.print.AbstractPrintService;

import java.util.Map;


/**
 * Serwis tworzenia modelu dla "Kasa wyda".
 * @author Marek Romanowski
 * @since Aug 28, 2013
 */
@Service
public class WithdrawSlipService extends AbstractPrintService<WithdrawSlip> {


  @Override
  protected void buildModel(WithdrawSlip print, Map<String, Object> dataModel) {
  }


  @Override
  public String getFileName(WithdrawSlip print) {
    return "withdrawSlip_" + print.getNumber();
  }
}

