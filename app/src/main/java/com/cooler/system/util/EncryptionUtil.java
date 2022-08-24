package com.cooler.system.util;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.codec.binary.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
    private static final String mKey = "jnjinmindianzilg";
//
//
//    public static void main(String[] args) {
//        String  content = "{\"codes\": [\"JT1001\",\"JT1002\",\"JT1003\"]} ";
//        String key = "jnjinmindianzilg";
//        System.out.println("加密密钥和解密密钥：" + key);
//        String encrypt = encrypToBase64Str(content, key);
//        System.out.println("加密后：" +encrypt);
//
//
//    }
    /**
     * 业务参数加密方法
     */
    public static String encrypToBase64Str(String content) {

        byte[] encryptResult = encrypt(content, mKey);
        encryptResult = encrypt(android.util.Base64.encodeToString(parseByte2HexStr(encryptResult).getBytes(), Base64.NO_WRAP),  mKey);
        String lastResult = android.util.Base64.encodeToString(parseByte2HexStr(encryptResult).getBytes(), Base64.NO_WRAP);
        return lastResult;
    }
    public static String decryptFromBase64Str(String content){
        byte[] b1 = android.util.Base64.decode(content, Base64.NO_WRAP);
        Log.e("test","length ="+b1);
        byte[] b2 = parseHexStr2Byte(StringUtils.newStringUtf8(b1));
        byte[] decrypt = decrypt(b2, mKey);
        Log.e("test","dec ="+decrypt.length);
        byte[] b3 = android.util.Base64.decode(decrypt, Base64.NO_WRAP);
        Log.e("test","b3 ="+b3.length);
        byte[] b4 = parseHexStr2Byte(StringUtils.newStringUtf8(b3));
        Log.e("test","b4 ="+b4.length);
        byte[] b5= decrypt(b4, mKey);
        return StringUtils.newStringUtf8(b5);
    }


    public static byte[] encrypt(String content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] decrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    public static String hexArray2String(byte[] bs){
        StringBuffer ss=new  StringBuffer();
        for (byte b : bs) {
            ss.append(b);
        }
        return ss.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}