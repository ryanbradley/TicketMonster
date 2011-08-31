package org.jboss.spring.ticketmonster.domain;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect-oriented programming class to define various pointcuts and cross-cutting methods, such as logging, for the TicketMonster application.
 * 
 * @author Ryan Bradley
 *
 */

//@Aspect
public class LogInformation {
	
	protected final Log logger = LogFactory.getLog(getClass()); 
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.mvc.EventController.displayEvents(javax.servlet.http.HttpServletRequest)) && args(request)")
	public void displayEvents(HttpServletRequest request) {
	}
	
	@Pointcut(value = "execution(* org.jboss.spring.ticketmonster.mvc.EventController.viewEvent(Long)) && args(id)", argNames="id")
	public void viewEvent(Long id) {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.mvc.VenueController.displayVenues(..))")
	public void displayVenues() {
	}
	
	@Pointcut(value = "execution(* org.jboss.spring.ticketmonster.mvc.VenueController.viewVenue(Long)) && args(id)", argNames="id")
	public void viewVenue(Long id) {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.mvc.ShowController.getShowTimes(..))")
	public void showTimes() {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.mvc.ShowController.getCategories(..))")
	public void categories() {
	}

	@Pointcut(value = "execution(* org.jboss.spring.ticketmonster.mvc.BookingFormController.viewShow(Long)) && args(id)", argNames="id")
	public void show(Long id) {
	}

	@Pointcut(value = "execution(* org.jboss.spring.ticketmonster.service.ReservationManager.findContiguousSeats(Long, Long, int)) && args(showId, sectionId, quantity)", argNames="showId, sectionId, quantity")
	public void allocated(Long showId, Long sectionId, int quantity) {
	}
	
	@Pointcut(value = "execution(* org.jboss.spring.ticketmonster.service.ReservationManager.updateSeatAllocation(Long, Long, int)) && args(showId, sectionId, quantity)", argNames= "showId, sectionId, quantity")
	public void updated(Long showId, Long sectionId, int quantity) {
	}
	
	@AfterReturning("displayEvents(request)")
	public void eventDetails(HttpServletRequest request) {
		String majorString, categoryString, fromDate, untilDate;
		
		majorString = request.getParameter("major");
		categoryString = request.getParameter("category");
		fromDate = request.getParameter("from");
		untilDate = request.getParameter("until");
		
		if(majorString != null) {
			logger.info("Returning event information for all events with the major field marked as: " + majorString + ".");
			return;
		}
		
		else if(categoryString != null) {
			logger.info("Returning event information for all events in the category: ");
			int categoryId = categoryString.charAt(0) - '0';
			switch(categoryId) {
				case 1:
					logger.info("Concerts.");
					return;
				case 2:
					logger.info("Theatre.");
					return;
				case 3:
					logger.info("Musicals.");
					return;
				case 4:
					logger.info("Sports");
					return;
				case 5:
					logger.info("Comedy.");
					return;
				default:
					logger.info("Invalid Category");
					return;
			}
		}
		
		else if(fromDate != null && untilDate != null) {
			logger.info("Returning event information for all events starting on or after: " + fromDate + " and ending on or before " + untilDate  + "."); 
		}
		
		else {
			logger.info("Returning event information for all events currently in the TicketMonster database.");
		}
		
		return;
	}

	@AfterReturning(value="viewEvent(id)", argNames = "id")
	public void singleEventDetails(Long id) {
		logger.info("Returning event information for the event with an ID token of " + id + ".");
		logger.info("Returning all venues where the shows of the event with an ID token of " + id + " is being held.");
	}

	@Before("displayVenues()")
	public void displayVenueDetails() {
		logger.info("Returning information for all venues which are hosting events listed on TicketMonster.");
	}
	
	@Before(value="viewVenue(id)", argNames="id")
	public void viewVenueDetails(Long id) {
		logger.info("Returning venue information for the venue specified by the ID token " + id + ".");
	}

	@Before("showTimes()")
	public void getShowTimes() {
        logger.info("Retrieving show times for event at venue.");
	}
	
	@Before("categories()")
	public void getCategories() {
		logger.info("Retrieving all price categories for show.");
	}

	@Before(value="show(id)", argNames="id")
	public void viewShow(Long id) {
		logger.info("Displaying all relevant details, including event, venue, time, and price categories for the show with ID token " + id);
	}

	@AfterReturning(value="allocated(showId, sectionId, quantity)", argNames="showId, sectionId, quantity")
	public void allocatedSeats(Long showId, Long sectionId, int quantity) {
		logger.info("Allocated a section of " + quantity + " contiguous seats in Section " + sectionId + " for the show " + showId + ".");
	}
	
	@AfterReturning(value="updated(showId, sectionId, quantity)", argNames="showId, sectionId, quantity")
	public void updatedAllocation(Long showId, Long sectionId, int quantity) {
		logger.info("Updated allocation for show " + showId + " in Section " + sectionId + "to be for " + quantity + " seats.");
	}

}
