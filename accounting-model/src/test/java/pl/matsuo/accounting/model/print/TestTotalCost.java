package pl.matsuo.accounting.model.print;

import org.junit.Test;

import static org.junit.Assert.*;
import static pl.matsuo.core.util.NumberUtil.*;

public class TestTotalCost {


  @Test
  public void testAddToValue() throws Exception {
    TotalCost totalCost = new TotalCost();
    totalCost.addToValue(bd(10));
    assertTrue(bd(10).compareTo(totalCost.getValue()) == 0);
  }


  @Test
  public void testAddToTax() throws Exception {
    TotalCost totalCost = new TotalCost();
    totalCost.addToTax(bd(10));
    assertTrue(bd(10).compareTo(totalCost.getTax()) == 0);
  }

  @Test
  public void testGetSum() throws Exception {
    TotalCost totalCost = new TotalCost();
    totalCost.addToValue(bd(10));
    totalCost.addToTax(bd(10));
    assertTrue(bd(20).compareTo(totalCost.getSum()) == 0);
  }
}