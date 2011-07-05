package org.jboss.spring.ticketmonster.repo;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.ShowTime;
import org.jboss.spring.ticketmonster.domain.TicketCategory;

/**
 * Interface for Show-related database access.
 * 
 * @author Ryan Bradley
 *
 */

public interface ShowDao {

	List<ShowTime> getShowTimes(Long eventId, Long venueId);
	
	Show getShow(Long showId);
	
	List<TicketCategory> getCategories(Long showId);
}
