package com.cjq.yicaijiaoyu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by android on 2015/5/18.
 */
public class StreamUtil {
    private static final int BUFFER_SIZE = 8192;

    public static String readStreamToString(InputStream stream) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        while ((len = stream.read(buffer)) != -1) {
            bao.write(buffer,0,len);
        }
        bao.flush();
        String res=bao.toString("GBK");
        bao.close();
        return res;
    }
}
