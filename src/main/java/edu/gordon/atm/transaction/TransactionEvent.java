package edu.gordon.atm.transaction;

import java.util.EventObject;

public class TransactionEvent extends EventObject {
	
	protected Message message;
	protected Transaction transaction;
	
	public TransactionEvent(Object source, Message message, Transaction transaction) {
		super(source);
		this.message = message;
		this.transaction = transaction;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}
}
