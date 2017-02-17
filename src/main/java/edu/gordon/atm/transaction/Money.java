/* ATM Example system - file Money.java copyright (c) 2001 - Russell C. Bjork */

package edu.gordon.atm.transaction;

/** Representation for money amounts */
public class Money {
  /**
   * Instance variable: this amount represented as a number of cents
   */
  private long cents;

  /**
   * Constructor
   *
   * @param dollars
   *          whole dollar amount
   */
  public Money(int dollars) {
    this(dollars, 0);
  }

  /**
   * Constructor
   *
   * @param dollars
   *          dollar part of amount
   * @param cents
   *          cents part of amount
   */
  public Money(int dollars, int cents) {
    this.cents = 100L * dollars + cents;
  }

  /**
   * Copy constructor
   *
   * @param toCopy
   *          the Money object to copy
   */
  public Money(Money toCopy) {
    this.cents = toCopy.cents;
  }

  /**
   * Add an amount of money to this
   *
   * @param amountToAdd
   *          the amount to add
   */
  public void add(Money amountToAdd) {
    this.cents += amountToAdd.cents;
  }

  public long getCents() {
    return cents;
  }

  /**
   * Compare this to another amount
   *
   * @param compareTo
   *          the amount to compare to
   * @return true if this amount is <= compareTo amount
   */
  public boolean lessEqual(Money compareTo) {
    return this.cents <= compareTo.cents;
  }

  /**
   * Subtract an amount of money from this
   *
   * @param amountToSubtract
   *          the amount to subtract Precondition: amount must be <= this amount
   */
  public void subtract(Money amountToSubtract) {
    this.cents -= amountToSubtract.cents;
  }

  /**
   * Create a string representation of this amount
   *
   * @return string representation of this amount
   */
  @Override
  public String toString() {
    return "$"
        + cents / 100
        + (cents % 100 >= 10 ? "." + cents % 100 : ".0" + cents % 100);
  }
}
