package edu.gordon.atm.ui.interfaces;

import com.google.common.eventbus.Subscribe;

public class StatusComponent {

	private Status lastStatus = null;
	
	@Subscribe
	public void handleEvent(NewStatusEvent evt) {
		lastStatus = evt.getStatus();
	}
	
	public Status getLastStatus() {
		return lastStatus;
	}
}
