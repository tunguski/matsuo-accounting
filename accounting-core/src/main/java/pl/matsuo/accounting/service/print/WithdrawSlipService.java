package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.WithdrawSlip;

import java.math.BigDecimal;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@Service
public class WithdrawSlipService extends CashDocumentService<WithdrawSlip> {


  protected AccountingPrint fillDocument(AccountingPrint print, WithdrawSlip withdrawSlip) {
    super.fillDocument(print, withdrawSlip);

    BigDecimal sum = sumSlipPositions(withdrawSlip);
    print.setTotalAmount(sum);
    print.setValue(sum);
    withdrawSlip.setTotalAmountInWords(speakCashAmount(sum));
    return print;
  }
}

