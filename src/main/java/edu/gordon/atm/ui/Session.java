/* ATM Example system - file Session.java copyright (c) 2001 - Russell C. Bjork */

package edu.gordon.atm.ui;

import edu.gordon.atm.transaction.Card;
import edu.gordon.atm.transaction.Deposit;
import edu.gordon.atm.transaction.Inquiry;
import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.transaction.Receipt;
import edu.gordon.atm.transaction.Transaction;
import edu.gordon.atm.transaction.Transaction.Cancelled;
import edu.gordon.atm.transaction.Transaction.CardRetained;
import edu.gordon.atm.transaction.Transfer;
import edu.gordon.atm.transaction.Withdrawal;
import edu.gordon.atm.ui.interfaces.Status;
import edu.gordon.atm.ui.interfaces.StatusComponent;
import edu.gordon.eventsmanager.EventsManager;

/** Representation for one ATM session serving a single customer. */
public class Session {
	/**
	 * Reading the customer's card
	 */
	private static final int READING_CARD_STATE = 1;
	/**
	 * Asking the customer to enter a PIN
	 */
	private static final int READING_PIN_STATE = 2;
	/**
	 * Asking the customer to choose a transaction type
	 */
	private static final int CHOOSING_TRANSACTION_STATE = 3;
	/**
	 * Peforming a transaction
	 */
	private static final int PERFORMING_TRANSACTION_STATE = 4;
	/**
	 * Ejecting the customer's card
	 */
	private static final int EJECTING_CARD_STATE = 5;
	/**
	 * Session finished
	 */
	private static final int FINAL_STATE = 6;
	/**
	 * The ATM on which the session is performed
	 */
	private final ATM atm;
	/**
	 * The PIN entered (or re-entered) by the customer
	 */
	private int pin;
	/**
	 * The current state of the session
	 */
	private int state;
	/**
	 * The current state of the transaction
	 */
	private int stateTransaction;
	private int action;
	private StatusComponent statusComp;

	/**
	 * Constructor
	 *
	 * @param edu
	 *          .gordon.atm the ATM on which the session is performed
	 */
	public Session(ATM atm) {
		this.atm = atm;
		state = READING_CARD_STATE;
		stateTransaction = Transaction.GETTING_SPECIFICS_STATE;
		action = 0;
		statusComp = new StatusComponent();
	}

	private void handleUISpecifics(Transaction currentTransaction)
			throws Cancelled {
		Money amount = null;
		int from = -1;
		int to = -1;
		if (currentTransaction instanceof Inquiry) {
			from = atm.getCustomerConsole().readMenuChoice("Account to inquire from",
					currentTransaction.getAccounInformation());
		} else if (currentTransaction instanceof Deposit) {
			action = Message.INITIATE_DEPOSIT;
			to = atm.getCustomerConsole().readMenuChoice("Account to deposit to",
					currentTransaction.getAccounInformation());
			amount = atm.getCustomerConsole().readAmount("Amount to deposit");
		} else if (currentTransaction instanceof Transfer) {
			from = atm.getCustomerConsole().readMenuChoice("Account to transfer from",
					currentTransaction.getAccounInformation());
			to = atm.getCustomerConsole().readMenuChoice("Account to transfer to",
					currentTransaction.getAccounInformation());
			amount = atm.getCustomerConsole().readAmount("Amount to transfer");
		} else if (currentTransaction instanceof Withdrawal) {
			from = atm.getCustomerConsole().readMenuChoice("Account to withdraw from",
					currentTransaction.getAccounInformation());
			String[] amountOptions = { "$20", "$40", "$60", "$100", "$200" };
			Money[] amountValues = { new Money(20), new Money(40), new Money(60),
					new Money(100), new Money(200) };
			String amountMessage = "";
			boolean validAmount = false;
			while (!validAmount) {
				amount = amountValues[atm.getCustomerConsole().readMenuChoice(
						amountMessage + "Amount of cash to withdraw", amountOptions)];
				validAmount = atm.getCashDispenser().checkCashOnHand(amount);
				if (!validAmount) {
					amountMessage = "Insufficient cash available\n";
				}
			}
		}
		currentTransaction.setSpecifics(from, to, amount);
	}

