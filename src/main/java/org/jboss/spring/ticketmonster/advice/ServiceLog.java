package org.jboss.spring.ticketmonster.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect-oriented programming class - advice for Ticket Monster service interfaces (i.e. classes within org.jboss.spring.ticketmonster.service)
 * 
 * @author Ryan Bradley
 *
 */

@Aspect
@Component
public class ServiceLog {

    protected final Log logger = LogFactory.getLog(getClass()); 

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
}
