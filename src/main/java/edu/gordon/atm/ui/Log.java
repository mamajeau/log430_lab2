/* ATM Example system - file Log.java copyright (c) 2001 - Russell C. Bjork */

package edu.gordon.atm.ui;

import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.ui.interfaces.ILog;
import edu.gordon.atm.ui.interfaces.Status;

/**
 * Physical Manager for the ATM's internal log.
 */
public class Log implements ILog {
  /**
   * Constructor
   */
  public Log() {
  }

  /**
   * Log the dispensing of cash by the cash dispenser
   *
   * @param amount
   *          the amount of cash being dispensed
   */
  @Override
  public void logCashDispensed(Money amount) {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  /**
   * Log accepting an envelope. This method is only called if an envelope is
   * actually received from the customer
   */
  @Override
  public void logEnvelopeAccepted() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  /**
   * Log a response received from a message
   *
   * @param status
   *          the status object returned by the bank in response
   */
  @Override
  public void logResponse(Status response) {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  /**
   * Log the sending of a message to the bank
   *
   * @param message
   *          the message to be logged
   */
  @Override
  public void logSend(Message message) {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }
}
