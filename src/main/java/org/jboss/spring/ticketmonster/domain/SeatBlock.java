package org.jboss.spring.ticketmonster.domain;

/**
 * Class representing a contiguous block of seats which has either been allocated or purchased.
 * 
 * @author Ryan Bradley
 *
 */

public class SeatBlock {

	private CacheKey key;
	
	private int startSeat;
	
	private int endSeat;
	
	private boolean status;

	public CacheKey getKey() {
		return key;
	}

	public void setKey(CacheKey key) {
		this.key = key;
	}

	public int getStartSeat() {
		return startSeat;
	}

	public void setStartSeat(int startSeat) {
		this.startSeat = startSeat;
	}

	public int getEndSeat() {
		return endSeat;
	}

	public void setEndSeat(int endSeat) {
		this.endSeat = endSeat;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
