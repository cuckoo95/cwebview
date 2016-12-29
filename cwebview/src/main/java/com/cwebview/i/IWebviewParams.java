package com.cwebview.i;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      Webview相关参数
 */

public interface IWebviewParams {
    /**
     * 当前WebView是否支持goBack
     * @return
     */
    boolean isCanGoback();

    /**
     * 获取自定义UserAgent
     * @return
     */
    String getUserAgent();

}
