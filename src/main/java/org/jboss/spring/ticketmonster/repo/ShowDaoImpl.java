package org.jboss.spring.ticketmonster.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.ShowTime;
import org.jboss.spring.ticketmonster.domain.TicketCategory;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.domain.VenueLayout;
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

	@SuppressWarnings("unchecked")
	public List<TicketCategory> getCategories(Long showId) {
		List<TicketCategory> categories = new ArrayList<TicketCategory>();
		Show show = this.getShow(showId);
		VenueLayout layout = show.getVenueLayout();
		
		Query q1 = entityManager.createQuery("select s from Section s where s.layout = :layout");
		q1.setParameter("layout", layout);
		List<Section> sections = q1.getResultList();
		
		Query q2 = entityManager.createQuery("select p from PriceCategory p where p.section = :section");
		
		for(Section section : sections) {
			q2.setParameter("section", section);
			List<PriceCategory> priceCategories = q2.getResultList();
			for(PriceCategory category : priceCategories) {
				if(!categories.contains(category.getCategory()))
					categories.add(category.getCategory());
			}
		}
		
		return categories;
	}

}
