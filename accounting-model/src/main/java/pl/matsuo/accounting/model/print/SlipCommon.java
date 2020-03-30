package pl.matsuo.accounting.model.print;

/**
 * Model danych faktury.
 *
 * @since Aug 16, 2013
 */
public interface SlipCommon extends CashDocument<SlipPosition> {

  String getTotalAmountInWords();

  void setTotalAmountInWords(String totalAmountInWords);

  String getCreator();

  void setCreator(String text);

  String getApprovingPerson();

  void setApprovingPerson(String text);

  String getCashReportReference();

  void setCashReportReference(String text);
}
