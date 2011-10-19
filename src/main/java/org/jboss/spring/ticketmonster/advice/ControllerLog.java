package org.jboss.spring.ticketmonster.advice;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect-oriented programming class - advice for Ticket Monster MVC controllers (i.e. classes within org.jboss.spring.ticketmonster.mvc)
 * 
 * @author Ryan Bradley
 *
 */

@Aspect
@Component
public class ControllerLog {

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

}
