/* ATM Example system - file NetworkToBank.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.atm.ui;

import java.net.InetAddress;

import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Transaction;
import edu.gordon.atm.ui.interfaces.ILog;
import edu.gordon.atm.ui.interfaces.INetworkToBank;
import edu.gordon.atm.ui.interfaces.Status;

/**
 * Physical Manager for the ATM's network connection.
 */
public class NetworkToBank implements INetworkToBank {
  private ILog log;
  private InetAddress bankAddress;

  /**
   * Constructor
   *
   * @param log
   *          the log in which to record sending of messages and responses
   * @param bankAddress
   *          the network address of the bank
   */
  public NetworkToBank(ILog log, InetAddress bankAddress) {
    this.log = log;
    this.bankAddress = bankAddress;
  }

  @Override
  public void closeConnection() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public void openConnection() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public Status sendMessage(Message message, Transaction currTransact) {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }
}
