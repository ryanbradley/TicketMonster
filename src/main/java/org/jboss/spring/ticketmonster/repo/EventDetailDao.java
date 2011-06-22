package org.jboss.spring.ticketmonster.repo;

import java.util.List;
import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.EventDetail;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.Venue;

/**
 * Interface for methods that retrieve event data for the 'view event' and 'book event' use cases.
 * 
 * @author Ryan Bradley
 *
 */

public interface EventDetailDao {
	
	public EventDetail getEventDetail(Long eventId);
	
	public Event getEvent(Long eventId);
	
	public List<Venue> getVenues(Event event);
	
	public List<Show> getShows(Long eventId, Long venueId);
	
	//public Map<Section, Availability> getAvailability(Long showId);
	
	//public boolean bookSeats(Long showId, Long sectionId, Map<Long, Integer> quantities);
}
