package pl.matsuo.accounting.model.print;

/**
 * Model danych druku kasa wydała.
 *
 * @since Aug 16, 2013
 */
public interface DepositSlip extends SlipCommon {

  String getAccountant();
  void setAccountant(String text);
}

