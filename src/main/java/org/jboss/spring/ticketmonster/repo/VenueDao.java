package org.jboss.spring.ticketmonster.repo;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface for methods relating Venue story data access.
 * 
 * @author Ryan Bradley
 *
 */

@Transactional
public interface VenueDao {

	List<Venue> getVenues();
	
	Venue getVenue(Long id);
	
	List<Event> getEvents(Venue venue);
}
