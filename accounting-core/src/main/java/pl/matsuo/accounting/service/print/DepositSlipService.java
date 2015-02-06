package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.DepositSlip;

import java.math.BigDecimal;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@Service
public class DepositSlipService extends CashDocumentService<DepositSlip> {


  protected AccountingPrint fillDocument(AccountingPrint print, DepositSlip depositSlip) {
    super.fillDocument(print, depositSlip);

    BigDecimal sum = sumSlipPositions(depositSlip);
    print.setTotalAmount(sum);
    print.setValue(sum);
    depositSlip.setTotalAmountInWords(speakCashAmount(sum));
    return print;
  }
}

