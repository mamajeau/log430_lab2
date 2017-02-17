/* ATM Example system - file LogPanel.java copyright (c) 2001 - Russell C. Bjork */

package edu.gordon.simulation;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The GUI panel that displays the ATM's internal log.
 */
class LogPanel extends Panel {
  /**
   *
   */
  private static final long serialVersionUID = 6736220889351024850L;
  /**
   * Area into which the log is to be printed
   */
  private TextArea logPrintArea;

  /**
   * Constructor
   *
   * @param gui
   *          the the overall GUI
   */
  LogPanel(final GUI gui) {
    GridBagLayout logLayout = new GridBagLayout();
    setLayout(logLayout);
    setFont(new Font("Monospaced", Font.PLAIN, 14));
    Label logPanelLabel = new Label("Log", Label.CENTER);
    add(logPanelLabel);
    GridBagConstraints constraints = GUI.makeConstraints(0, 0, 1, 1,
        GridBagConstraints.NONE);
    constraints.weighty = 0;
    logLayout.setConstraints(logPanelLabel, constraints);
    logPrintArea = new TextArea();
    logPrintArea.setBackground(Color.white);
    logPrintArea.setForeground(Color.black);
    logPrintArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    logPrintArea.setEditable(false);
    add(logPrintArea);
    constraints = GUI.makeConstraints(1, 0, 1, 1, GridBagConstraints.BOTH);
    constraints.weighty = 1;
    logLayout.setConstraints(logPrintArea, constraints);
    Panel logButtonPanel = new Panel();
    logButtonPanel.setLayout(new FlowLayout());
    Button clearLogButton = new Button("Clear Log");
    clearLogButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        logPrintArea.setText("");
      }
    });
    logButtonPanel.add(clearLogButton);
    Button dismissLogButton = new Button(" Hide Log ");
    dismissLogButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gui.showCard("ATM");
      }
    });
    logButtonPanel.add(dismissLogButton);
    add(logButtonPanel);
    constraints = GUI.makeConstraints(2, 0, 1, 1, GridBagConstraints.NONE);
    constraints.weighty = 0;
    logLayout.setConstraints(logButtonPanel, constraints);
  }

  /**
   * Add text to the log
   *
   * @param text
   *          the text to be printed
   */
  void println(String text) {
    logPrintArea.append(text + "\n");
  }
}
