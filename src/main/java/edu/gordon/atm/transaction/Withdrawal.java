/* ATM Example system - file Withdrawal.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.atm.transaction;

import edu.gordon.atm.banking.AccountInformation;

/**
 * Representation for a cash withdrawal transaction
 */
public class Withdrawal extends Transaction {
  /**
   * Constructor
   *
   * @param edu
   *          .gordon.atm the ATM used to communicate with customer
   * @param session
   *          the session in which the transaction is being performed
   * @param card
   *          the customer's card
   * @param pin
   *          the PIN entered by the customer
   */
  public Withdrawal(Card card, int pin) {
    super(card, pin);
  }

  /**
   * Complete an approved transaction
   *
   * @return receipt to be printed for this transaction
   */
  @Override
  public Receipt completeTransaction(String bankName, int atmID,
      String atmPlace) {
    Money total = new Money((int) balances.getTotal() / 100,
        (int) balances.getTotal() % 100);
    Money available = new Money((int) balances.getAvailable() / 100,
        (int) balances.getAvailable() % 100);
    return new Receipt(bankName, atmID, atmPlace, card, serialNumber, total,
        available) {
      {
        detailsPortion = new String[2];
        detailsPortion[0] = "WITHDRAWAL FROM: "
            + AccountInformation.ACCOUNT_ABBREVIATIONS[from];
        detailsPortion[1] = "AMOUNT: " + amount.toString();
      }
    };
  }

  /**
   * Get specifics for the transaction from the customer
   *
   * @return message to bank for initiating this transaction
   * @exception Cancelled
   *              if customer cancelled this transaction
   */
  @Override
  public Message getSpecificsFromCustomer(int action) throws Cancelled {
    return new Message(Message.WITHDRAWAL, card, pin, serialNumber, from, -1,
        amount);
  }
}
