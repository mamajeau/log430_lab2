/* ATM Example system - file SimCashDispenser.java copyright (c) 2001 - Russell
 * C. Bjork */

package edu.gordon.simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.ui.interfaces.ICashDispenser;
import edu.gordon.atm.ui.interfaces.ILog;

/**
 * Simulate the cash dispenser.
 */
class SimCashDispenser extends Panel implements ICashDispenser {
  /**
   *
   */
  private static final long serialVersionUID = 3187405762984625755L;
  /**
   * Label that shows the amount of money being dispensed - starts out small and
   * grows to simulate cash coming out of the machine
   */
  private Label label;
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
   */
  SimCashDispenser(ILog log) {
    this.log = log;
    cashOnHand = new Money(0);
    setLayout(new GridLayout(1, 1));
    label = new Label("$XXX.XX", Label.CENTER);
    label.setFont(new Font("SansSerif", Font.PLAIN, 24));
    label.setForeground(new Color(0, 64, 0));
    add(label);
    label.setVisible(false);
  }

  /**
   * Animate dispensing cash to a customer
   *
   * @param amount
   *          the amount of cash to dispense Precondition: amount is <= cash on
   *          hand
   */
  public void animateDispensingCash(Money amount) {
    label.setText(amount.toString());
    for (int size = 3; size <= 24; size += 1) {
      label.setFont(new Font("SansSerif", Font.PLAIN, size));
      label.setVisible(true);
      try {
        Thread.sleep(250);
      } catch (InterruptedException e) {
      }
      label.setVisible(false);
    }
  }

  @Override
  public boolean checkCashOnHand(Money amount) {
    return amount.lessEqual(cashOnHand);
  }

  @Override
  public void dispenseCash(Money amount) {
    cashOnHand.subtract(amount);
    animateDispensingCash(amount);
    // Log cash having been dispensed
    log.logCashDispensed(amount);
  }

  @Override
  public void setInitialCash(Money initialCash) {
    cashOnHand = initialCash;
  }
}
