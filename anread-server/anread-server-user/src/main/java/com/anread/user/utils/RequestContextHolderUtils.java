package com.anread.user.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 请求上下文工具类：存储User-Agent + 客户端IP
 */
public class RequestContextHolderUtils {
    // 存储User-Agent
    private static final ThreadLocal<String> USER_AGENT_HOLDER = new ThreadLocal<>();
    // 存储客户端真实IP
    private static final ThreadLocal<String> CLIENT_IP_HOLDER = new ThreadLocal<>();

    // ========== User-Agent 相关 ==========
    public static void setUserAgent(String userAgent) {
        USER_AGENT_HOLDER.set(userAgent);
    }
    public static String getUserAgent() {
        return USER_AGENT_HOLDER.get();
    }

    // ========== 客户端IP 相关 ==========
    public static void setClientIp(String clientIp) {
        CLIENT_IP_HOLDER.set(clientIp);
    }
    public static String getClientIp() {
        return CLIENT_IP_HOLDER.get();
    }

    // ========== 清除上下文 ==========
    public static void clear() {
        USER_AGENT_HOLDER.remove();
        CLIENT_IP_HOLDER.remove();
    }

    /**
     * 解析客户端真实IP（处理反向代理/CDN/nginx转发场景）
     */
    public static String getRealClientIp(HttpServletRequest request) {
        // 1. 优先从X-Forwarded-For获取（CDN/反向代理）
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 2. 其次从X-Real-IP获取（nginx）
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // 3. 最后从request获取原生IP
            ip = request.getRemoteAddr();
        }
        // 处理多IP场景（X-Forwarded-For可能返回多个IP，取第一个）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}