package com.xq.live.web.utils;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by hgg on 2018/2/24.
 * 小程序AES解密
 */
public class AESDecodeUtils {

    public static void main(String[] args) throws Exception {
        byte[] encrypData = Base64.decodeBase64("FYA8BCJAfX1ZbvmenKqjbGJ0HtqqH7yXAW6hJjyM+kR9Rq+qDG40bzWC7NSpINyAck3Go1OGNwEBKplUd5S7kJSjn6SVfUXul2eiq+FUxEJejL00DM/2QYueHu0T3q/GgrSukIwYSg+VeGDVbPnFk1nMVlYgOk/Hz55vuv3Xb0vDe3eAmg5kzbnZr49Pnhp8YVrQkc7hKYidI0ss8JqGBg==");
        byte[] ivData = Base64.decodeBase64("pUj9SsUhgXYhLQiVOxI0uw==");
        byte[] sessionKey = Base64.decodeBase64("vuyyj/EuT1MTmJN37Ge5CQ==");
        System.out.println(decrypt(sessionKey,ivData,encrypData));
    }

    public static String decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串
        return new String(cipher.doFinal(encData),"UTF-8");
    }
}