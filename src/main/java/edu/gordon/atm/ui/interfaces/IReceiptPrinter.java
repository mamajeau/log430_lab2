
package edu.gordon.atm.ui.interfaces;

import edu.gordon.atm.transaction.Receipt;

/**
 * Manager for the ATM's receipt printer. In a real ATM, this would manage a
 * physical device; in this edu.gordon.simulation, it uses classes in package
 * edu.gordon.simulation to simulate the device.
 */
public interface IReceiptPrinter {
  /**
   * Print a receipt
   *
   * @param receipt
   *          object containing the information to be printed
   */
  public void printReceipt(Receipt receipt);
}
