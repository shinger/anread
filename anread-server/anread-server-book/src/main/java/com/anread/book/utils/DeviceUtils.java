package com.anread.book.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.regex.Pattern;

public class DeviceUtils {

    private static final Pattern MOBILE_PATTERN = Pattern.compile(
        ".*(?i)(android|iphone|ipad|ipod|blackberry|iemobile|opera mini|mobile).*"
    );

    public static boolean isMobileDevice() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null && MOBILE_PATTERN.matcher(userAgent).matches();
    }
}