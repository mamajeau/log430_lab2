/* ATM Example system - file SimulatedBank.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.simulation;

import com.google.common.eventbus.Subscribe;

import edu.gordon.atm.banking.NewBalancesEvent;
import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.transaction.Transaction;
import edu.gordon.atm.transaction.TransactionEvent;
import edu.gordon.atm.ui.interfaces.NewStatusEvent;
import edu.gordon.atm.ui.interfaces.Status;
import edu.gordon.eventsmanager.EventsManager;

/**
 * Simulation of the bank. A set of simulated accounts is initalized at startup.
 */
public class SimulatedBank {
	/**
	 * Representation for status of a transaction that failed (for reason other
	 * than invalid PIN)
	 */
	private static class Failure extends Status {
		private String message;

		/**
		 * Constructor
		 *
		 * @param message
		 *          description of the failure
		 */
		public Failure(String message) {
			this.message = message;
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public boolean isInvalidPIN() {
			return false;
		}

		@Override
		public boolean isSuccess() {
			return false;
		}
	}

	/**
	 * Representation for status of a transaction that failed due to an invalid
	 * PIN
	 */
	private static class InvalidPIN extends Failure {
		/**
		 * Constructor
		 *
		 * @param message
		 *          description of the failure
		 */
		public InvalidPIN() {
			super("Invalid PIN");
		}

		@Override
		public boolean isInvalidPIN() {
			return true;
		}
	}

	/**
	 * Representation for status of a transaction that succeeded
	 */
	private static class Success extends Status {
		@Override
		public String getMessage() {
			return null;
		}

		@Override
		public boolean isInvalidPIN() {
			return false;
		}

		@Override
		public boolean isSuccess() {
			return true;
		}
	}

	/**
	 * PIN for each card. (Valid card numbers start with 1)
	 */
	private static final int PIN[] = { 0, // dummy for nonexistent card 0
			42, 1234 };
	/**
	 * Array of account numbers associated with each card. For each card, there
	 * can be three different types of account, which correspond to the names in
	 * class AccountInformation. 0 means no account of this type. (Valid card
	 * numbers start with 1)
	 */
	private static final int ACCOUNT_NUMBER[][] = { { 0, 0, 0 }, // dummies for
			// nonexistent
			// card 0
			{ 1, 2, 0 }, { 1, 0, 3 } };
	/**
	 * Withdrawals so far today on each card. (Valid card numbers start with 1)
	 */
	private static Money WITHDRAWALS_TODAY[] = { new Money(0), // dummy for
			// nonexistent card
			// 0
			new Money(0), new Money(0) };
	/**
	 * Maximum daily withdrawal limit for any one card.
	 */
	private static final Money DAILY_WITHDRAWAL_LIMIT = new Money(300);
	/**
	 * Balance for each account (will change as program runs, hence not a static
	 * final.
	 */
	private Money BALANCE[] = { new Money(0), // dummy for nonexistent account 0
			new Money(100), new Money(1000), new Money(5000) };
	/**
	 * Available alance for each account (will change as program runs, hence not a
	 * static final.
	 */
	private Money AVAILABLE_BALANCE[] = { new Money(0), // dummy for nonexistent
			// account 0
			new Money(100), new Money(1000), new Money(5000) };

	public SimulatedBank() { }
	
	/**
	 * Simulate the handling of a message
	 *
	 * @param message
	 *          the message to send
	 * @param currTransact.getBalance()
	 *          (out) balances in customer's account as reported by bank
	 * @return status code returned by bank
	 */
	@Subscribe
	public void handleTransaction(TransactionEvent evt) {
		System.out.println(evt.getMessage().getMessageCode());
		Message message = evt.getMessage();
		Transaction currTransact = evt.getTransaction();
		
		int cardNumber = message.getCard().getNumber();
		if (cardNumber < 1 || cardNumber > PIN.length) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Invalid card")));
			return;
		}
		if (message.getPIN() != PIN[cardNumber]) {
			EventsManager.post(new NewStatusEvent(this, new InvalidPIN()));
			return;
		}
		
		switch (evt.getMessage().getMessageCode()) {
		case Message.WITHDRAWAL:
			withdrawal(message, currTransact);
			break;
		case Message.INITIATE_DEPOSIT:
			initiateDeposit(message);
			break;
		case Message.COMPLETE_DEPOSIT:
			completeDeposit(message, currTransact);
			break;
		case Message.TRANSFER:
			transfer(message, currTransact);
			break;
		case Message.INQUIRY:
			inquiry(message, currTransact);
			break;
		}
	}

