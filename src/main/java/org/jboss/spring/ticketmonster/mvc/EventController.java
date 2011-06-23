package org.jboss.spring.ticketmonster.mvc;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/events.htm")
public class EventController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET)
	public @ModelAttribute("events")
	List<Event> getEvents() {
		logger.info("Getting events by querying database with an EntityManager bean.");
		List<Event> events = entityManager.createQuery("select e from Event e").getResultList();
		logger.info("Returning List object of all events currently in the database.");
		return events;
	}
}
