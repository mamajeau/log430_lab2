
package edu.gordon.atm.ui.interfaces;

import edu.gordon.atm.transaction.Card;

/**
 * Manager for the ATM's card reader. In a real ATM, this would manage a
 * physical device; in this edu.gordon.simulation, it uses classes in package
 * edu.gordon.simulation to simulate the device.
 */
public interface ICardReader {
  /**
   * Eject the card that is currently inside the reader.
   */
  public void ejectCard();

  /**
   * Read a card that has been partially inserted into the reader
   *
   * @return Card object representing information on the card if read
   *         successfully, null if not read successfully
   */
  public Card readCard();

  /**
   * Retain the card that is currently inside the reader for action by the bank.
   */
  public void retainCard();
}