	/**
	 * Simulate initiation of a deposit. At this point, the bank only approves the
	 * validity of the deposit - no changes to the records are made until the
	 * envelope is actually inserted
	 *
	 * @param message
	 *          the message describing the deposit requested
	 * @return status code derived from current values
	 */
	private void initiateDeposit(Message message) {

		int cardNumber = message.getCard().getNumber();
		int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
		if (accountNumber == 0) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Invalid account type")));
			return;
		}
		// Don't update anything yet
		EventsManager.post(new NewStatusEvent(this, new Success()));
	}
	
	/**
	 * Simulate completion of a deposit
	 *
	 * @param message
	 *          the message describing the deposit requested
	 * @param currTransact
	 *          (out) balances (not updated until completed)
	 * @return status code - must always be success in this case
	 */
	private void completeDeposit(Message message, Transaction currTransact) {
		
		int cardNumber = message.getCard().getNumber();
		int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
		if (accountNumber == 0) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Invalid account type")));
			return;
		}
		// Now we can update the balance
		Money amount = message.getAmount();
		BALANCE[accountNumber].add(amount);
		// Return updated balances
		currTransact.setBalances(BALANCE[accountNumber],
				AVAILABLE_BALANCE[accountNumber]);
		
		EventsManager.post(new NewStatusEvent(this, new Success()));
		EventsManager.post(new NewBalancesEvent(this, currTransact.getBalance()));
	}

	/**
	 * Simulate processing of an inquiry
	 *
	 * @param message
	 *          the message describing the inquiry requested
	 * @param currTransact
	 *          (out) balances in account
	 * @return status code derived from current values
	 */
	private void inquiry(Message message, Transaction currTransact) {
		
		int cardNumber = message.getCard().getNumber();
		int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getFromAccount()];
		if (accountNumber == 0) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Invalid account type")));
			return;
		}
		// Return requested balances
		currTransact.setBalances(BALANCE[accountNumber],
				AVAILABLE_BALANCE[accountNumber]);
		
		EventsManager.post(new NewStatusEvent(this, new Success()));
		EventsManager.post(new NewBalancesEvent(this, currTransact.getBalance()));
	}

	/**
	 * Simulate processing of a transfer
	 *
	 * @param message
	 *          the message describing the transfer requested
	 * @param currTransact
	 *          (out) balances in "to" account after transfer
	 * @return status code derived from current values
	 */
	private void transfer(Message message, Transaction currTransact) {
		
		int cardNumber = message.getCard().getNumber();
		int fromAccountNumber = ACCOUNT_NUMBER[cardNumber][message
		                                                   .getFromAccount()];
		if (fromAccountNumber == 0) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Invalid from account type")));
			return;
		}
		int toAccountNumber = ACCOUNT_NUMBER[cardNumber][message.getToAccount()];
		if (toAccountNumber == 0) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Invalid to account type")));
			return;
		}
		if (fromAccountNumber == toAccountNumber) {
			EventsManager.post(new NewStatusEvent(this, 
					new Failure("Can't transfer money from\n" + "an account to itself")));
			return;
		}
		Money amount = message.getAmount();
		if (!amount.lessEqual(AVAILABLE_BALANCE[fromAccountNumber])) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Insufficient available balance")));
			return;
		}
		// Update account balances once we know everything is OK
		BALANCE[fromAccountNumber].subtract(amount);
		AVAILABLE_BALANCE[fromAccountNumber].subtract(amount);
		BALANCE[toAccountNumber].add(amount);
		AVAILABLE_BALANCE[toAccountNumber].add(amount);
		// Return updated balances
		currTransact.setBalances(BALANCE[toAccountNumber],
				AVAILABLE_BALANCE[toAccountNumber]);
		
		EventsManager.post(new NewStatusEvent(this, new Success()));
		EventsManager.post(new NewBalancesEvent(this, currTransact.getBalance()));
	}

	/**
	 * Simulate processing of a withdrawal
	 *
	 * @param message
	 *          the message describing the withdrawal requested
	 * @param currTransact
	 *          (out) balances in account after withdrawal
	 * @return status code derived from current values
	 */
	private void withdrawal(Message message, Transaction currTransact) {
		
		int cardNumber = message.getCard().getNumber();
		int accountNumber = ACCOUNT_NUMBER[cardNumber][message.getFromAccount()];
		if (accountNumber == 0) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Invalid account type")));
			return;
		}
		Money amount = message.getAmount();
		Money limitRemaining = new Money(DAILY_WITHDRAWAL_LIMIT);
		limitRemaining.subtract(WITHDRAWALS_TODAY[cardNumber]);
		if (!amount.lessEqual(limitRemaining)) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Daily withdrawal limit exceeded")));
			return;
		}
		if (!amount.lessEqual(AVAILABLE_BALANCE[accountNumber])) {
			EventsManager.post(new NewStatusEvent(this, new Failure("Insufficient available balance")));
			return;
		}
		// Update withdrawals today and account balances once we know everything is
		// OK
		WITHDRAWALS_TODAY[cardNumber].add(amount);
		BALANCE[accountNumber].subtract(amount);
		AVAILABLE_BALANCE[accountNumber].subtract(amount);
		// Return updated balances
		currTransact.setBalances(BALANCE[accountNumber],
				AVAILABLE_BALANCE[accountNumber]);
		
		EventsManager.post(new NewStatusEvent(this, new Success()));
		EventsManager.post(new NewBalancesEvent(this, currTransact.getBalance()));
	}
}
