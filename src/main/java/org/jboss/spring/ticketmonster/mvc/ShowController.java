package org.jboss.spring.ticketmonster.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.VenueLayout;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller that retrieves all shows specified by a given Event id and Venue id.
 * 
 * @author Marius Bogoevici
 */

@Controller
public class ShowController {

    protected final Log logger = LogFactory.getLog(getClass());

/*    @Autowired
    private EventDao eventDao;

    @Autowired
    private VenueDao venueDao;
*/
    @Autowired
    private ShowDao showDao;

    @RequestMapping(value = "/shows.htm", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<ShowTime> getShowTimes(Long eventId, Long venueId) {
        logger.info("Retrieving show times for event " + eventId + " at venue " + venueId);
        List<ShowTime> showTimes = new ArrayList<ShowTime>(); 
        showTimes = showDao.getShowTimes(eventId, venueId);
        
        return showTimes;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody VenueLayout getVenueLayoutName(Show show) {
    	
    	return show.getVenueLayout();
    }
}
