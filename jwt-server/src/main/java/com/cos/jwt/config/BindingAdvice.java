package com.cos.jwt.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class BindingAdvice {

    private static final Logger logger = LoggerFactory.getLogger(BindingAdvice.class);

    /**
     * AOP에서 호출된 컨트롤러 메서드 및 파라미터를 Log로 남긴다.
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.cos.jwt.controller.*Controller.*(..))")
    public Object validationCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        String [] type = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
        String method = joinPoint.getSignature().getName();

        Object [] args = Arrays.stream(joinPoint.getArgs()).map(arg -> arg).toArray();

        logger.info("[{}][{}] Args: [{}]", type[type.length - 1], method, Arrays.toString(args));

        for (Object arg : args) {
            if (arg instanceof BindingResult && ((BindingResult) arg).hasErrors()) {
                Map<Object, Object> errorMap = new HashMap<>();
                ((BindingResult) arg).getFieldErrors().stream().map(fieldError -> {
                    return errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                });

                return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
            }
        }

        return joinPoint.proceed();
    }
}
