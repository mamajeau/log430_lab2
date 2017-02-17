/* ATM Example system - file CashDispenser.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.atm.ui;

import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.ui.interfaces.ICashDispenser;
import edu.gordon.atm.ui.interfaces.ILog;

/**
 * Physical Manager for the ATM's cash dispenser.
 */
public class CashDispenser implements ICashDispenser {
  /**
   * Log into which cash amounts dispensed will be recorded
   */
  private ILog log;
  /**
   * Current cash on hand
   */
  private Money cashOnHand;

  /**
   * Constructor
   *
   * @param log
   *          the log in which to record dispensing cash
   */
  public CashDispenser(ILog log) {
    this.log = log;
    cashOnHand = new Money(0);
  }

  /**
   * See if there is enough cash on hand to satisfy a request
   *
   * @param amount
   *          the amount of cash the customer wants
   * @return true if at least this amount of money is available
   */
  @Override
  public boolean checkCashOnHand(Money amount) {
    return amount.lessEqual(cashOnHand);
  }

  @Override
  public void dispenseCash(Money amount) {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  /**
   * Set the amount of cash initially on hand
   *
   * @param initialCash
   *          the amount of money in the dispenser
   */
  @Override
  public void setInitialCash(Money initialCash) {
    cashOnHand = initialCash;
  }
}
