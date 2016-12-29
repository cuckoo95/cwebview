package com.cwebview.i;

import android.webkit.JavascriptInterface;

/**
 *
 * @author Cuckoo
 * @date 2016-12-05
 * @description
 *      Native与H5默认交互
 */

public interface IJavaScriptInterface {
    /**WebView中添加的JS交互接口关键字*/
    final String JSINTERFACE = "callMobileMethod";


    /**
     * Native与H5通信的interfaceName
     */
    @JavascriptInterface
    String getInterfaceName();

    /**
     * 释放内存
     */
    void onDestory();
}
