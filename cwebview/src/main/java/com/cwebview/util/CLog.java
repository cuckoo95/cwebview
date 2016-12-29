package com.cwebview.util;

import android.util.Log;

/**
 *
 * @author Cuckoo
 * @date 2016-12-19
 * @description
 *      Log信息
 */

public class CLog {
    private static final boolean ISON = true ;
    private static final String TAG = "CWebview";

    public static void e(String msg){
        if(ISON){
            Log.e(TAG,StringUtil.f(msg));
        }
    }

    public static void w(String msg){
        if(ISON){
            Log.w(TAG,StringUtil.f(msg));
        }
    }

    public static void i(String msg){
        if(ISON){
            Log.i(TAG,StringUtil.f(msg));
        }
    }

    public static void d(String msg){
        if(ISON){
            Log.d(TAG,StringUtil.f(msg));
        }
    }
}
