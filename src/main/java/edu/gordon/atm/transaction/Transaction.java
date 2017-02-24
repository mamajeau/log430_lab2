/* ATM Example system - file Transaction.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.atm.transaction;

import edu.gordon.atm.banking.AccountInformation;
import edu.gordon.atm.banking.Balances;
import com.google.common.eventbus.EventBus;

/**
 * Abstract base class for classes representing the various kinds of transaction
 * the ATM can perform
 */
public abstract class Transaction {
  /**
   * Exception thrown when the user presses the cancel key while the ATM is
   * waiting for some action
   */
  public static class Cancelled extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -4004711757857722281L;

    /**
     * Constructor
     */
    public Cancelled() {
      super("Cancelled by customer");
    }
  }

  /**
   * Exception that is thrown when the customer's card is retained due to too
   * many invalid PIN entries
   */
  public static class CardRetained extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -7371002857615263987L;

    /**
     * Constructor
     */
    public CardRetained() {
      super("Card retained due to too many invalid PINs");
    }
  }

  /**
   * List of available transaction types to display as a menu
   */
  public static final String[] TRANSACTION_TYPES_MENU = { "Withdrawal",
      "Deposit", "Transfer", "Balance Inquiry" };
  /**
   * Next serial number - used to assign a unique serial number to each
   * transaction
   */
  private static int nextSerialNumber = 1;
  /**
   * Getting specifics of the transaction from customer
   */
  public static final int GETTING_SPECIFICS_STATE = 1;
  /**
   * Sending transaction to bank
   */
  public static final int SENDING_TO_BANK_STATE = 2;
  /**
   * Performing invalid PIN extension
   */
  public static final int INVALID_PIN_STATE = 3;
  /**
   * Completing transaction
   */
  public static final int COMPLETING_TRANSACTION_STATE = 4;
  /**
   * Printing receipt
   */
  public static final int PRINTING_RECEIPT_STATE = 5;
  /**
   * Asking if customer wants to do another transaction
   */
  public static final int ASKING_DO_ANOTHER_STATE = 6;

  public void initEventBus()
  {
      EventBus eventBus = new EventBus();

		eventBus.register(new DepositComponent());
		//eventBus.register(new WithdrawComponent());
  }
  
  
  /**
   * Create a transaction of an appropriate type by asking the customer what
   * type of transaction is desired and then returning a newly-created member of
   * the appropriate subclass
   *
   * @param edu
   *          .gordon.atm the ATM used to communicate with customer
   * @param session
   *          the session in which this transaction is being performed
   * @param card
   *          the customer's card
   * @param pin
   *          the PIN entered by the customer
   * @return a newly created Transaction object of the appropriate type
   * @exception Cancelled
   *              if the customer presses cancel instead of choosing a
   *              transaction type
   */
  public static Transaction makeTransaction(Card card, int pin, int choice)
      throws Cancelled {
    switch (choice) {
    case 0:
      return new Withdrawal(card, pin);
    case 1:
      return new Deposit(card, pin);
    case 2:
      return new Transfer(card, pin);
    case 3:
      return new Inquiry(card, pin);
    default:
      return null; // To keep compiler happy - should not happen!
    }
  }

  /**
   * Customer card for the session this transaction is part of
   */
  protected Card card;
  /**
   * PIN entered or re-entered by customer
   */
  protected int pin;
  /**
   * Serial number of this transaction
   */
  protected int serialNumber;
  /**
   * Message to bank describing this transaction
   */
  protected Message message;
  /**
   * Amount of money for transaction
   */
  protected Money amount;
  /**
   * Account source for transaction
   */
  protected int from;
  /**
   * Account destination for transaction
   */
  protected int to;
  /**
   * Used to return account balances from the bank
   */
  protected Balances balances;

  /**
   * Constructor
   *
   * @param edu
   *          .gordon.atm the ATM used to communicate with customer
   * @param session
   *          the session in which this transaction is being performed
   * @param card
   *          the customer's card
   * @param pin
   *          the PIN entered by the customer
   */
  protected Transaction(Card card, int pin) {
    this.card = card;
    this.pin = pin;
    this.serialNumber = nextSerialNumber++;
    this.balances = new Balances();
    initEventBus();
            
  }

  /**
   * Complete an approved transaction - each subclass must implement this
   * appropriately.
   *
   * @return receipt to be printed for this transaction
   * @exception Cancelled
   *              if customer cancelled this transaction
   */
  public abstract Receipt completeTransaction(String bankName, int atmID,
      String atmPlace) throws Cancelled;

  public String[] getAccounInformation() {
    return AccountInformation.ACCOUNT_NAMES;
  }

  public Money getAmount() {
    return this.amount;
  }

  public Balances getBalance() {
    return balances;
  }

  public Card getCard() {
    return this.card;
  }

  /**
   * Get serial number of this transaction
   *
   * @return serial number
   */
  public int getSerialNumber() {
    return serialNumber;
  }

  /**
   * Get specifics for the transaction from the customer - each subclass must
   * implement this appropriately.
   *
   * @param from
   * @return message to bank for initiating this transaction
   * @exception Cancelled
   *              if customer cancelled this transaction
   */
  public abstract Message getSpecificsFromCustomer(int action) throws Cancelled;

  public void setBalances(Money total, Money available) {
    balances.setBalances(total.getCents(), available.getCents());
  }

  public void setSpecifics(int from, int to, Money amount) {
    this.from = from;
    this.to = to;
    this.amount = amount;
  }
}
