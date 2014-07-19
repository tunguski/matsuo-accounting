package pl.matsuo.accounting.service.print;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.DepositSlip;

import java.math.BigDecimal;

import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;


@Service
public class DepositSlipService extends CashDocumentService<DepositSlip> {


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

