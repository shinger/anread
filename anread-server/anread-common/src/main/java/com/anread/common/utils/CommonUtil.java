package com.anread.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommonUtil {

    public static String generateRandomID(int length) {
        String randomID = UUID.randomUUID().toString().replace("-", "");
        return randomID.substring(0, length-1);
    }

    /**
     * 生成日期路径
     * @return 日期路径，格式为yyyy/MM/dd/
     */
    public static String generateDatePath() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
        return now.format(formatter);
    }

    public static void main(String[] args) {
        System.out.println(generateDatePath());
    }

}
