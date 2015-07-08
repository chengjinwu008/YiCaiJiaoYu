package com.cjq.yicaijiaoyu.utils;

import java.io.File;

/**
 * Created by CJQ on 2015/7/8.
 */
public class FileUtil {

    public static long getLengthByByte(File file){
        if(!file.isDirectory()){
            return file.length();
        }else {
            long x=0;
            File[] fs= file.listFiles();
            for(File f:fs){
                x+= getLengthByByte(f);
            }
            return x;
        }
    }
}
