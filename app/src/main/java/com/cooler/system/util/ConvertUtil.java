package com.cooler.system.util;

public class ConvertUtil {
    /**
     * Unicode码转为汉字
     */
    public static String unicode2Chinese(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U'))) {
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                } else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    /*
     * 10进制转字节
     */
    public static byte int2Byte(int i) {
        byte r = (byte) i;
        return r;
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
