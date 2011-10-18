package org.jboss.spring.ticketmonster.domain;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect-oriented programming class to define various pointcuts and cross-cutting methods, such as logging, for the TicketMonster application.
 * 
 * @author Ryan Bradley
 *
 */

@Aspect
public class LogInformation {
	
	protected final Log logger = LogFactory.getLog(getClass()); 
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.mvc.EventController.displayEvents(javax.servlet.http.HttpServletRequest)) && args(request)")
	public void events(HttpServletRequest request) {
	}
	
	@Pointcut(value = "execution(* org.jboss.spring.ticketmonster.mvc.EventController.viewEvent(Long)) && args(id)", argNames="id")
	public void event(Long id) {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.mvc.VenueController.displayVenues(..))")
	public void venues() {
	}
	
	@Pointcut(value = "execution(* org.jboss.spring.ticketmonster.mvc.VenueController.viewVenue(Long)) && args(id)", argNames="id")
	public void venue(Long id) {
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
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.service.AllocationManager.createAllocation(..))")
	public void allocation() {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.service.AllocationManager.persistToCache(..))")
	public void persist() {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.service.AllocationManager.finalizeReservations(..))")
	public void finalize() {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.service.AllocationManager.calculateTotal(..))")
	public void total() {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.service.AllocationManager.persistToDatabase(..))")
	public void database() {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.repo.AllocationDao.populateCache(..))")
	public void cache() {
	}
	
	@Pointcut("execution(* org.jboss.spring.ticketmonster.repo.UserDao.getByName(String)) && args(username)")
	public void getUser(String username) {
	}
	
	@AfterReturning("events(request)")
	public void displayEvents(HttpServletRequest request) {
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

	@AfterReturning(value="event(id)", argNames = "id")
	public void eventDetails(Long id) {
		logger.info("Returning event information for the event with an ID token of " + id + ".");
		logger.info("Returning all venues where the shows of the event with an ID token of " + id + " is being held.");
	}

	@Before("venues()")
	public void displayVenues() {
		logger.info("Returning information for all venues which are hosting events listed on TicketMonster.");
	}
	
	@Before(value="venue(id)", argNames="id")
	public void venueDetails(Long id) {
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

	@AfterReturning("allocation()")
	public void createAllocation() {
		logger.info("Created an Allocation object from a reservation in the cache, i.e. a SeatBlock object.");
	}
	
	@AfterReturning("persist()")
	public void persistChanges() {
		logger.info("Persisted SeatBlock reservation to the database as purchased instead of allocated.");
	}

	@Before("finalize()")
	public void finalizeReservations() {
		logger.info("Creating Allocation objects and persisting reservations as 'purchased' to the database.");
	}
	
	@AfterReturning("total()")
	public void calculatedTotal() {
		logger.info("Calculated total price of all seats purchased in the current session.");
	}
	
	@AfterReturning("database()")
	public void persistToDatabase() {
		logger.info("Persisted all Allocation objects created from the current session's reservations to the database.");
	}
	
	@Before("cache()")
	public void startup() {
		logger.info("Populating the reservations cache with previously made allocations that are stored in the database.");
	}
	
	@AfterThrowing("cache()")
	public void bootstrapFail() {
		logger.info("Population of the cache with allocations in the database failed.");
	}
	
	@AfterReturning("cache()")
	public void bootstrap() {
		logger.info("Successfully populated the cache with Allocation objects from the database.  Already purchased SeatBlocks have been marked as such.");
	}
	
	@AfterReturning("getUser(username)")
	public void user(String username) {
		logger.info("Retrieved User object from the database with a 'username' field of " + username + ".");
	}
}