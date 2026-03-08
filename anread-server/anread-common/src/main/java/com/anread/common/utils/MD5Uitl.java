package com.anread.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Uitl {

    public static String generateMD5(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(bytes);
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                // 将字节转为无符号的十六进制字符串
                String hex = Integer.toHexString(0xff & b);
                // 如果不足两位，加零前缀
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
