package com.cwebview.i;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      Webview公共方法
 */

public interface IWebviewAssister extends ILiefCycle{
    /***************************************************/
    /************************UI相关**********************/
    /***************************************************/

    /**
     * 获取Webview本身
     * @return
     */
    WebView getWebview();

    /**
     * 获取Webview的配置信息
     * @return
     */
    WebSettings getWebSettings();

    /**
     * 执行WebView的配置
     */
    void doConfigure();


    /***************************************************/
    /************************操作相关**********************/
    /***************************************************/

    /**
     * 加载URL
     * @param url
     */
    void loadUrl(String url);

    /**
     * 重新加载
     */
    void reload();

}
