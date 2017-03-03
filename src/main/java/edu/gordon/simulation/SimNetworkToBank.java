// Copyright ÉTS_ETE2016_LOG430-02. All rights reserved.
// Toute réutilisation de ce code source est interdite et sera considérée
// comme une infraction au sens de la résolution CA-331-3487:
// - Règlement sur les infractions de nature académique.

package edu.gordon.simulation;

import java.net.InetAddress;

import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Transaction;
import edu.gordon.atm.ui.interfaces.ILog;
import edu.gordon.atm.ui.interfaces.INetworkToBank;
import edu.gordon.atm.ui.interfaces.Status;
import edu.gordon.atm.ui.interfaces.StatusComponent;
import edu.gordon.eventsmanager.EventsManager;

/**
 * Simulate the network connection to the bank.
 *
 * @author benoit
 */
public class SimNetworkToBank implements INetworkToBank {
	private ILog log;
	private InetAddress bankAddress;
	private Simulation sim;
	private StatusComponent statusComp;

	/**
	 * Constructor
	 *
	 * @param log
	 *          the log in which to record sending of messages and responses
	 * @param bankAddress
	 *          the network address of the bank
	 */
	public SimNetworkToBank(ILog log, InetAddress bankAddress,
			Simulation simulation) {
		this.log = log;
		this.bankAddress = bankAddress;
		this.sim = simulation;
		this.statusComp = new StatusComponent();
		EventsManager.register(statusComp);
	}

	@Override
	public void closeConnection() {
		// Since the network is simulated, we don't have to do anything
	}

	@Override
	public void openConnection() {
		// Since the network is simulated, we don't have to do anything
	}

	@Override
	public void sendMessage(Message message, Transaction currTransact) {
		// Log sending of the message
		log.logSend(message);
		// Simulate the sending of the message - here is where the real code
		// to actually send the message over the network would go
		sim.sendMessage(message, currTransact);
		// Log the response gotten back
		
		log.logResponse(statusComp.getLastStatus());
	}
}
