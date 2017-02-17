
package edu.gordon.atm.ui.interfaces;

import edu.gordon.atm.transaction.Money;

/**
 * Manager for the ATM's cash dispenser. In a real ATM, this would manage a
 * physical device; in this edu.gordon.simulation, it uses classes in package
 * edu.gordon.simulation to simulate the device.
 */
public interface ICashDispenser {
  /**
   * See if there is enough cash on hand to satisfy a request
   *
   * @param amount
   *          the amount of cash the customer wants
   * @return true if at least this amount of money is available
   */
  public boolean checkCashOnHand(Money amount);

  /**
   * Dispense cash to a customer
   *
   * @param amount
   *          the amount of cash to dispense Precondition: amount is <= cash on
   *          hand
   */
  public void dispenseCash(Money amount);

  /**
   * Set the amount of cash initially on hand
   *
   * @param initialCash
   *          the amount of money in the dispenser
   */
  public void setInitialCash(Money initialCash);
}
