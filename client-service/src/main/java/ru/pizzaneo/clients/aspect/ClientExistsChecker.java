package ru.pizzaneo.clients.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Aspect
@Component
public class ClientExistsChecker {

    @Pointcut("execution(public * ru.pizzaneo.clients.service.ClientsService.*(..))")
    public void clientServiceMethodExecution() {
    }

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void clientServiceMethodExecutionAnnotated() {
    }

    @Before("clientServiceMethodExecution() && clientServiceMethodExecutionAnnotated()")
    public void print(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Transactional transactional = method.getAnnotation(Transactional.class);
        if (!transactional.readOnly()) {
            System.out.println("call update method");
//            Arrays.stream(joinPoint.getArgs())
//                    .forEach(o -> System.out.println(o.toString()));
        }
    }
}