	/**
	 * Perform the Invalid PIN Extension - reset session pin to new value if
	 * successful
	 *
	 * @param message
	 * @param currentTransaction
	 * @return status code returned by bank from most recent re-submission of
	 *         transaction
	 * @exception Cancelled
	 *              if customer presses the CANCEL key instead of re-entering PIN
	 * @exception CardRetained
	 *              if card was retained due to too many invalid PIN's
	 */
	public void performInvalidPINExtension(Message message,
			Transaction currentTransaction) throws Cancelled, CardRetained {
		
		atm.getCustomerConsole().clearDisplay();
		for (int i = 0; i < 3; i++) {
			pin = atm.getCustomerConsole().readPIN(
					"PIN was incorrect\nPlease re-enter your PIN\n" + "Then press ENTER");
			atm.getCustomerConsole().display("");
			message.setPIN(pin);
			atm.getNetworkToBank().sendMessage(message, currentTransaction);
			if (!statusComp.getLastStatus().isInvalidPIN()) {
				setPIN(pin);
				return;
			}
		}
		atm.getCardReader().retainCard();
		atm.getCustomerConsole()
		.display("Your card has been retained\nPlease contact the bank.");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		atm.getCustomerConsole().display("");
		throw new CardRetained();
	}

	/**
	 * Perform the Session Use Case
	 */
	public void performSession() {
		Card card = null;
		Transaction currentTransaction = null;
		while (state != FINAL_STATE) {
			switch (state) {
			case READING_CARD_STATE:
				card = atm.getCardReader().readCard();
				if (card != null) {
					state = READING_PIN_STATE;
				} else {
					atm.getCustomerConsole().display("Unable to read card");
					state = EJECTING_CARD_STATE;
				}
				break;
			case READING_PIN_STATE:
				try {
					pin = atm.getCustomerConsole()
							.readPIN("Please enter your PIN\n" + "Then press ENTER");
					state = CHOOSING_TRANSACTION_STATE;
				} catch (Cancelled e) {
					state = EJECTING_CARD_STATE;
				}
				break;
			case CHOOSING_TRANSACTION_STATE:
				try {
					int choice = atm.getCustomerConsole().readMenuChoice(
							"Please choose transaction type",
							Transaction.TRANSACTION_TYPES_MENU);
					currentTransaction = Transaction.makeTransaction(card, pin, choice);
					state = PERFORMING_TRANSACTION_STATE;
				} catch (Cancelled e) {
					state = EJECTING_CARD_STATE;
				}
				break;
			case PERFORMING_TRANSACTION_STATE:
				try {
					boolean doAnotherTransaction = performTransaction(currentTransaction);
					if (doAnotherTransaction) {
						state = CHOOSING_TRANSACTION_STATE;
					} else {
						state = EJECTING_CARD_STATE;
					}
				} catch (Transaction.CardRetained e) {
					state = FINAL_STATE;
				}
				break;
			case EJECTING_CARD_STATE:
				atm.getCardReader().ejectCard();
				state = FINAL_STATE;
				break;
			}
		}
	}

