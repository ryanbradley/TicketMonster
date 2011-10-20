package org.jboss.spring.ticketmonster.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
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
    
    @Pointcut("within(org.jboss.spring.ticketmonster.mvc.*)")
    public void mvc() {
    }
    
    @Before("mvc()")
    public void logEntry(JoinPoint joinPoint) {
        int index = 0;
        Object[] args = joinPoint.getArgs();
        String arguments = new String();
        for(Object obj : args) {
            index++;
            arguments += obj.toString();
            if(index < args.length) {
                arguments += ", ";
            }
            else {
                arguments += ".";
            }
        }
        logger.info("Entering the web controller method " + joinPoint.getSignature().getName() + "() with the following arguments: " + arguments);
    }
    
    @AfterReturning("mvc()")
    public void logReturnSuccess(JoinPoint joinPoint) {
        logger.info("Returning successfully from the web controller method " + joinPoint.getSignature().getName() + "().");
    }

/*  @AfterReturning("events(request)")
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
    }*/
}
