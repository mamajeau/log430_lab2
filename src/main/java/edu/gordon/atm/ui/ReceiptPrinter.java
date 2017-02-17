/* ATM Example system - file ReceiptPrinter.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.atm.ui;

import edu.gordon.atm.transaction.Receipt;
import edu.gordon.atm.ui.interfaces.IReceiptPrinter;

/**
 * Manager for the ATM's receipt printer.
 */
public class ReceiptPrinter implements IReceiptPrinter {
  /**
   * Constructor
   */
  public ReceiptPrinter() {
  }

  @Override
  public void printReceipt(Receipt receipt) {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }
}
