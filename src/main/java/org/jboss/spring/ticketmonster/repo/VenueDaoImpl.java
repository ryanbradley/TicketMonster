package org.jboss.spring.ticketmonster.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.repo.VenueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the VenueDao interface.
 * 
 * @author Ryan Bradley
 *
 */

@Transactional
public class VenueDaoImpl implements VenueDao {
	
	@Autowired
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Venue> getVenues() {
		List<Venue> venues = entityManager.createQuery("select v from Venue v").getResultList();
		return venues;
	}

	public Venue getVenue(Long id) {
		Venue venue = entityManager.find(Venue.class, id);
		return venue;
	}

	@SuppressWarnings("unchecked")
	public List<Event> getEvents(Venue venue) {
		List<Event> events = new ArrayList<Event>();
		Query query = entityManager.createQuery("select s from Show s where s.venue = :venue");
		query.setParameter("venue", venue);
		List<Show> shows = query.getResultList();
		
		for(Show show : shows) {
			if(!events.contains(show.getEvent()))
				events.add(show.getEvent());
		}
		return events;
	}

	@SuppressWarnings("unchecked")	
	public List<Show> getShows(Event event, Venue venue) {
		Query query = entityManager.createQuery("select s Show s from where s.event = :event and s.venue = :venue");
		query.setParameter("event", event);
		query.setParameter("venue", venue);
		List<Show> shows = query.getResultList();
		return shows;
	}
}
