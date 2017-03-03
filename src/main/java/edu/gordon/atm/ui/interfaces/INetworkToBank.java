
package edu.gordon.atm.ui.interfaces;

import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Transaction;

/**
 * Manager for the ATM's network connection. In a real ATM, this would manage a
 * physical device; in this edu.gordon.simulation, it uses classes in package
 * edu.gordon.simulation to simulate the device.
 */
public interface INetworkToBank {
  /**
   * Close connection to bank at system shutdown
   */
  public void closeConnection();

  /**
   * Open connection to bank at system startup
   */
  public void openConnection();

  /**
   * Send a message to bank
   *
   * @param message
   *          the message to send
   * @param balances
   *          (out) balances in customer's account as reported by bank
   * @return status code returned by bank
   */
  public void sendMessage(Message message, Transaction currentTransaction);
}
