package org.jboss.spring.ticketmonster.repo;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.Venue;

/**
 * Interface for methods relating Venue story data access.
 * 
 * @author Ryan Bradley
 *
 */

public interface VenueDao {

	List<Venue> getVenues();
	
	Venue getVenue(Long id);
	
	List<Event> getEvents(Venue venue);
	
	List<Show> getShows(Event event, Venue venue);
}
