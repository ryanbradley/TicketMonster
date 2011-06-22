package org.jboss.spring.ticketmonster.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.EventDetail;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.repo.EventDetailDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the EventDetailDao interface, a service designed to find the details pertaining to a certain event.
 * 
 * @author Ryan Bradley
 *
 */

public class EventDetailDaoImpl implements EventDetailDao {

	@Autowired
	EntityManager entityManager;
	
	private List<Venue> venues;
	
	public Event getEvent(Long eventId) {
		Event event = entityManager.find(Event.class, eventId);
		return event;
	}

	@SuppressWarnings("unchecked")
	public List<Venue> getVenues(Event event) {
		if(venues == null) {
			venues = new ArrayList<Venue>();
			
			for(Show show : (List<Show>) entityManager.createQuery(
					"select s from Show s where s.event = :event")
					.setParameter("event", event)
					.getResultList()) 
			{
				if(!venues.contains(show.getVenue())) {
					venues.add(show.getVenue());
				}
			}
		}
		return venues;
	}

	@SuppressWarnings("unchecked")
	public List<Show> getShows(Long eventId, Long venueId) {
		return entityManager.createQuery(
				"select s from Show s where s.eventId = :eventId and s.venueId = :venueId")
				.setParameter("eventId", eventId)
				.setParameter("venueId", venueId)
				.getResultList();
	}
	
	/* public Map<Section, Availability> getAvailability(Long showId) {
		Show show = entityManager.find(Show.class, showId);
		
		Map<Section, Availability> availability = new HashMap<Section, Availability>();
		
		List<PriceCategory> categories = entityManager.createQuery(
				"select pc from PriceCategory pc where pc.event = :event and pc.venue = :venue")
				.setParameter("event", show.getEvent())
				.setParameter("event", show.getVenue())
				.getResultList();
		
		return availability;
	
	public boolean bookSeats(Long sectionId, Long showId, Map<Long, Integer> quantities) {
		Show show = entityManager.find(Show.class, showId);
		Section section = entityManager.find(Section.class, sectionId);
		
		int quantity = 0;
		
		for(Long key : quantities.keySet()) {
			quantity += quantities.get(key);
		}
		
		Allocation allocation = null;
		// Allocation allocation = bookingManager.reserce(show, section, quantity);
		
		return allocation != null;
	} */

	public EventDetail getEventDetail(Long eventId) {
		EventDetail eventDetail = new EventDetail();
		eventDetail.setEvent(entityManager.find(Event.class, eventId));
		eventDetail.setVenues(this.getVenues(eventDetail.getEvent()));
		
		Map<Venue, List<Show>> shows = new HashMap<Venue, List<Show>>();
		
		for(Venue v : eventDetail.getVenues()) {
			shows.put(v, this.getShows(eventId, v.getId()));
		}
		
		eventDetail.setShows(shows);
		
		return eventDetail;
	}
}
