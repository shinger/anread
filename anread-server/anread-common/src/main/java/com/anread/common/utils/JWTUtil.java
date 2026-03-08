package com.anread.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;

public class JWTUtil {

    /**
     * 生成token  header.payload.singature
     */
    private static final String SING = "ANYUE_HARRY";

    public static String getToken(String id, String password) {
        Calendar instance = Calendar.getInstance();
        // 默认7天过期
        instance.add(Calendar.DATE, 7);

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        String token = builder.withExpiresAt(instance.getTime())  //指定令牌过期时间
                .withClaim("id", id)
                .withClaim("password", password)
                .sign(Algorithm.HMAC256(SING));  // sign
        return token;
    }

    /**
     * 验证token  合法性
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    public static String parseId(String token) {
        String userId = null;
        try {
            // 创建算法对象
            Algorithm algorithm = Algorithm.HMAC256(SING);

            // 创建 JWT 验证器
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            // 解析 JWT
            DecodedJWT jwt = verifier.verify(token);

            // 提取 id 信息
            userId = jwt.getClaim("id").asString();

        } catch (JWTVerificationException exception) {
            // 处理解析或验证错误
            System.err.println("Invalid token: " + exception.getMessage());
        }
        return userId;
    }

    /**
     * 获取token信息方法
     */
    /*public static DecodedJWT getTokenInfo(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }*/
}

