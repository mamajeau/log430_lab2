/* ATM Example system - file CustomerConsole.java copyright (c) 2001 - Russell
 * C. Bjork */

package edu.gordon.atm.ui;

import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.transaction.Transaction.Cancelled;
import edu.gordon.atm.ui.interfaces.ICustomerConsole;

/**
 * Physical ATM Customer console.
 */
public class CustomerConsole implements ICustomerConsole {
  /**
   * Constructor
   */
  public CustomerConsole() {
  }

  @Override
  public void clearDisplay() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public void display(String message) {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public synchronized Money readAmount(String prompt) throws Cancelled {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public synchronized int readMenuChoice(String prompt, String[] menu)
      throws Cancelled {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public int readPIN(String prompt) throws Cancelled {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }
}
