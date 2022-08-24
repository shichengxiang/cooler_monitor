package com.cooler.system.util;

import android.util.Base64;
 
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
 
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
 
/**
 * AES加密工具类
 * 使用：
 * {
 *      String source = "mazaiting";
 *      String key = "123456";
 * <p>
 *      String encryptString = AesUtil.encrypt(source, key);
 *      System.out.println("加密后: " + encryptString);
 * <p>
 *      String decryptString = AesUtil.decrypt(encryptString, key);
 *      System.out.println("解密后: " + decryptString);
 * }
 *
 * @author mazaiting
 * @date 2018/3/30
 */
 
public class AesUtil {
    /**
     * 算法Key
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 加密算法
     */
    private static final String CIPHER_ALGORITHM = "AES";
 
 
    /**
     * 加密数据
     *
     * @param data 待加密内容
     * @param key  加密的密钥
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key) {
        try {
            // 获得密钥
            Key desKey = keyGenerator(key);
            // 实例化一个密码对象
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 密码初始化
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            // 执行加密
            byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            // 解析异常
            return "";
        }
    }
 
    /**
     * 解密数据
     *
     * @param data 待解密的内容
     * @param key  解密的密钥
     * @return 解密后的字符串
     */
    public static String decrypt(String data, String key) {
        try {
            // 生成密钥
            Key kGen = keyGenerator(key);
            // 实例化密码对象
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 初始化密码对象
            cipher.init(Cipher.DECRYPT_MODE, kGen);
            // 执行解密
            byte[] bytes = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            // 解析异常
            return "";
        }
    }
 
    /**
     * 获取密钥
     *
     * @param key 密钥字符串
     * @return 返回一个密钥
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static Key keyGenerator(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {
        KeyGenerator kGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kGen.init(128, secureRandom);
        SecretKey secretKey = kGen.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return new SecretKeySpec(encoded, KEY_ALGORITHM);
    }
}
 
