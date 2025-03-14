package com.ss.infrastructure.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @AfterReturning(pointcut = "execution(* com.ss..*(..))", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, Object result) {
        log.debug("Method executed successfully: " + joinPoint.getSignature().getName());
        logMethod(joinPoint, LogLevel.INFO);
    }
    
    @AfterThrowing(pointcut = "execution(* com.ss..*(..))", throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Throwable exception) {
        log.debug("Method execution failed: " + joinPoint.getSignature().getName());
        logMethod(joinPoint, LogLevel.ERROR);
    }
    
    private void logMethod(JoinPoint joinPoint, LogLevel logLevel) {
        switch (logLevel) {
            case ERROR:
                log.error("package :{} -> {} {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), "[ERROR]");
                break;
            case INFO:
                log.info("package :{} -> {} {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), "[SUCESS]");
                break;
            default:
                break;
        }
    }
}