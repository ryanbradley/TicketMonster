package org.jboss.spring.ticketmonster.service;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.BookingState;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface containing methods to reserve seating that has been allocated.
 * 
 * @author Ryan Bradley
 *
 */

@PreAuthorize("hasRole('ROLE_USER')")
@Transactional
public interface ReservationManager {
	
	BookingState getBookingState();
	
	List<SectionRequest> createSectionRequests(BookingRequest booking);
	
	boolean findContiguousSeats(Long showId, Long sectionId, int quantity);
	
	SeatBlock reserveSeats(SeatBlock frontBlock, int quantity, CacheKey key);
	
	boolean updateSeatReservation(Long showId, Long sectionId, int quantity);
	
	SeatBlock update(Long showId, Long rowId, int quantity);
	
	void removeSeatReservation(Long showId, Long sectionId);
	
	boolean checkAvailability(Long showId, Long sectionId, int quantity);

}
