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
 * Aspect-oriented programming class - advice for Ticket Monster service interfaces (i.e. classes within org.jboss.spring.ticketmonster.service)
 * 
 * @author Ryan Bradley
 *
 */

@Aspect
@Component
public class ServiceLog {

    protected final Log logger = LogFactory.getLog(getClass());
    
    @Pointcut(value = "within(org.jboss.spring.ticketmonster.service*)")
    public void service() {
    }
    
    @Before("service()")
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
        
        logger.info("Entering the service method " + joinPoint.getSignature().getName() + "() with the following arguments: " + arguments);
    }

    @AfterReturning("service()")
    public void logReturnSuccess(JoinPoint joinPoint) {
        logger.info("Returning successfully from the service method " + joinPoint.getSignature().getName() + "().");
    }
}
