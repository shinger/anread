package com.anread.common.utils;


import org.junit.Test;

public class MD5UtilTest {

    @Test
    public void testGenerateMD5() {
        String test = "hello world";
        String s = MD5Uitl.generateMD5(test.getBytes());
        System.out.println(s);
    }


}
