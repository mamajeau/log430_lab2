/* ATM Example system - file EnvelopeAcceptor.java copyright (c) 2001 - Russell
 * C. Bjork */

package edu.gordon.atm.ui;

import edu.gordon.atm.ui.interfaces.IEnvelopeAcceptor;
import edu.gordon.atm.ui.interfaces.ILog;

/**
 * Physical Manager for the ATM's envelope acceptor.
 */
public class EnvelopeAcceptor implements IEnvelopeAcceptor {
  /**
   * Log in which to record receiving an envelope
   */
  private ILog log;

  /**
   * Constructor
   *
   * @param log
   *          the log in which to record receiving an envelope
   */
  public EnvelopeAcceptor(ILog log) {
    this.log = log;
  }

  @Override
  public boolean acceptEnvelope() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }
}
