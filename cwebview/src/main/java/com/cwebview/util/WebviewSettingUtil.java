package com.cwebview.util;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      Webview设置信息
 */
public class WebviewSettingUtil {
    /**
     * 初始化WebView，包括常用的WebView相关设置，<br>
     * 但不包括addJavascriptInterface()、setWebChromeClient()等方法实现
     * @param webView
     * @return
     */
    public static void initUniversalConfigs(WebView webView) {
        if (webView==null) {
            return ;
        }
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 去掉滚动条占位
        webView.requestFocus();// 支持网页内部操作，比如点击按钮

        /**
         * Setting先关
         */
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);// WebView启用Javascript脚本执行
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // ws.setBlockNetworkImage(true);//把图片加载放在最后来加载渲染
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 提高渲染的优先级
        webSettings.setDomStorageEnabled(true);// 设置可以使用localStorage

        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setBuiltInZoomControls(true);// 设置是否启用内置的缩放控件
        webSettings.setSupportZoom(false);// 设置是否支持缩放
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);

        //H5页面视频播放相关
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
    }


    /**添加UserAgent */
    public static void setUserAgentString(WebView webView, String appUserAgent) {
        if(webView!=null&&!StringUtil.isEmpty(appUserAgent)){
            WebSettings webSettings = webView.getSettings();
            String userAgentString = webSettings.getUserAgentString();
            webSettings.setUserAgentString(userAgentString+appUserAgent);
        }
    }
}
