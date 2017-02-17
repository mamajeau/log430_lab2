/* ATM Example system - file SimDisplay.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.util.StringTokenizer;

import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.transaction.Transaction.Cancelled;
import edu.gordon.atm.ui.interfaces.ICustomerConsole;

/**
 * Simulate the display portion of the customer console.
 */
class SimCustomerConsole extends Panel implements ICustomerConsole {
  /**
   *
   */
  private static final long serialVersionUID = -5902210256838725714L;
  private Simulation sim;
  /**
   * Individual lines comprising the display
   */
  private Label[] displayLine;
  /**
   * Number of the current line to write to
   */
  private int currentDisplayLine;

  /**
   * Constructor
   */
  SimCustomerConsole(Simulation simulation) {
    this.sim = simulation;
    setLayout(new GridLayout(ATMPanel.DISPLAYABLE_LINES, 1));
    setBackground(new Color(0, 96, 0)); // Dark green
    setForeground(Color.white);
    Font lineFont = new Font("Monospaced", Font.PLAIN, 14);
    displayLine = new Label[ATMPanel.DISPLAYABLE_LINES];
    for (int i = 0; i < ATMPanel.DISPLAYABLE_LINES; i++) {
      displayLine[i] = new Label(ATMPanel.BLANK_DISPLAY_LINE);
      displayLine[i].setFont(lineFont);
      add(displayLine[i]);
    }
    currentDisplayLine = 0;
  }

  @Override
  public void clearDisplay() {
    for (int i = 0; i < displayLine.length; i++) {
      displayLine[i].setText("");
    }
    currentDisplayLine = 0;
  }

  @Override
  public void display(String text) {
    // clearDisplay();
    StringTokenizer tokenizer = new StringTokenizer(text, "\n", false);
    while (tokenizer.hasMoreTokens()) {
      try {
        displayLine[currentDisplayLine++].setText(tokenizer.nextToken());
      } catch (Exception e) {
      }
    }
  }

  /**
   * Override the getInsets() method to provide additional spacing all around
   */
  @Override
  public Insets getInsets() {
    Insets insets = super.getInsets();
    insets.top += 5;
    insets.bottom += 5;
    insets.left += 10;
    insets.right += 10;
    return insets;
  }

  @Override
  public Money readAmount(String prompt) throws Cancelled {
    clearDisplay();
    display(prompt);
    display("");
    String input = sim.readInput(Simulation.AMOUNT_MODE, 0);
    clearDisplay();
    if (input == null) {
      throw new Cancelled();
    } else {
      int dollars = Integer.parseInt(input) / 100;
      int cents = Integer.parseInt(input) % 100;
      return new Money(dollars, cents);
    }
  }

  @Override
  public synchronized int readMenuChoice(String prompt, String[] menu)
      throws Cancelled {
    clearDisplay();
    display(prompt);
    for (int i = 0; i < menu.length; i++) {
      display((i + 1) + ") " + menu[i]);
    }
    String input = sim.readInput(Simulation.MENU_MODE, menu.length);
    clearDisplay();
    if (input == null) {
      throw new Cancelled();
    } else {
      return Integer.parseInt(input) - 1;
    }
  }

  @Override
  public int readPIN(String prompt) throws Cancelled {
    clearDisplay();
    display(prompt);
    display("");
    String input = sim.readInput(Simulation.PIN_MODE, 0);
    clearDisplay();
    if (input == null) {
      throw new Cancelled();
    } else {
      return Integer.parseInt(input);
    }
  }

  /**
   * Set echoed input on the display
   *
   * @param echo
   *          the line to be echoed - always the entire line
   */
  void setEcho(String echo) {
    displayLine[currentDisplayLine]
        .setText(ATMPanel.BLANK_DISPLAY_LINE.substring(0,
            ATMPanel.BLANK_DISPLAY_LINE.length() / 2 - echo.length()) + echo);
  }
}
