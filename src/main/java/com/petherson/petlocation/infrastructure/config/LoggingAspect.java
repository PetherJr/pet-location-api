package com.petherson.petlocation.infrastructure.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.petherson.petlocation.application.usecase..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String methodName = joinPoint.getSignature().toShortString();
        
        logger.info("Starting execution of: {}", methodName);
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            logger.info("Finished execution of: {} in {} ms", methodName, stopWatch.getTotalTimeMillis());
            return result;
        } catch (Throwable throwable) {
            stopWatch.stop();
            logger.error("Error in {}: {} ({} ms)", methodName, throwable.getMessage(), stopWatch.getTotalTimeMillis());
            throw throwable;
        }
    }
}
