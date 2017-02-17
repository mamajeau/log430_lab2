// Copyright ÉTS_AUT2016_LOG430. All rights reserved.
// Toute réutilisation de ce code source est interdite et sera considérée
// comme une infraction au sens de la résolution CA-331-3487:
// - Règlement sur les infractions de nature académique.

package edu.gordon.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.gordon.atm.banking.Balances;
import edu.gordon.atm.transaction.Card;
import edu.gordon.atm.transaction.Deposit;
import edu.gordon.atm.transaction.Inquiry;
import edu.gordon.atm.transaction.Message;
import edu.gordon.atm.transaction.Money;
import edu.gordon.atm.transaction.Transfer;
import edu.gordon.atm.transaction.Withdrawal;
import edu.gordon.atm.ui.interfaces.Status;
import edu.gordon.simulation.SimulatedBank;

/**
 * Test case of the 4 core functionalities.
 * 
 * @author benoit
 */
public class AtmTestCase {
  private int serialNb = 1;
  private SimulatedBank simBank;
  private Balances balances;
  private Money cash;
  private Card card;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    simBank = new SimulatedBank();
    card = new Card(1);
    cash = new Money(20);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testBalanceInquiry() {
    Inquiry transact = new Inquiry(card, 42);
    balances = transact.getBalance();
    Message msg = new Message(Message.INQUIRY, card, 42, serialNb++, 0, -1,
        cash);
    Status status = simBank.handleMessage(msg, transact);
    assertTrue("Operation failed", status.isSuccess());
    assertEquals("Available balance is incorrect", 100,
        (int) balances.getAvailable() / 100);
    assertEquals("Total balance is incorrect", 100,
        (int) balances.getTotal() / 100);
  }

  @Test
  public void testDeposit() {
    Deposit transact = new Deposit(card, 42);
    balances = transact.getBalance();
    Message msg = new Message(Message.INITIATE_DEPOSIT, card, 42, serialNb++,
        -1, 0, cash);
    Status status = simBank.handleMessage(msg, transact);
    assertTrue("Operation failed", status.isSuccess());
    msg = new Message(Message.COMPLETE_DEPOSIT, card, 42, serialNb++, -1, 0,
        cash);
    status = simBank.handleMessage(msg, transact);
    assertTrue("Operation failed", status.isSuccess());
    assertEquals("Available balance is incorrect", 100,
        (int) balances.getAvailable() / 100);
    assertEquals("Total balance is incorrect", 120,
        (int) balances.getTotal() / 100);
  }

  @Test
  public void testTransfer() {
    Transfer transact = new Transfer(card, 42);
    balances = transact.getBalance();
    Message msg = new Message(Message.TRANSFER, card, 42, serialNb++, 0, 1,
        cash);
    Status status = simBank.handleMessage(msg, transact);
    assertTrue("Operation failed", status.isSuccess());
    assertEquals("Available balance is incorrect", 1020,
        (int) balances.getAvailable() / 100);
    assertEquals("Total balance is incorrect", 1020,
        (int) balances.getTotal() / 100);
  }

  @Test
  public void testWithdrawal() {
    Withdrawal transact = new Withdrawal(card, 42);
    balances = transact.getBalance();
    Message msg = new Message(Message.WITHDRAWAL, card, 42, serialNb++, 0, -1,
        cash);
    Status status = simBank.handleMessage(msg, transact);
    assertTrue("Operation failed", status.isSuccess());
    assertEquals(80, (int) balances.getAvailable() / 100);
    assertEquals(80, (int) balances.getTotal() / 100);
  }
}
