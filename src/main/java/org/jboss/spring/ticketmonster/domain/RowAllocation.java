package org.jboss.spring.ticketmonster.domain;

import java.util.LinkedList;

/**
 * Class that represents a list of SeatBlock objects, for cache look-up and seat allocation purposes.
 * 
 * @author Ryan Bradley
 *
 */

public class RowAllocation {
	
	private LinkedList<SeatBlock> allocatedSeats;

	public LinkedList<SeatBlock> getAllocatedSeats() {
		return allocatedSeats;
	}

	public void setAllocatedSeats(LinkedList<SeatBlock> allocatedSeats) {
		this.allocatedSeats = allocatedSeats;
	}

}
