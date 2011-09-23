package org.jboss.spring.ticketmonster.repo;

import java.util.Date;
import java.util.List;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Venue;

/**
 * Interface for methods relating to Event story data access.
 * 
 * @author Ryan Bradley
 *
 */

public interface EventDao {
	
	public List<Event> getEvents();
	
	public List<Event> searchCategory(Long categoryId);
	
	public List<Event> searchMajor(boolean major);
	
	public List<Event> searchDate(Date startDate, Date endDate);
	
	public List<Venue> getVenues(Event event);
	
	public Event getEvent(Long id);
}
