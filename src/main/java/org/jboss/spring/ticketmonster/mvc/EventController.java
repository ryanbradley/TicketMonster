package org.jboss.spring.ticketmonster.mvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.repo.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Web controller for all 'Event' related stories, such as displaying all events, searching for events using parameters like 'major',
 * 'category', 'start date' and 'end date', as well as displaying specific event details.
 * 
 * @author Ryan Bradley
 *
 */

@Controller
@RequestMapping("/events")
public class EventController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private EventDao eventDao;
	
	@RequestMapping(method=RequestMethod.GET)
	public @ModelAttribute("events")
	List<Event> displayEvents(HttpServletRequest request) {
		logger.info("Searching URL for any search parameters, such as category, date, major");
		
		List<Event> events = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String majorString = request.getParameter("major");
		String fromDate = request.getParameter("from");
		String endDate = request.getParameter("until");
		String categoryString = request.getParameter("category");
		
		if(fromDate != null && endDate != null) {
			logger.info("Found date parameters, from: " + fromDate + " and until: " + endDate + ".");
			Date from = null, until = null;
			
			try {
				from = dateFormat.parse(fromDate);
				until = dateFormat.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
			events = eventDao.searchDate(from, until);
			logger.info("Returning all events occurring between " + fromDate + " and " + endDate + ".");			
		}
		
		else if(categoryString != null) {
			logger.info("Found category parameter, " + categoryString + ".");
			int id = categoryString.charAt(0) - '0';
			Long categoryId = (long) id;
			events = eventDao.searchCategory(categoryId);
			logger.info("Returning all events with a category ID of " + categoryString + ".");			
		}
		
		else if(majorString != null) {
			logger.info("Found 'major' parameter, " + majorString + ".");
			boolean major = majorString.equalsIgnoreCase("true");
			events = eventDao.searchMajor(major);
			logger.info("Returning all events with the major field marked as" + majorString);			
		}
		
		else {
			logger.info("Retrieving all events currently in the database.");
			events = eventDao.getEvents();
		}
		
		return events;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String viewEvent(@PathVariable("id") Long id, Model model) {
		logger.info("Retrieving the event specified by the ID token: " + id.toString() + ".");
		Event event = eventDao.getEvent(id);
		model.addAttribute("event", event);
		logger.info("Retrieving the venues where the event specified by the ID token: " + id.toString() + " is playing.");
		List<Venue> venues = eventDao.getVenues(event);
		model.addAttribute("venues", venues);
		return "eventDetails";
	}

}
