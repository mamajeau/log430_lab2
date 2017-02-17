
package edu.gordon.atm.ui.interfaces;

import edu.gordon.atm.transaction.Money;

/**
 * Manager for the ATM's operator panel. In a real ATM, this would manage a
 * physical device; in this edu.gordon.simulation, it uses classes in package
 * edu.gordon.simulation to simulate the device and code would be needed to
 * sense a change in the state of the switch and notify the ATM - simulated in
 * this case by a button in the GUI.
 */
public interface IOperatorPanel {
  /**
   * Get the amount of cash in the cash dispenser from the operator at start up
   *
   * @return dollar value of the bills in the cash dispenser (# of bills x $20)
   */
  public Money getInitialCash();

  public void setEnabled(boolean b);
}
