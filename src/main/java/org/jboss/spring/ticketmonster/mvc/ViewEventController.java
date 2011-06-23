package org.jboss.spring.ticketmonster.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.action.EventDetailDaoImpl;
import org.jboss.spring.ticketmonster.domain.EventDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller to resolve the web view for the 'View Event Details' use case.
 * 
 * @author Ryan Bradley
 *
 */

/*@Controller
@RequestMapping("/events")*/
public class ViewEventController {
	protected final Log logger = LogFactory.getLog(getClass());
	
	//@Autowired
	private EventDetailDaoImpl eventDetailDao;
	
	//@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ModelAttribute("eventDetail")
	EventDetail getEventDetails(@PathVariable("id") Long id) {
		EventDetail eventDetail = eventDetailDao.getEventDetail(id);
		return eventDetail;
	}	
}
