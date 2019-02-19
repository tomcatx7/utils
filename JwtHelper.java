package com.example.myblog.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

public class JwtHelper {
    //用户可以自定义jwt生成秘钥的常量字符串
    public static String JWT_CONSTANT ="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    /**
     *
     * @param claims 用户自定义声明
     * @return 返回jwt
     */
    public static String generateJwt(HashMap<String,Object> claims) {
        //选择签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //获取用户秘钥
        SecretKey key = getPrivateKey();
        //header
        HashMap<String, Object> header = new HashMap<>();
        header.put("typ", "jwt");
        header.put("alg", signatureAlgorithm.getValue());
        //签名并拼接token
        String token = Jwts.builder().setHeader(header)
                .setClaims(claims)
                .signWith(key, signatureAlgorithm)
                .compact();
        return token;
    }

    /**
     * 校验token签名
     * @param token jwt的格式为 XXX.YYY.ZZZ （xxx-头部信息 yyy-声明内容 zzz-用头部信息和声明内容生成的签名）
     * @return
     */
    public static boolean checkSign(String token) {
        if (token == null || token.equals("")) throw new NullPointerException("校验内容不能为空");
        if (token.split("\\.").length == 3) {
            String header = token.split("\\.")[0];
            String claim = token.split("\\.")[1];
            String sign = token.split("\\.")[2];
            //获取私钥
            SecretKey key = getPrivateKey();
//            JwsHeader header1 = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getHeader();
//            System.out.println(header1);
            //利用私钥key对token的内容再次进行签名
            String signature = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getSignature();
            //对比token中的（zzz签名），判断内容是否被篡改
            if (sign.equals(signature)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param token
     * @return 返回解析后的声明体内容
     */
    public static Map praseClaims(String token){
        SecretKey key = getPrivateKey();
        //声明中的内容就是一个键值对
        Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return body;
    }

    /**
     *
     * @return 返回用户自定义私钥
     */
    public static SecretKey getPrivateKey(){
        //将自定义常量字符串使用base64解码成字节数组
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(JWT_CONSTANT);
        //生成秘钥
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
