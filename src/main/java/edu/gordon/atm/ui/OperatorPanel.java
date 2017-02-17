/* ATM Example system - file OperatorPanel.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.atm.ui;

import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.ui.interfaces.IOperatorPanel;

/**
 * Physical Manager for the ATM's operator panel.
 */
public class OperatorPanel implements IOperatorPanel {
  /**
   * ATM this panel is part of. The ATM object will be notified when the switch
   * on the panel is turned on or off
   */
  private ATM atm;

  /**
   * Constructor
   *
   * @param edu.gordon.atm
   *          the ATM this panel is part of
   */
  public OperatorPanel(ATM atm) {
    this.atm = atm;
  }

  @Override
  public Money getInitialCash() {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }

  @Override
  public void setEnabled(boolean b) {
    throw new UnsupportedOperationException(
        "This feature is currently not supported because it required to communicate "
            + "with the physical ATM which we do not have yet");
  }
}
