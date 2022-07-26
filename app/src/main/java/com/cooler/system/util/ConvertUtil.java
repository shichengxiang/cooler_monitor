package com.cooler.system.util;

import android.util.Log;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertUtil {
    /**
     * Unicode码转为汉字
     */
    public static String unicodeToCN(String paramString) {
        Matcher matcher = Pattern.compile("(\\\\u(\\p{XDigit}{4}))").matcher(paramString);
        while (matcher.find()) {
            char c = (char)Integer.parseInt(matcher.group(2), 16);
            String str = matcher.group(1);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(c);
            stringBuilder.append("");
            paramString = paramString.replace(str, stringBuilder.toString());
        }
        return paramString;
    }

    /*
     * 10进制转字节
     */
    public static byte int2Byte(int i) {
        byte r = (byte) i;
        return r;
    }
    public static byte[] intArr2ByteArr(int[] a){
        byte[] b = new  byte[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i]=(byte) a[i];
        }
        return b;
    }
    public static String toAscii(int[] a){
        StringBuffer bf = new StringBuffer();
        for (int i : a) {
            String hex = Integer.toHexString(i);
            if(hex.length()<2) break;
            for (int j=0;j<hex.length();j+=2){
                if(j+2<=hex.length()){
                    String s = hex.substring(j, j + 2);
                    Util.log("hex==  "+s);
                    int b =Integer.parseInt(s,16);
                    if(b>=32 && b<=127) bf.append((char) b);
                }
            }
        }
        return bf.toString();
    }
    public static String toUnicode(int[] a){
        StringBuffer bf = new StringBuffer();
        for (int i : a) {
            String hex = Integer.toHexString(i);
            if(hex.length()<2) break;
            bf.append("\\u");
            for (int j=hex.length();j>=2;j-=2){
                    String s = hex.substring(j-2, j);
                    bf.append(s);
            }
        }
        return bf.toString();
    }
    public static String getUnicode(int[] a ){
        StringBuffer bf = new StringBuffer();
        for (int i : a) {
            String hex = Integer.toHexString(i);
            if(hex.length()<2) break;
            for (int j=0;j<hex.length();j+=2){
                if(j+2<=hex.length()){
                    String s = hex.substring(j, j + 2);
                    int b =Integer.parseInt(s,16);
                    if(b>=32 && b<=127) bf.append((char) b);
                }
            }
        }
        return bf.toString();
    }

    public static String byte2String(byte[] param) {
        char []wm5=new char[param.length];
        for (int k = 0; k < param.length; k++) {
            wm5[k] = (char)(param[k]+'0');//
        }
        String wm4=String.valueOf(wm5);
        int size = param.length / 8;
        //定义接收数组
        byte[] bytes1 = new byte[size];
        for (int i = 0; i < size; i++) {
            //每次截取8位计算
            String tmp = wm4.substring(8*i,8*(i+1));
            int tmpInt = Integer.parseInt(tmp,2);
            byte tmpByte = (byte)(tmpInt & 0xff);
            bytes1[i] = tmpByte;
        }
        String str = new String(bytes1, StandardCharsets.UTF_8);
        return str;
    }
    public static byte[] hexStringToBytes(String paramString) {
        paramString = paramString.replace(" ", "");
        int i = paramString.length();
        byte[] arrayOfByte = new byte[i / 2];
        for (byte b = 0; b < i; b += 2)
            arrayOfByte[b / 2] = (byte)((Character.digit(paramString.charAt(b), 16) << 4) + Character.digit(paramString.charAt(b + 1), 16));
        return arrayOfByte;
    }
    /*
     *  把十六进制Unicode编码字符串转换为中文字符串
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * 十进制转 unicode
     * @param a
     * @return
     */
    public static String int2Unicode(int a){
        return Character.toString((char) a);
    }

    /**
     * 多个int转ascii
     * @param a
     * @return
     */
    public static String int2Ascii(int a[]){
        StringBuffer b= new StringBuffer();
        for (int i=0;i<a.length;i++){
            char c=(char)a[i];
            b.append(c);
        }
        return b.toString();
    }
    public static String txt2Str(String txt){
        byte[] bs=txt.getBytes();
        StringBuffer bf= new StringBuffer();
        for (int i =0;i<bs.length;i++){
            bf.append(bs[i]).append("?");
        }

        return bf.toString();
    }

}
