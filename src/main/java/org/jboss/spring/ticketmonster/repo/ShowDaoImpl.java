package org.jboss.spring.ticketmonster.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.domain.VenueLayout;
import org.jboss.spring.ticketmonster.mvc.ShowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ShowDaoImpl implements ShowDao {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private VenueDao venueDao;

	@SuppressWarnings("unchecked")
	public List<ShowTime> getShowTimes(Long eventId, Long venueId) {
		Event event = eventDao.getEvent(eventId);
		Venue venue = venueDao.getVenue(venueId);
		
		Query query = entityManager.createQuery("select s from Show s where s.event = :event and s.venue = :venue");
		query.setParameter("event", event);
		query.setParameter("venue", venue);
		List<Show> shows = query.getResultList();
		
		List<ShowTime> showTimes = new ArrayList<ShowTime>();
		for(Show s : shows) {
            ShowTime showTime = new ShowTime();
            showTime.setShowId(s.getId());
            showTime.setDate(s.getShowDate());
            showTimes.add(showTime);
		}
		
		return showTimes;
	}

	public Show getShow(Long showId) {
		Show show = entityManager.find(Show.class, showId);
		return show;
	}

	public VenueLayout getVenueLayout(Long showId) {
		Show show = this.getShow(showId);
		VenueLayout layout = show.getVenueLayout();
		return layout;
	}

}
