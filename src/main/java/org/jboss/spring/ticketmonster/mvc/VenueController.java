package org.jboss.spring.ticketmonster.mvc;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.repo.VenueDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * A web controller for 'Venue' related stories, such as the display of all venues in the database, 
 * 
 * @author Ryan Bradley
 * 
 */

@Controller
@RequestMapping("/venues")
public class VenueController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private VenueDao venueDao;
	
	@RequestMapping(method=RequestMethod.GET)
	public @ModelAttribute("venues")
	List<Venue> displayVenues() {
		logger.info("Searching the database for all venues where an event is being hosted.");
		List<Venue> venues = venueDao.getVenues();
		logger.info("Returning all venues currently stored in the database.");
		return venues;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String viewVenue(@PathVariable("id") Long id, Model model) {
		logger.info("Retrieving the venue specified by the ID token in the url: " + id.toString() + ".");
		Venue venue = venueDao.getVenue(id);
		model.addAttribute("venue", venue);
		logger.info("Retrieving the events playing at the  venue with ID token of " + id.toString() + ".");
		List<Event> events = venueDao.getEvents(venue);
		model.addAttribute("events", events);
		return "venueDetails";
	}
	

}
