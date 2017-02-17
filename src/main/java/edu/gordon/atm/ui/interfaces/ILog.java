
package edu.gordon.atm.ui.interfaces;

import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Money;

/**
 * Manager for the ATM's internal log. In a real ATM, this would manage a
 * physical device; in this edu.gordon.simulation, it uses classes in package
 * edu.gordon.simulation to simulate the device.
 */
public interface ILog {
  /**
   * Log the dispensing of cash by the cash dispenser
   *
   * @param amount
   *          the amount of cash being dispensed
   */
  public void logCashDispensed(Money amount);

  /**
   * Log accepting an envelope. This method is only called if an envelope is
   * actually received from the customer
   */
  public void logEnvelopeAccepted();

  /**
   * Log a response received from a message
   *
   * @param status
   *          the status object returned by the bank in response
   */
  public void logResponse(Status response);

  /**
   * Log the sending of a message to the bank
   *
   * @param message
   *          the message to be logged
   */
  public void logSend(Message message);
}
