package org.jboss.spring.ticketmonster.action;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.repo.EventDetailDao;
import org.springframework.beans.factory.annotation.Autowired;

public class EventDetailDaoImpl implements EventDetailDao {

	@Autowired
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Event> getEvents() {
		List<Event> events = entityManager.createQuery("select e from Event e").getResultList();
		return events;
	}
	
	@SuppressWarnings("unchecked")
	public List<Event> searchCategory(Long categoryId) {
			Query query = entityManager.createQuery("select e from Event e where e.category.id = :categoryId");
			query.setParameter("categoryId", categoryId);
			List<Event> events = query.getResultList();
			return events;
	}

	@SuppressWarnings("unchecked")
	public List<Event> searchMajor(boolean major) {
		Query query = entityManager.createQuery("select e from Event e where e.major = :major");
		query.setParameter("major", major);
		List<Event> events = query.getResultList();
		return events;
	}

	public List<Event> searchDate(Date startDate, Date endDate) {
		List<Event> events = this.getEvents();
		
		for(Event e : events) {
			if(!(e.getStartDate().after(startDate)) && (e.getEndDate().before(endDate)))
				events.remove(e);
		}
		return events;
	}

	public Event getEvent(Long id) {
		return entityManager.find(Event.class, id);
	}
}
