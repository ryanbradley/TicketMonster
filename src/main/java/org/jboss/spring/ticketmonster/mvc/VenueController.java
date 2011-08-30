package org.jboss.spring.ticketmonster.mvc;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A web controller for 'Venue' related stories, such as the display of all venues in the database, 
 * 
 * @author Ryan Bradley
 * 
 */

@Controller
@RequestMapping("/venues")
public class VenueController {
	
	@Autowired
	private VenueDao venueDao;
	
	@RequestMapping(method=RequestMethod.GET)
	public @ModelAttribute("venues")
	List<Venue> displayVenues() {
		List<Venue> venues = venueDao.getVenues();
		return venues;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = {"text/html", "application/xhtml+xml"})
	public String viewVenue(@PathVariable("id") Long id, Model model) {
		Venue venue = venueDao.getVenue(id);
		model.addAttribute("venue", venue);
		List<Event> events = venueDao.getEvents(venue);
		model.addAttribute("events", events);
		return "venueDetails";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = "application/json")
	public @ResponseBody Venue viewVenueDetails(@PathVariable("id") Long id) {
		Venue venue = venueDao.getVenue(id);
		return venue;
	}
}
