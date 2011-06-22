package org.jboss.spring.ticketmonster.domain;

import java.util.List;
import java.util.Map;

/**
 * Details about an event including description, venues hosting the event, and various show dates.
 * 
 * @author Ryan Bradley
 * 
 */

public class EventDetail {

	private Event event;
	private List<Venue> venues;
	private Map<Venue, List<Show>> shows;

	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public List<Venue> getVenues() {
		return venues;
	}
	public void setVenues(List<Venue> venues) {
		this.venues = venues;
	}
	public Map<Venue, List<Show>> getShows() {
		return shows;
	}
	public void setShows(Map<Venue, List<Show>> shows) {
		this.shows = shows;
	}	
}
