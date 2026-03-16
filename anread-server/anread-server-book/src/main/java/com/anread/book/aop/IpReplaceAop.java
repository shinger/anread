package com.anread.book.aop;

import com.anread.common.annotation.IpReplace;
import com.anread.book.mapper.SysConfigMapper;
import com.anread.book.utils.RequestContextHolderUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Iterator;

@Aspect
@Component
public class IpReplaceAop {
    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Around("execution(* com.anread.book.controller..*(..))")
    public Object replaceIpForAnnotatedFields(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 提前获取当前线程的客户端IP（拦截器已存储）
        String clientIp = RequestContextHolderUtils.getClientIp();
        // 2. 提前获取数据库中的accessIP（避免多次查询）
        String dbAccessIp = sysConfigMapper.getAccessIP();

        Object result = joinPoint.proceed();

        if (result instanceof Mono) {
            return handleMono((Mono<?>) result, clientIp, dbAccessIp);
        } else if (result instanceof Flux) {
            return handleFlux((Flux<?>) result, clientIp, dbAccessIp);
        } else {
            handleIpReplace(result, clientIp, dbAccessIp);
            return result;
        }
    }

    // 处理Mono：传递客户端IP和数据库IP到响应式线程
    private Mono<?> handleMono(Mono<?> mono, String clientIp, String dbAccessIp) {
        return mono.map(data -> {
            handleIpReplace(data, clientIp, dbAccessIp);
            return data;
        });
    }

    // 处理Flux：同上
    private Flux<?> handleFlux(Flux<?> flux, String clientIp, String dbAccessIp) {
        return flux.map(data -> {
            handleIpReplace(data, clientIp, dbAccessIp);
            return data;
        });
    }

    /**
     * 核心逻辑：客户端IP != 数据库accessIP 时才替换字段中的IP
     */
    private void handleIpReplace(Object obj, String clientIp, String dbAccessIp) {
        if (obj == null || clientIp == null || dbAccessIp == null) {
            return;
        }

        // 1. 处理集合/数组/Result返回体（逻辑不变）
        if (obj instanceof Iterable) {
            ((Iterable<?>) obj).forEach(item -> handleIpReplace(item, clientIp, dbAccessIp));
            return;
        }
        if (obj.getClass().isArray()) {
            for (Object item : (Object[]) obj) {
                handleIpReplace(item, clientIp, dbAccessIp);
            }
            return;
        }
        if (obj.getClass().getName().contains("Result")) {
            try {
                Field dataField = obj.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                handleIpReplace(dataField.get(obj), clientIp, dbAccessIp);
                return;
            } catch (Exception e) { /* 忽略 */ }
        }

        // 2. 处理标记@IpReplace的字段（核心修改）
        ReflectionUtils.doWithFields(obj.getClass(), field -> {
            IpReplace ipReplace = field.getAnnotation(IpReplace.class);
            if (ipReplace == null) {
                return;
            }

            field.setAccessible(true);
            String fieldValue = (String) field.get(obj);
            if (fieldValue == null) {
                return;
            }

            // 执行替换（使用注解中的originalIp，如127.0.0.1/localhost）
            String originalIp = ipReplace.originalIp();
            String newFieldValue = fieldValue.replace(originalIp, dbAccessIp);
            field.set(obj, newFieldValue);
        });
    }
}