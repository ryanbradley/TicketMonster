package org.jboss.spring.ticketmonster.service;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.SectionRequest;

/**
 * Interface containing methods to reserve seating that has been allocated.
 * 
 * @author Ryan Bradley
 *
 */

public interface ReservationManager {
	
	List<SectionRequest> createSectionRequests(BookingRequest booking);

	List<Allocation> reserveSeats(List<SectionRequest> sectionRequests);
	
	Allocation findContiguousSeats(SectionRequest sectionRequest);

	Allocation createAllocation(Long showId, Long priceCategoryId, int quantity);
	
	Allocation populateAllocation(int quantity, SeatBlock block);
	
	Allocation updateAllocation(Allocation allocation, int quantity);
}
