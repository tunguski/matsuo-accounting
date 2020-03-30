package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.DepositSlip;

/**
 * Serwis tworzenia modelu dla "Kasa przyjmie".
 *
 * @since Aug 28, 2013
 */
@Service
public class DepositSlipPrintService extends AbstractAccountingPrintService<DepositSlip> {

  @Override
  public String getFileName(DepositSlip print) {
    return "depositSlip";
  }
}
