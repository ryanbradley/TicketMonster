package org.jboss.spring.ticketmonster.repo;

import java.util.Date;
import java.util.List;

import org.jboss.spring.ticketmonster.domain.Event;

public interface EventDetailDao {
	
	public List<Event> getEvents();
	
	public List<Event> searchCategory(Long categoryId);
	
	public List<Event> searchMajor(boolean major);
	
	public List<Event> searchDate(Date startDate, Date endDate);
	
	public Event getEvent(Long id);

}
