package com.bdpit5.bus.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.bdpit5.bus.controller..*(..))")
    public void controllerLayer() {}

    @Pointcut("execution(* com.bdpit5.bus.service..*(..))")
    public void serviceLayer() {}

    @Before("controllerLayer()")
    public void logController(JoinPoint joinPoint) {
        log.info("➡️  Controller: {} | Method: {} | Args: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @Around("serviceLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long timeTaken = System.currentTimeMillis() - start;

        log.info("⏱ Service: {} | Method: {} | Execution Time: {} ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                timeTaken);

        return result;
    }

    @AfterThrowing(
            pointcut = "controllerLayer() || serviceLayer()",
            throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("❌ Exception in {}.{}() with cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}