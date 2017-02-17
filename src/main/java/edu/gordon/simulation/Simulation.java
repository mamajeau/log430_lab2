/* ATM Example system - file Simulation.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.simulation;

import java.net.InetAddress;

import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.transaction.Transaction;
import edu.gordon.atm.ui.ATM;
import edu.gordon.atm.ui.interfaces.Status;

/**
 * Simulation of the physical components of the ATM, including its network
 * connection to the bank. An instance is created at startup by either the
 * application's main() program or the applet's init() method. The individual
 * components are displayed in a panel belonging to class GUI. The bank is
 * simulated by an object belonging to class SimulatedBank. The constructor for
 * this class creates one instance of each. The methods simulate specific
 * operations of the ATM, and are forwarded to either the GUI panel or the
 * simulated bank to actually carry them out.
 */
public class Simulation extends ATM {
  /**
   * Read input in PIN mode - allow user to enter several characters, and to
   * clear the line if the user wishes; echo as asterisks
   */
  public static final int PIN_MODE = 1;
  /**
   * Read input in amount mode - allow user to enter several characters, and to
   * clear the line if the user wishes; echo what use types
   */
  public static final int AMOUNT_MODE = 2;
  /**
   * Read input in menu choice mode - wait for one digit key to be pressed, and
   * return value immediately.
   */
  public static final int MENU_MODE = 3;
  /**
   * The one and only instance of this class
   */
  private static Simulation theInstance;
  private SimKeyboard keyboard;
  /**
   * Panel containing the GUI that simulates the ATM
   */
  private GUI gui;
  /**
   * Simulated bank
   */
  private SimulatedBank simulatedBank;

  public Simulation(int id, String place, String bankName,
      InetAddress bankAddress) {
    super(id, place, bankName, bankAddress);
    gui = new GUI((SimOperatorPanel) operatorPanel, (SimCardReader) cardReader,
        (SimCustomerConsole) customerConsole, keyboard,
        (SimCashDispenser) cashDispenser,
        (SimEnvelopeAcceptor) envelopeAcceptor,
        (SimReceiptPrinter) receiptPrinter);
    // Create the edu.gordon.simulation of the bank
    simulatedBank = new SimulatedBank();
  }

  @Override
  public void createComponents() {
    // Create the simulated individual components of the ATM's GUI
    log = new SimLog(this);
    operatorPanel = new SimOperatorPanel(this);
    cardReader = new SimCardReader(this);
    customerConsole = new SimCustomerConsole(this);
    cashDispenser = new SimCashDispenser(log);
    envelopeAcceptor = new SimEnvelopeAcceptor(log);
    networkToBank = new SimNetworkToBank(log, bankAddress, this);
    receiptPrinter = new SimReceiptPrinter();
    keyboard = new SimKeyboard((SimCustomerConsole) customerConsole,
        (SimEnvelopeAcceptor) envelopeAcceptor);
  }

  /**
   * Accessor for GUI Panel that simulates the ATM
   *
   * @return the GUI Panel
   */
  public GUI getGUI() {
    return gui;
  }

  /**
   * Simulated getting initial amount of cash from operator
   *
   * @return value of initial cash entered
   */
  public Money getInitialCash() {
    return gui.getInitialCash();
  }

  /**
   * Accessor for simulated bank
   *
   * @return simulated bank
   */
  public SimulatedBank getSimulatedBank() {
    return simulatedBank;
  }

  /**
   * Simulate printing a line to the log
   *
   * @param text
   *          the line to print
   */
  public void printLogLine(String text) {
    gui.printLogLine(text);
  }

  /**
   * Simulate reading input from the keyboard
   *
   * @param mode
   *          the input mode to use - one of the constants defined below.
   * @param maxValue
   *          the maximum acceptable value (used in MENU_MODE only)
   * @return the line that was entered - null if user pressed CANCEL.
   */
  public String readInput(int mode, int maxValue) {
    return keyboard.readInput(mode, maxValue);
  }

  /**
   * Simulate sending a message to bank
   *
   * @param message
   *          the message to send
   * @param currTransact
   *          (out) balances in customer's account as reported by bank
   * @return status code returned by bank
   */
  public Status sendMessage(Message message, Transaction currTransact) {
    // Simulate time taken to send message over network
    try {
      Thread.sleep(2 * 1000);
    } catch (InterruptedException e) {
    }
    return simulatedBank.handleMessage(message, currTransact);
  }

  /**
   * Notify the ATM that the state of the on-off switch has been changed
   *
   * @param on
   *          true if state is now "on", false if it is "off"
   */
  void switchChanged(boolean on) {
    // The card reader is only enabled when the switch is on
    ((SimCardReader) cardReader).setVisible(on);
    if (on) {
      switchOn();
    } else {
      switchOff();
    }
  }
}
