package org.jboss.spring.ticketmonster.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
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
    
    @Pointcut("within(org.jboss.spring.ticketmonster.repo*)")
    public void repo() {
    }
    
    @Before("repo()")
    public void logEntry(JoinPoint joinPoint) {
        String arguments = new String();
        int index = 0;
        Object[] args = joinPoint.getArgs();
        
        for(Object obj : args) {
            index++;
            if(index < args.length) {
                arguments += obj.toString();
                arguments += ", ";
            }
            else {
                arguments += ".";
            }
        }
        
        logger.info("Entering the repository/DAO method " + joinPoint.getSignature().getName() + "() with the following arguments: " + arguments);
    }
    
    @AfterReturning("repo()")
    public void logReturnSuccess(JoinPoint joinPoint) {
        logger.info("Returning successfully from the repository/DAO method " + joinPoint.getSignature().getName() + "().");
    }
    
    @AfterThrowing(pointcut="repo()", throwing="e")
    public void logReturnThrown(JoinPoint joinPoint, Exception e) {
        logger.info("Exception " + e.getMessage() + " thrown by " + joinPoint.getSignature().getName() + "().");
    }
}
