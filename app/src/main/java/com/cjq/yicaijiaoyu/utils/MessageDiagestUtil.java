package com.cjq.yicaijiaoyu.utils;

import android.util.Base64;

import java.security.MessageDigest;

/**
 * Created by android on 2015/5/11.
 */
public class MessageDiagestUtil {
    public static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getBase64(String s) {
        return Base64.encodeToString(s.getBytes(),Base64.DEFAULT);
    }

    public static String getFromBase64(String s) {
        return new String(Base64.decode(s.getBytes(),Base64.DEFAULT));
    }
}
