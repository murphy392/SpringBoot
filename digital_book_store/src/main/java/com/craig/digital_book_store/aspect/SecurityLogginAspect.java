package com.craig.digital_book_store.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityLogginAspect {
    private static final Logger logger = LoggerFactory.getLogger(SecurityLogginAspect.class);

    @Before("execution(* com.craig.digital_book_store.controller.AuthController.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Before executing method: {}", methodName);
    }

    @AfterReturning(pointcut = "execution(* com.craig.digital_book_store.controller.AuthController.authenticateUser(..))",
    returning = "result")
    public void logAfterSuccessfulLogin(JoinPoint joinPoint, Object result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()){
            logger.info("User {} logged in successfully", auth.getName());
        }
    }
}
