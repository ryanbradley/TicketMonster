package org.jboss.spring.ticketmonster.service;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.BookingState;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.SeatBlock;

/**
 * Interface for persisting seat reservations to the database, and finalizing a reservation once it is purchased.
 * 
 * @author Ryan Bradley
 *
 */

public interface AllocationManager {
	
	BookingState getBookingState();

	List<Allocation> finalizeReservations(List<SeatBlock> reservations);
	
	Allocation createAllocation(SeatBlock block);
	
	void persistChanges(SeatBlock block);
	
	Double calculateTotal(List<PriceCategoryRequest> categoryRequests);
	
}
