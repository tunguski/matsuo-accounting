package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.WithdrawSlip;

import java.math.BigDecimal;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@Service
public class WithdrawSlipService extends CashDocumentService<WithdrawSlip> {


  @Override
  protected void preCreate(AccountingPrint print, WithdrawSlip cashDocument) {
    cashDocument.setNumber(numerationService.getNumber("WITHDRAW_SLIP"));
  }


  protected AccountingPrint fillDocument(AccountingPrint print, WithdrawSlip withdrawSlip) {
    super.fillDocument(print, withdrawSlip);

    BigDecimal sum = sumSlipPositions(withdrawSlip);
    withdrawSlip.setTotalAmount(sum);
    withdrawSlip.setCashRegisterAmount(sum.negate());
    withdrawSlip.setTotalAmountInWords(speakCashAmount(sum));
    return print;
  }
}

