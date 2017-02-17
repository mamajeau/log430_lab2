// Copyright ÉTS_ETE2016_LOG430-02. All rights reserved.
// Toute réutilisation de ce code source est interdite et sera considérée
// comme une infraction au sens de la résolution CA-331-3487:
// - Règlement sur les infractions de nature académique.

package edu.gordon.simulation;

import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.ui.interfaces.ILog;
import edu.gordon.atm.ui.interfaces.Status;

/**
 * Simulate the ATM log.
 *
 * @author benoit
 */
public class SimLog implements ILog {
  private Simulation sim;

  public SimLog(Simulation simulation) {
    this.sim = simulation;
  }

  @Override
  public void logCashDispensed(Money amount) {
    sim.printLogLine("Dispensed: " + amount.toString());
  }

  @Override
  public void logEnvelopeAccepted() {
    sim.printLogLine("Envelope:  received");
  }

  @Override
  public void logResponse(Status response) {
    sim.printLogLine("Response:  " + response.toString());
  }

  @Override
  public void logSend(Message message) {
    sim.printLogLine("Message:   " + message.toString());
  }
}
