// Copyright ÉTS_AUT2016_LOG430. All rights reserved.
// Toute réutilisation de ce code source est interdite et sera considérée
// comme une infraction au sens de la résolution CA-331-3487:
// - Règlement sur les infractions de nature académique.

package edu.gordon.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.gordon.atm.banking.Balances;
import edu.gordon.atm.banking.BalancesComponent;
import edu.gordon.atm.transaction.Card;
import edu.gordon.atm.transaction.Deposit;
import edu.gordon.atm.transaction.Inquiry;
import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.transaction.TransactionEvent;
import edu.gordon.atm.transaction.Transfer;
import edu.gordon.atm.transaction.Withdrawal;
import edu.gordon.atm.ui.interfaces.StatusComponent;
import edu.gordon.eventsmanager.EventsManager;
import edu.gordon.simulation.SimulatedBank;

/**
 * Test case of the 4 core functionalities.
 * 
 * @author benoit
 */
public class AtmTestCase {
	private int serialNb = 1;
	private SimulatedBank simBank;
	private StatusComponent statusComp;
	private BalancesComponent balancesComp;
	private Money cash;
	private Card card;
	private Balances balances;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		simBank = new SimulatedBank();
		statusComp = new StatusComponent();
		balancesComp = new BalancesComponent();
		EventsManager.register(simBank);
		EventsManager.register(statusComp);
		EventsManager.register(balancesComp);
		card = new Card(1);
		cash = new Money(20);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		EventsManager.unregister(simBank);
		EventsManager.unregister(statusComp);
		EventsManager.unregister(balancesComp);
	}

	@Test
	public void testBalanceInquiry() {
		Inquiry transact = new Inquiry(card, 42);
		Message msg = new Message(Message.INQUIRY, card, 42, serialNb++, 0, -1,
				cash);
		EventsManager.post(new TransactionEvent(this, msg, transact));
		
		assertTrue("Operation failed", statusComp.getLastStatus().isSuccess());
		
		balances = balancesComp.getLastBalances();
		assertEquals("Available balance is incorrect", 100,
				(int) balances.getAvailable() / 100);
		assertEquals("Total balance is incorrect", 100,
				(int) balances.getTotal() / 100);
	}
	
	@Test
	public void testDeposit() {
		Deposit transact = new Deposit(card, 42);
		Message msg = new Message(Message.INITIATE_DEPOSIT, card, 42, serialNb++,
				-1, 0, cash);
		
		EventsManager.post(new TransactionEvent(this, msg, transact));
		
		assertTrue("Operation failed", statusComp.getLastStatus().isSuccess());
		msg = new Message(Message.COMPLETE_DEPOSIT, card, 42, serialNb++, -1, 0,
				cash);
		EventsManager.post(new TransactionEvent(this, msg, transact));
		assertTrue("Operation failed", statusComp.getLastStatus().isSuccess());
		
		balances = balancesComp.getLastBalances();
		assertEquals("Available balance is incorrect", 100,
				(int) balances.getAvailable() / 100);
		assertEquals("Total balance is incorrect", 120,
				(int) balances.getTotal() / 100);
	}

	@Test
	public void testTransfer() {
		Transfer transact = new Transfer(card, 42);
		Message msg = new Message(Message.TRANSFER, card, 42, serialNb++, 0, 1,
				cash);
		
		EventsManager.post(new TransactionEvent(this, msg, transact));
		
		assertTrue("Operation failed", statusComp.getLastStatus().isSuccess());
		
		balances = balancesComp.getLastBalances();
		assertEquals("Available balance is incorrect", 1020,
				(int) balances.getAvailable() / 100);
		assertEquals("Total balance is incorrect", 1020,
				(int) balances.getTotal() / 100);
	}

	@Test
	public void testWithdrawal() {
		Withdrawal transact = new Withdrawal(card, 42);
		Message msg = new Message(Message.WITHDRAWAL, card, 42, serialNb++, 0, -1,
				cash);
		
		EventsManager.post(new TransactionEvent(this, msg, transact));
		
		assertTrue("Operation failed", statusComp.getLastStatus().isSuccess());
		
		balances = balancesComp.getLastBalances();
		assertEquals(80, (int) balances.getAvailable() / 100);
		assertEquals(80, (int) balances.getTotal() / 100);
	}
}
