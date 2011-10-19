package org.jboss.spring.ticketmonster.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect-oriented programming class - advice for Ticket Monster repositories (i.e. classes within org.jboss.spring.ticketmonster.repo)
 * 
 * @author Ryan Bradley
 *
 */

@Aspect
@Component
public class RepositoryLog {

    protected final Log logger = LogFactory.getLog(getClass()); 

    @Pointcut("execution(* org.jboss.spring.ticketmonster.repo.AllocationDao.populateCache(..))")
    public void cache() {
    }
    
    @Pointcut("execution(* org.jboss.spring.ticketmonster.repo.UserDao.getByName(String)) && args(username)")
    public void getUser(String username) {
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