	/**
	 * Peform a transaction. This method depends on the three abstract methods
	 * that follow to perform the operations unique to each type of transaction in
	 * the appropriate way.
	 *
	 * @param currentTransaction
	 * @return true if customer indicates a desire to do another transaction;
	 *         false if customer does not desire to do another transaction
	 * @exception CardRetained
	 *              if card was retained due to too many invalid PIN's
	 */
	public boolean performTransaction(Transaction currentTransaction)
			throws CardRetained {
		String doAnotherMessage = "";
		Receipt receipt = null;
		Message message = null;
		stateTransaction = Transaction.GETTING_SPECIFICS_STATE;
		while (true) // Terminates by return in ASKING_DO_ANOTHER_STATE or exception
		{
			switch (stateTransaction) {
			case Transaction.GETTING_SPECIFICS_STATE:
				message = null;
				try {
					handleUISpecifics(currentTransaction);
					message = currentTransaction.getSpecificsFromCustomer(action);
					atm.getCustomerConsole().display("");
					stateTransaction = Transaction.SENDING_TO_BANK_STATE;
				} catch (Cancelled e) {
					doAnotherMessage = "Last transaction was cancelled";
					stateTransaction = Transaction.ASKING_DO_ANOTHER_STATE;
				}
				break;
			case Transaction.SENDING_TO_BANK_STATE:
				atm.getNetworkToBank().sendMessage(message,
						currentTransaction);
				if (statusComp.getLastStatus().isInvalidPIN()) {
					stateTransaction = Transaction.INVALID_PIN_STATE;
				} else if (statusComp.getLastStatus().isSuccess()) {
					stateTransaction = Transaction.COMPLETING_TRANSACTION_STATE;
				} else {
					doAnotherMessage = statusComp.getLastStatus().getMessage();
					stateTransaction = Transaction.ASKING_DO_ANOTHER_STATE;
				}
				break;
			case Transaction.INVALID_PIN_STATE:
				try {
					performInvalidPINExtension(message, currentTransaction);
					// If customer repeatedly enters invalid PIN's, a
					// CardRetained exception is thrown, and this method
					// terminates
					if (statusComp.getLastStatus().isSuccess()) {
						stateTransaction = Transaction.COMPLETING_TRANSACTION_STATE;
					} else {
						doAnotherMessage = statusComp.getLastStatus().getMessage();
						stateTransaction = Transaction.ASKING_DO_ANOTHER_STATE;
					}
				} catch (Cancelled e) {
					doAnotherMessage = "Last transaction was cancelled";
					stateTransaction = Transaction.ASKING_DO_ANOTHER_STATE;
				}
				break;
			case Transaction.COMPLETING_TRANSACTION_STATE:
				try {
					prepareTransaction(currentTransaction);
					receipt = currentTransaction.completeTransaction(atm.getBankName(),
							atm.getID(), atm.getPlace());
					stateTransaction = Transaction.PRINTING_RECEIPT_STATE;
				} catch (Cancelled e) {
					doAnotherMessage = "Last transaction was cancelled";
					stateTransaction = Transaction.ASKING_DO_ANOTHER_STATE;
				}
				break;
			case Transaction.PRINTING_RECEIPT_STATE:
				atm.getReceiptPrinter().printReceipt(receipt);
				stateTransaction = Transaction.ASKING_DO_ANOTHER_STATE;
				break;
			case Transaction.ASKING_DO_ANOTHER_STATE:
				if (doAnotherMessage.length() > 0) {
					doAnotherMessage += "\n";
				}
				try {
					String[] yesNoMenu = { "Yes", "No" };
					boolean doAgain = atm.getCustomerConsole().readMenuChoice(
							doAnotherMessage + "Would you like to do another transaction?",
							yesNoMenu) == 0;
					return doAgain;
				} catch (Cancelled e) {
					return false;
				}
			}
		}
	}

	private void prepareTransaction(Transaction currentTransaction)
			throws Cancelled {
		if (currentTransaction instanceof Deposit) {
			atm.getEnvelopeAcceptor().acceptEnvelope();
			int action = Message.INITIATE_DEPOSIT;
			atm.getNetworkToBank().sendMessage(
					currentTransaction.getSpecificsFromCustomer(action),
					currentTransaction);
		} else if (currentTransaction instanceof Withdrawal) {
			atm.getCashDispenser().dispenseCash(currentTransaction.getAmount());
		}
	}

	/**
	 * Change the pin recorded for the customer (if invalid pin extension was
	 * performed by a transaction
	 *
	 * @param pin
	 *          the newly entered pin
	 */
	public void setPIN(int pin) {
		this.pin = pin;
	}
}
