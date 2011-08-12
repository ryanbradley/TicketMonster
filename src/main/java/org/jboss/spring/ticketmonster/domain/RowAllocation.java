package org.jboss.spring.ticketmonster.domain;

import java.util.LinkedList;

/**
 * Class that represents a list of SeatBlock objects, for cache look-up and seat allocation purposes.
 * 
 * @author Ryan Bradley
 *
 */

public class RowAllocation {
	
	private int capacity;
	
	private LinkedList<SeatBlock> allocatedSeats;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public LinkedList<SeatBlock> getAllocatedSeats() {
		return allocatedSeats;
	}

	public void setAllocatedSeats(LinkedList<SeatBlock> allocatedSeats) {
		this.allocatedSeats = allocatedSeats;
	}

}
