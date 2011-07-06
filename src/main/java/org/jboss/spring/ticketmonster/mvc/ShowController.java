package org.jboss.spring.ticketmonster.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.ShowTime;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller that retrieves all shows specified by a given Event id and Venue id.
 * 
 * @authors Marius Bogoevici, Ryan Bradley
 */

@Controller
public class ShowController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ShowDao showDao;

    @RequestMapping(value = "/shows.htm", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<ShowTime> getShowTimes(Long eventId, Long venueId) {
        logger.info("Retrieving show times for event " + eventId + " at venue " + venueId);
        List<ShowTime> showTimes = showDao.getShowTimes(eventId, venueId);
        
        return showTimes;
    }
    
	@RequestMapping(value= "/categories.htm", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<PriceCategory> getCategories(Long eventId, Long venueId) {
		logger.info("Retrieving all price categories for shows with " + eventId + "and " + venueId + ".");
		List<PriceCategory> categories = showDao.getCategories(eventId, venueId);
		return categories;
	}    
}
