package pl.matsuo.accounting.web.controller.print;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.WithdrawSlip;

import java.math.BigDecimal;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@RestController
@RequestMapping("/withdrawSlips")
public class WithdrawSlipController extends CashDocumentController<WithdrawSlip> {


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

