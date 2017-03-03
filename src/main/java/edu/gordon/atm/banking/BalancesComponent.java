package edu.gordon.atm.banking;

import com.google.common.eventbus.Subscribe;

public class BalancesComponent {

	private Balances lastBalances = null;
	
	@Subscribe
	public void handleEvent(NewBalancesEvent evt) {
		lastBalances = evt.getBalances();
	}
	
	public Balances getLastBalances() {
		return lastBalances;
	}
}
