package org.jboss.spring.ticketmonster.mvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.repo.EventDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/events")
public class EventController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private EventDetailDao eventDetailDao;
	
	@RequestMapping(method=RequestMethod.GET)
	public @ModelAttribute("events")
	List<Event> displayEvents(HttpServletRequest request) {
		logger.info("Searching URL for any search parameters, such as category, date, major");
		
		List<Event> events = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		String majorString = request.getParameter("major");
		String fromDate = request.getParameter("from");
		String endDate = request.getParameter("until");
		String categoryString = request.getParameter("category");
		
		if(fromDate != null && endDate != null) {
			logger.info("Found date parameters, from: " + fromDate + " and until: " + endDate + ".");
			Date from = new Date(), until = new Date();
			
			try {
				from = dateFormat.parse(fromDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				until = dateFormat.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			events = eventDetailDao.searchDate(from, until);
			logger.info("Returning all events occurring between " + fromDate + " and " + endDate + ".");			
		}
		
		else if(categoryString != null) {
			logger.info("Found category parameter, " + categoryString + ".");
			int id = categoryString.charAt(0) - '0';
			Long categoryId = (long) id;
			events = eventDetailDao.searchCategory(categoryId);
			logger.info("Returning all events with a category ID of " + categoryString + ".");			
		}
		
		else if(majorString != null) {
			logger.info("Found 'major' parameter, " + majorString + ".");
			boolean major = majorString.equalsIgnoreCase("true");
			events = eventDetailDao.searchMajor(major);
			logger.info("Returning all events with the major field marked as" + majorString);			
		}
		
		else {
			logger.info("Retrieving all events currently in the database.");
			events = eventDetailDao.getEvents();
		}
		
		return events;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String viewEvent(@PathVariable("id") Long id, Model model) {
		logger.info("Retrieving the event specified by the ID token: " + id.toString() + ".");
		Event event = eventDetailDao.getEvent(id);
		model.addAttribute("event", event);
		return "eventDetails";
	}

}
