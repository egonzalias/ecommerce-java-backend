package co.com.egonzalias.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* co.com.egonzalias.service..*(..)) || execution(* co.com.egonzalias.controller..*(..))")
    public Object logEssential(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // Log method entry with arguments
        Object[] args = joinPoint.getArgs();
        logger.info("Calling {} with args {}", joinPoint.getSignature().toShortString(), args);

        Object result;
        try {
            result = joinPoint.proceed(); // Execute the method
        } catch (Throwable ex) {
            logger.error("Exception in {}: {}", joinPoint.getSignature().toShortString(), ex.getMessage());
            throw ex;
        }

        long duration = System.currentTimeMillis() - start;

        // Log method exit with result and execution time
        logger.info("Completed {} in {} ms, returned: {}",
                joinPoint.getSignature().toShortString(), duration, result);

        return result;
    }
}
