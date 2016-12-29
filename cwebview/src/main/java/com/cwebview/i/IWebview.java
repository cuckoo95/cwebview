package com.cwebview.i;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.cwebview.jswebviewbridge.i.IJSBridgeEventListener;

import java.util.ArrayList;

/**
 *
 * @author Cuckoo
 * @date 2016-12-05
 * @description
 *      Webview对外暴露的方法
 */

public interface IWebview extends IWebviewAssister{

    /**
     * 注册原生的JS交互
     * @param handler
     *  Native与H5的通信串为{@link IJavaScriptInterface#getInterfaceName()}
     */
    void registerDefaultJS(IJavaScriptInterface handler);

    /**
     * 设置全屏播放以及上传图片信息
     * @param webChromeClient
     *  如果为null，采用默认值
     */
    void setWebChromeClient(WebChromeClient webChromeClient);

    /**************************************************/
    /***************JSWebviewBridge相关******************/
    /**************************************************/
    /**
     * 设置基于JSWebviewBridge的{@link WebViewClient}
     * @param callback
     *      {@link WebViewClient}的回调方法
     */
    void setJBWebviewClient(IWebviewClientCallback callback);

    /**
     * 向H5注册多个接收事件， 收到事件后通过{@link IJSBridgeEventListener}进行回调
     * @param jsMethodList
     * 		JS方法列表
     * @param eventListener
     * 		H5触发的回调函数
     */
    void registerJBHandlers(ArrayList<String> jsMethodList,
                            IJSBridgeEventListener eventListener);

    /**
     * 向H5注册单个事件
     * @param jsMethod
     * @param eventListener
     */
    void registerJBHandler(final String jsMethod, IJSBridgeEventListener eventListener);

    /**
     * 通知H5执行某个方法
     * @param jsMethod
     * @param data
     * @param eventListener
     * 		H5端执行完的回调信息
     */
    void callJBHandler(final String jsMethod, Object data, IJSBridgeEventListener eventListener);
}
