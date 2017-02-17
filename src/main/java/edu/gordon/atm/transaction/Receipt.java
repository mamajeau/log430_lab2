/* ATM Example system - file Receipt.java copyright (c) 2001 - Russell C. Bjork */

package edu.gordon.atm.transaction;

import java.util.Date;
import java.util.Enumeration;

/**
 * Abstract base class for representation of a receipt to be printed. Each
 * specific type of transaction creates an instance of a concrete subclass of
 * this class.
 */
public abstract class Receipt {
  /**
   * Heading portion of the receipt - common to all forms of receipt
   */
  private String[] headingPortion;
  /**
   * Transaction details portion of the receipt - specific to each type of
   * transaction, and therefore filled in by subclasses
   */
  protected String[] detailsPortion;
  /**
   * Ending balances portion of the receipt - common to all forms of receipt
   */
  private String[] balancesPortion;

  /**
   * Constructor. This base class constructor will create the portions of the
   * receipt that are common to all types of transaction, and the subclass
   * constructor will create the details portion unique to each type.
   *
   * @param edu
   *          .gordon.atm the ATM where the transaction was done
   * @param card
   *          the card that was used
   * @param transaction
   *          the Transaction object for the transaction
   * @param balances
   *          Balances object giving final balances for account used
   */
  protected Receipt(String bankName, int atmID, String atmPlace, Card card,
      int tSerialNb, Money total, Money available) {
    // Heading portion of the receipt
    headingPortion = new String[4];
    headingPortion[0] = new Date().toString();
    headingPortion[1] = bankName;
    headingPortion[2] = "ATM #" + atmID + " " + atmPlace;
    headingPortion[3] = "CARD " + card.getNumber() + " TRANS #" + tSerialNb;
    // The constructor for each subclass will fill in the details array
    // appropriately
    // Balances portion of the receipt
    balancesPortion = new String[2];
    balancesPortion[0] = "TOTAL BAL: " + total;
    balancesPortion[1] = "AVAILABLE: " + available;
  }

  /**
   * Get the individual lines to be printed. Each call to the nextElement() of
   * the enumeration gets one line (as a String)
   */
  public Enumeration getLines() {
    return new Enumeration() {
      // The current portion of the receipt being printed
      private int portion = 0;
      // The index of the line in the current portion
      private int index = 0;

      @Override
      public boolean hasMoreElements() {
        return portion < 2 || index < balancesPortion.length;
      }

      @Override
      public Object nextElement() {
        String line = null;
        switch (portion) {
        case 0:
          line = headingPortion[index++];
          if (index >= headingPortion.length) {
            portion++;
            index = 0;
          }
          break;
        case 1:
          line = detailsPortion[index++];
          if (index >= detailsPortion.length) {
            portion++;
            index = 0;
          }
          break;
        case 2:
          line = balancesPortion[index++];
          break;
        }
        return line;
      }
    };
  }
}
