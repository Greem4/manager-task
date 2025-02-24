package ru.greemlab.managertask.util.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Аспект для логирования вызовов методов.
 * <p>
 * Этот аспект перехватывает вызовы публичных методов в контроллерах и сервисах и логирует:
 * - Начало выполнения метода.
 * - Время выполнения метода.
 * - Результат выполнения метода (или исключение, если оно произошло).
 * <p>
 * Аспект использует AOP (Aspect-Oriented Programming) для логирования
 * и исключает логирование информации об объекте {@link Authentication}.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * Определяет точку отсечения для всех публичных методов в пакетах контроллеров и сервисов.
     */
    @Pointcut("execution(public * ru.greemlab.managertask.controller..*(..)) " +
              "|| execution(public * ru.greemlab.managertask.service..*(..))")
    public void applicationPackagePointcut() {
    }

    /**
     * Перехватывает выполнение методов, определенных в точке отсечения {@link LoggingAspect}.
     * Логирует информацию о начале и завершении выполнения метода, включая время исполнения,
     * а также параметры метода и его результат или исключение, если оно возникло.
     *
     * @param joinPoint точка соединения для перехвата метода.
     * @return результат выполнения метода.
     * @throws Throwable если метод выбрасывает исключение.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = (MethodSignature) joinPoint.getSignature();
        var className = signature.getDeclaringTypeName();
        var methodName = signature.getName();
        var parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        StringBuilder argumentsInfo = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && !(args[i] instanceof Authentication)) {
                argumentsInfo.append(parameterNames[i])
                        .append("=")
                        .append(args[i])
                        .append(i < args.length - 1 ? ", " : "");
            }
        }

        String threadName = Thread.currentThread().getName();
        log.debug("[THREAD: {}] Starting method call: {}.{}({})",
                threadName, className, methodName, argumentsInfo);

        Instant start = Instant.now();

        try {
            Object result = joinPoint.proceed();

            Instant end = Instant.now();
            long elapsedSeconds = Duration.between(start, end).toSeconds();
            log.debug("[THREAD: {}] Completed method call: {}.{} in {} seconds. Result: {}",
                    threadName, className, methodName, elapsedSeconds, result);

            return result;
        } catch (Throwable ex) {
            Instant end = Instant.now();
            long elapsedSeconds = Duration.between(start, end).toSeconds();
            log.error("[THREAD: {}] Exception in method: {}.{} after {} seconds. Exception: {}",
                    threadName, className, methodName, elapsedSeconds, ex.toString());

            throw ex;
        }
    }
}
