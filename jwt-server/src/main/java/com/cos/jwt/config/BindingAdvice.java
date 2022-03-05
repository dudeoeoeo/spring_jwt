package com.cos.jwt.config;

import com.cos.jwt.model.CommonDto;
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
import org.springframework.validation.FieldError;

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

        Object [] parameters = Arrays.stream(joinPoint.getArgs()).map(arg -> !arg.toString().contains("error") ? arg : "").toArray();

        logger.info("[{}][{}] Args: [{}]", type[type.length - 1], method, Arrays.toString(parameters));

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BindingResult && ((BindingResult) arg).hasErrors()) {
                Map<String, Object> errorMap = new HashMap<>();
                for (FieldError fieldError : ((BindingResult) arg).getFieldErrors()) {
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());

                    logger.warn("[{}][{}] Field [{}] Message [{}]", type[type.length - 1], method, fieldError.getField(), fieldError.getDefaultMessage());
                }
                return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
            }
        }

        return joinPoint.proceed();
    }
}
