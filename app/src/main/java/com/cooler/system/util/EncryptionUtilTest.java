package com.cooler.system.util;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtilTest {


    /**
     * 编码
     * @param bstr
     * @return String
     */
    public static String Base64encode(byte[] bstr) {
        return Base64.encodeBase64String(bstr);
    }

    /**
     * 解码
     * @param str
     * @return string
     */
    public static byte[] Base64decode(String str) {
        return Base64.decodeBase64(str);
    }

    /*
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        /*防止linux下 随机生成key*/
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(encryptKey.getBytes());
        kgen.init(128, random);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "AES"));

        return cipher.doFinal(content.getBytes("UTF-8"));
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return Base64encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) {
        byte[] decryptBytes = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(StandardCharsets.UTF_8), "AES"));
            decryptBytes = cipher.doFinal(encryptBytes);
            return new String(decryptBytes,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptKey;

    }

    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey){
        return aesDecryptByBytes(Base64decode(encryptStr), decryptKey);
    }
}
