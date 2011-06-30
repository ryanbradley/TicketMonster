package org.jboss.spring.ticketmonster.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.repo.EventDao;
import org.jboss.spring.ticketmonster.repo.VenueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marius Bogoevici
 */
@Controller
public class ShowController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private EventDao eventDao;

    @Autowired
    private VenueDao venueDao;

    @RequestMapping(value = "/shows", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<ShowTime> getShowTimes(Long eventId, Long venueId) {

        logger.info("Retrieving show times for event " + eventId + " at venue " + venueId);
        Event event = eventDao.getEvent(eventId);
        Venue venue = venueDao.getVenue(venueId);
        List<Show> shows = eventDao.getShows(event, venue);
        List<ShowTime> showTimes = new ArrayList<ShowTime>();
        for (Show show : shows) {
            ShowTime showTime = new ShowTime();
            showTime.setShowId(show.getId());
            showTime.setDate(show.getShowDate());
            showTimes.add(showTime);
        }
        return showTimes;
    }
}
