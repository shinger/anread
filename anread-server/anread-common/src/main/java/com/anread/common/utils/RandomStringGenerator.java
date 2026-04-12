package com.anread.common.utils;

import java.security.SecureRandom;

/**
 * 随机字符串生成工具类
 * 生成由数字（0-9）和大小写英文字母（a-z, A-Z）组成的随机字符串
 */
public final class RandomStringGenerator {

    // 字符集：数字 + 大小写英文字母（共62个字符）
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    // 使用 SecureRandom 提供密码学安全的随机性
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 生成指定长度的随机字符串
     *
     * @param length 字符串长度（必须 >= 0）
     * @return 随机字符串
     * @throws IllegalArgumentException 如果 length < 0
     */
    public static String generate(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be non-negative");
        }
        if (length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = SECURE_RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    /**
     * 生成随机长度（在 min 到 max 之间）的随机字符串
     *
     * @param minLength 最小长度（包含，必须 >= 0）
     * @param maxLength 最大长度（包含，必须 >= minLength）
     * @return 随机字符串
     * @throws IllegalArgumentException 如果参数非法
     */
    public static String generate(int minLength, int maxLength) {
        if (minLength < 0) {
            throw new IllegalArgumentException("minLength must be non-negative");
        }
        if (maxLength < minLength) {
            throw new IllegalArgumentException("maxLength must be >= minLength");
        }

        int randomLength = minLength + SECURE_RANDOM.nextInt(maxLength - minLength + 1);
        return generate(randomLength);
    }

    // 私有构造函数防止实例化
    private RandomStringGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }
}