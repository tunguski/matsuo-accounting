package pl.matsuo.accounting.util;


import pl.matsuo.accounting.model.print.CorrectiveInvoice;
import pl.matsuo.accounting.model.print.CorrectiveInvoicePosition;
import pl.matsuo.accounting.model.print.InvoiceCommon;
import pl.matsuo.accounting.model.print.InvoicePosition;
import pl.matsuo.accounting.model.print.SlipCommon;
import pl.matsuo.accounting.model.print.SlipPosition;
import pl.matsuo.accounting.model.print.TotalCost;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.math.BigDecimal.*;
import static pl.matsuo.core.util.NumberUtil.*;
import static pl.matsuo.core.util.collection.CollectionUtil.*;


/**
 * Zbiór metod do przetwarzania danych w fakturze
 *
 * @author Mateusz Gałażyn <mateusz.galazyn@gmail.com>
 * @since 2013-08-16
 */
public class PrintUtil {


  /**
   * Sumator pozycji na fakturze
   */
  private static BiFunction<TotalCost, InvoicePosition, TotalCost> invoicePositionAdder =
      (TotalCost summary, InvoicePosition item) -> appendToSumOfInvoicePositions(summary, item);


  /**
   * Sumator stawek podatkowych na fakturze
   */
  private static BiFunction<Map<String, TotalCost>, InvoicePosition, Map<String, TotalCost>> taxRatesAdder =
      (Map<String, TotalCost> summary, InvoicePosition item) -> appendToTaxRate(summary, item);


  /**
   * Dodaje do taxRates, position
   *
   * @param taxRates
   *          Mapa, ktora jest lista stawek podatkowych (wartosci sa w kluczu)
   * @param position
   *          Pozycja
   * @return Mapa stawka VAT -> calkowity koszt
   */
  private static Map<String, TotalCost> appendToTaxRate(Map<String, TotalCost> taxRates, InvoicePosition position) {
    TotalCost totalTaxRateCost = new TotalCost();
    String key = position.getTaxRate();
    totalTaxRateCost.addToValue(position.getPrice().multiply(position.getCount())); // net weigh
    totalTaxRateCost.addToTax(position.getPrice().multiply(position.getCount())
        .multiply(decTax(position.getTaxRate()).scaleByPowerOfTen(-2))); // tax
    if (taxRates.containsKey(key)) {
      TotalCost tmp = taxRates.get(key);
      totalTaxRateCost.addToValue(tmp.getValue());
      totalTaxRateCost.addToTax(tmp.getTax());
    }
    taxRates.put(key, totalTaxRateCost);
    return taxRates;
  }


  /**
   * Sumuje pozycje w fakturze
   *
   * @param result
   *          suma pozycji w fakturze
   * @param position
   *          pojedyncza pozycja
   * @return zwraca zaktualizowany result
   */
  private static TotalCost appendToSumOfInvoicePositions(TotalCost result, InvoicePosition position) {
    result.addToValue(position.getPrice().multiply(position.getCount())); // net weigh
    result.addToTax(position.getPrice().multiply(position.getCount())
        .multiply(decTax(position.getTaxRate()).multiply(bd(".01")))); // tax
    return result;
  }


  /**
   * Czy pozycja jest korektą.
   * <ol>
   *   <li>Szukamy korekt: klasa pozycji jest korektą i wartość isAfterCorrection jest true</li>
   *   <li></li>
   * </ol>
   */
  protected static List<InvoicePosition> filter(List<? extends InvoicePosition> positions, final Boolean isCorrection) {
    return positions.stream().filter(
        (InvoicePosition position) ->
            isCorrection == (CorrectiveInvoicePosition.class.isAssignableFrom(position.getClass())
                && ((CorrectiveInvoicePosition) position).getIsAfterCorrection() != null
                && ((CorrectiveInvoicePosition) position).getIsAfterCorrection())
    ).collect(Collectors.toList());
  }


  /**
   * Tworzy listę stawek podatkowych
   */
  public static Map<String, TotalCost> createTaxRatesList(InvoiceCommon<? extends InvoicePosition> invoice) {
    return fold(filter((List<InvoicePosition>) invoice.getElements(), false),
        new LinkedHashMap<String, TotalCost>(), taxRatesAdder);
  }


  /**
   * Tworzy liste stawek podatkowych po korekcie faktury
   */
  public static Map<String, TotalCost> createCorrectedTaxRatesList(CorrectiveInvoice invoice) {
    return fold(filter(invoice.getElements(), true),
                   new LinkedHashMap<String,TotalCost>(), taxRatesAdder);
  }


  /**
   * Sumuje wszystkie wpisy w fakturze
   */
  public static TotalCost sumInvoicePositions(InvoiceCommon<? extends InvoicePosition> invoice) {
    return fold(filter(invoice.getElements(), false), new TotalCost(), invoicePositionAdder);
  }


  /**
   * Sumuje wszystkie wpisy w fakturze
   */
  public static TotalCost sumCorrectedInvoicePositions(CorrectiveInvoice invoice) {
    return fold(filter(invoice.getElements(), true), new TotalCost(), invoicePositionAdder);
  }


  /**
   * Sumuje wszystkie wartosci na druku wplaty/wyplaty
   */
  public static BigDecimal sumSlipPositions(SlipCommon slip) {
    return fold(slip.getElements(), ZERO,
        (BigDecimal partialSum, SlipPosition position) -> partialSum.add(position.getPrice()));
  }


  /**
   * Zamienia <i>null</i> na pusty string
   */
  public static String noNull(String input) {
    return (input == null) ? "" : input;
  }


  /**
   * Zamienia stawke podatkowa, dekoduje <i>zw</i> na <i>null</i>
   */
  public static BigDecimal decTax(String taxRate) {
    return (taxRate.compareTo("zw") == 0) ? ZERO : bd(taxRate);
  }


  public static Consumer<InvoicePosition> invoicePosition(
      String serviceName, BigDecimal count, BigDecimal price, String taxRate) {
    return invoicePosition -> {
      invoicePosition.setServiceName(serviceName);
      invoicePosition.setCount(count);
      invoicePosition.setPrice(price);
      invoicePosition.setTaxRate(taxRate);
    };
  }


  public static Consumer<InvoicePosition> invoicePosition(
      String serviceName, BigDecimal price, String taxRate) {
    return invoicePosition(serviceName, bd(1), price, taxRate);
  }


  public static Consumer<CorrectiveInvoicePosition> correctiveInvoicePosition(
      String serviceName, BigDecimal count, BigDecimal price, String taxRate) {
    return invoicePosition -> {
      invoicePosition(serviceName, count, price, taxRate).accept(invoicePosition);
      invoicePosition.setIsAfterCorrection(true);
    };
  }
}

