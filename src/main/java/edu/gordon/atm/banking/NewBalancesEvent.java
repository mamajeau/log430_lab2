package edu.gordon.atm.banking;

import java.util.EventObject;

public class NewBalancesEvent extends EventObject {

	private Balances balances;
	
	public NewBalancesEvent(Object source, Balances newBalances) {
		super(source);
		balances = newBalances;
	}

	public Balances getBalances() {
		return balances;
	}
}
