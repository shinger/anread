package com.anread.user.interceptor;

import com.anread.user.utils.RequestContextHolderUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserAgentInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 存储User-Agent（保留原有逻辑）
        String userAgent = request.getHeader("User-Agent");
        RequestContextHolderUtils.setUserAgent(userAgent);

        // 2. 解析并存储客户端真实IP（新增核心逻辑）
        String clientIp = RequestContextHolderUtils.getRealClientIp(request);
        RequestContextHolderUtils.setClientIp(clientIp);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除上下文，避免内存泄漏
        RequestContextHolderUtils.clear();
    }
}