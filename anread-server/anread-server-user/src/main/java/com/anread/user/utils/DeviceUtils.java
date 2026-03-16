package com.anread.user.utils;

/**
 * 设备判断工具类：基于自定义ThreadLocal上下文，彻底解决null问题
 */
public class DeviceUtils {
    // 移动端User-Agent关键词
    private static final String[] MOBILE_KEYWORDS = {"android", "iphone", "ipad", "mobile", "ios", "phone", "huawei", "mi", "vivo", "oppo"};

    /**
     * 判断是否为移动端设备（核心方法，永不返回null）
     */
    public static boolean isMobileDevice() {
        // 从自定义上下文获取User-Agent（拦截器已提前存入）
        String userAgent = RequestContextHolderUtils.getUserAgent();
        if (userAgent == null || userAgent.isEmpty()) {
            return false; // 无User-Agent默认非移动端
        }
        // 转小写后判断
        userAgent = userAgent.toLowerCase();
        for (String keyword : MOBILE_KEYWORDS) {
            if (userAgent.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}