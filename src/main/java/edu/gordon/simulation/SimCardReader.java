/* ATM Example system - file SimCardReader.java copyright (c) 2001 - Russell C.
 * Bjork */

package edu.gordon.simulation;

import java.awt.Button;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.gordon.atm.transaction.Card;
import edu.gordon.atm.ui.interfaces.ICardReader;

/**
 * Simulate the card reader.
 */
public class SimCardReader extends Button implements ICardReader {
  /**
   *
   */
  private static final long serialVersionUID = 3220654279067821853L;
  private Simulation sim;
  /**
   * To animate card insertion/ejection, we change the bounds of this button.
   * These are the original bounds we ultimately restore to
   */
  private Rectangle originalBounds;

  /**
   * Constructor
   *
   * @param edu.gordon.simulation
   *          the Simulation object
   */
  public SimCardReader(final Simulation simulation) {
    super("Click to insert card");
    this.sim = simulation;
    addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        simulation.cardInserted();
      }
    });
    // Not available until machine is turned on
    setVisible(false);
  }

  /**
   * Animate ejecting the card that is currently inside the reader.
   */
  public void animateEjection() {
    setLabel("Ejecting card");
    setVisible(true);
    Rectangle currentBounds = new Rectangle(
        originalBounds.x + originalBounds.width / 2,
        originalBounds.y + originalBounds.height / 2,
        originalBounds.width / originalBounds.height, 1);
    while (currentBounds.height <= originalBounds.height
        && currentBounds.width <= originalBounds.width) {
      setBounds(currentBounds.x, currentBounds.y, currentBounds.width,
          currentBounds.height);
      repaint();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
      currentBounds.height += 1;
      currentBounds.width = (originalBounds.width * currentBounds.height)
          / originalBounds.height;
      currentBounds.x = originalBounds.x
          + (originalBounds.width - currentBounds.width) / 2;
      currentBounds.y = originalBounds.y
          + (originalBounds.height - currentBounds.height) / 2;
    }
    setLabel("Click to insert card");
  }

  /**
   * Animate card going into the machine
   */
  public void animateInsertion() {
    originalBounds = getBounds();
    Rectangle currentBounds = new Rectangle(originalBounds.x, originalBounds.y,
        originalBounds.width, originalBounds.height);
    while (currentBounds.width > 0 && currentBounds.height > 0) {
      setBounds(currentBounds.x, currentBounds.y, currentBounds.width,
          currentBounds.height);
      repaint();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
      currentBounds.height -= 1;
      currentBounds.width = (originalBounds.width * currentBounds.height)
          / originalBounds.height;
      currentBounds.x = originalBounds.x
          + (originalBounds.width - currentBounds.width) / 2;
      currentBounds.y = originalBounds.y
          + (originalBounds.height - currentBounds.height) / 2;
    }
    setVisible(false);
  }

  /**
   * Animate retaining the card that is currently inside the reader for action
   * by the bank.
   */
  void animateRetention() {
    setLabel("Click to insert card");
    setVisible(true);
  }

  @Override
  public void ejectCard() {
    animateEjection();
    // Re-enable on-off switch
    sim.getOperatorPanel().setEnabled(true);
  }

  @Override
  public Card readCard() {
    // Machine can't be turned off while there is a card in it
    sim.getOperatorPanel().setEnabled(false);
    animateInsertion();
    // Since we don't have a magnetic stripe reader, we'll simulate by
    // having customer type the card number in
    return sim.getGUI().readCard();
  }

  @Override
  public void retainCard() {
    animateRetention();
    // Re-enable on-off switch
    sim.getOperatorPanel().setEnabled(true);
  }
}
