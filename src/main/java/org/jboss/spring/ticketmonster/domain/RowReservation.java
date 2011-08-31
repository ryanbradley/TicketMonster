package org.jboss.spring.ticketmonster.domain;

import java.util.LinkedList;

/**
 * Class that represents a list of SeatBlock objects, for cache look-up and seat allocation purposes.
 * 
 * @author Ryan Bradley
 *
 */

public class RowReservation {
	
	private int capacity;
	
	private LinkedList<SeatBlock> reservedSeats;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public LinkedList<SeatBlock> getReservedSeats() {
		return reservedSeats;
	}

	public void setReservedSeats(LinkedList<SeatBlock> reservedSeats) {
		this.reservedSeats = reservedSeats;
	}

}
