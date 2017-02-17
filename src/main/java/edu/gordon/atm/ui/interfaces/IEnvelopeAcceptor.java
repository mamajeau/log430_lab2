
package edu.gordon.atm.ui.interfaces;

import edu.gordon.atm.transaction.Transaction.Cancelled;

/**
 * Manager for the ATM's envelope acceptor. In a real ATM, this would manage a
 * physical device; in this edu.gordon.simulation, it uses classes in package
 * edu.gordon.simulation to simulate the device.
 */
public interface IEnvelopeAcceptor {
  /**
   * Accept an envelope from customer.
   *
   * @exception Cancelled
   *              if operation timed out or the customer cancelled it
   */
  public boolean acceptEnvelope() throws Cancelled;
}
