/* ATM Example system - file CardReader.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.atm.ui;

import edu.gordon.atm.transaction.Card;
import edu.gordon.atm.ui.interfaces.ICardReader;

/**
 * Physical Manager for the ATM's card reader. In a real ATM, code would be
 * needed to sense insertion of a card into the slot and notify the ATM -
 * simulated in this case by a button in the GUI
 */
public class CardReader implements ICardReader {
  /**
   * The ATM to which this card reader belongs
   */
  private ATM atm;

  /**
   * Constructor
   *
   * @param edu
   *          .gordon.atm the ATM that owns this card reader
   */
  public CardReader(ATM atm) {
    this.atm = atm;
  }

  @Override
  public void ejectCard() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public Card readCard() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public void retainCard() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }
}
