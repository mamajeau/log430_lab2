/* ATM Example system - file BillsPanel.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.simulation;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The GUI panel that allows the operator to enter the number of bills in the
 * ATM at startup.
 */
class BillsPanel extends Panel {
  /**
   *
   */
  private static final long serialVersionUID = -4853528442754541869L;
  /**
   * The field into which the number of bills is to be entered
   */
  private TextField billsNumberField;

  /**
   * Constructor
   */
  BillsPanel() {
    setLayout(new GridLayout(0, 1, 0, 0));
    setFont(new Font("Monospaced", Font.PLAIN, 14));
    add(new Label("A real ATM would have a mechanism to sense", Label.CENTER));
    add(new Label("or allow the operator to enter the number", Label.CENTER));
    add(new Label("of $20 bills in the cash dispenser.", Label.CENTER));
    add(new Label("For purposes of the edu.gordon.simulation,", Label.CENTER));
    add(new Label("please enter the number of $20 bills manually.",
        Label.CENTER));
    add(new Label("Then press RETURN", Label.CENTER));
    billsNumberField = new TextField(30);
    billsNumberField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        synchronized (BillsPanel.this) {
          BillsPanel.this.notify();
        }
      }
    });
    Panel billsNumberPanel = new Panel();
    billsNumberPanel.add(billsNumberField);
    add(billsNumberPanel);
  }

  /**
   * Ask the operator to enter the number of bills in the cash dispenser
   *
   * @return the number entered
   */
  synchronized int readBills() {
    boolean validNumberRead = false;
    int billsNumber = 0;
    billsNumberField.setText("");
    while (!validNumberRead) {
      billsNumberField.requestFocus();
      try {
        wait();
      } catch (InterruptedException e) {
      }
      try {
        billsNumber = Integer.parseInt(billsNumberField.getText());
        if (billsNumber >= 0) {
          validNumberRead = true;
        } else {
          getToolkit().beep();
        }
      } catch (NumberFormatException e) {
        getToolkit().beep();
      }
      if (!validNumberRead) {
        billsNumberField.setText("Must be a valid integer >= 0");
        billsNumberField.selectAll();
      }
    }
    return billsNumber;
  }
}
