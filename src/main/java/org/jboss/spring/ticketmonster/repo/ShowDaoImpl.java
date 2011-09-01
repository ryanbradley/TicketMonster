package org.jboss.spring.ticketmonster.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.ShowTime;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.springframework.beans.factory.annotation.Autowired;

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
	public List<PriceCategory> getCategories(Long eventId, Long venueId) {
		List<PriceCategory> categories = new ArrayList<PriceCategory>();
		Event event = eventDao.getEvent(eventId);
		Venue venue = venueDao.getVenue(venueId);

		Query query = entityManager.createQuery("select p from PriceCategory p where p.event = :event and p.venue = :venue order by p.section.id");
		query.setParameter("event", event);
		query.setParameter("venue", venue);
		
		categories = query.getResultList();
		return categories;
	}

	public Section getSectionByPriceCategory(Long categoryId) {
		PriceCategory category = entityManager.find(PriceCategory.class, categoryId);
		Section section = category.getSection();
		return section;
	}
	
	public Section findSection(Long sectionId) {
		Section section = entityManager.find(Section.class, sectionId);
		return section;
	}
	
	public SectionRow findSectionRow(Long rowId) {
		SectionRow row = entityManager.find(SectionRow.class, rowId);
		return row;
	}
	
	public PriceCategory findPriceCategory(Long categoryId) {
		PriceCategory category = entityManager.find(PriceCategory.class, categoryId);
		return category;
	}
	
	public Long getSectionIdByRowId(Long rowId) {
		SectionRow row = entityManager.find(SectionRow.class, rowId);
		return row.getSection().getId();		
	}

	@SuppressWarnings("unchecked")
	public List<SectionRow> getRowsBySection(Section section, int quantity) {
		Query query = entityManager.createQuery("select r from SectionRow r where r.section = :section and r.capacity >= :quantity");
		query.setParameter("section", section);
		query.setParameter("quantity", quantity);
		List<SectionRow> rows = query.getResultList();
		return rows;
	}

}
