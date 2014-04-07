package pl.matsuo.accounting.web.controller.print;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.DepositSlip;

import java.math.BigDecimal;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@Controller
@RequestMapping("/depositSlips")
public class DepositSlipController extends CashDocumentController<DepositSlip> {


  @Override
  protected void preCreate(AccountingPrint print, DepositSlip cashDocument) {
    cashDocument.setNumber(numerationService.getNumber("DEPOSIT_SLIP"));
  }


  protected AccountingPrint fillDocument(AccountingPrint print, DepositSlip depositSlip) {
    super.fillDocument(print, depositSlip);

    BigDecimal sum = sumSlipPositions(depositSlip);
    depositSlip.setTotalAmount(sum);
    depositSlip.setCashRegisterAmount(sum.negate());
    depositSlip.setTotalAmountInWords(speakCashAmount(sum));
    return print;
  }
}

