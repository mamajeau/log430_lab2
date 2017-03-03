package edu.gordon.atm.ui.interfaces;

import java.util.EventObject;

public class NewStatusEvent extends EventObject {

	private Status status;
	
	public NewStatusEvent(Object source, Status newStatus) {
		super(source);
		status = newStatus;
	}

	public Status getStatus() {
		return status;
	}
}
