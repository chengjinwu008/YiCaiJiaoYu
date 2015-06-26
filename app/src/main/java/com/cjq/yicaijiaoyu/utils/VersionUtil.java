package com.cjq.yicaijiaoyu.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by CJQ on 2015/6/26.
 */
public class VersionUtil {

    public static String getVersion(PackageManager manager,String packageName) {
        try {
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
