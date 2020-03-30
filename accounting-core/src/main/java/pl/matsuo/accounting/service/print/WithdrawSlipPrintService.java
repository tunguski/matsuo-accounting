package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.WithdrawSlip;

/**
 * Serwis tworzenia modelu dla "Kasa wyda".
 *
 * @since Aug 28, 2013
 */
@Service
public class WithdrawSlipPrintService extends AbstractAccountingPrintService<WithdrawSlip> {

  @Override
  public String getFileName(WithdrawSlip print) {
    return "withdrawSlip_" + print.getNumber();
  }
}
