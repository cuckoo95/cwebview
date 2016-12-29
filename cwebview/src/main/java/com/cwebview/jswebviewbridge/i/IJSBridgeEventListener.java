package com.cwebview.jswebviewbridge.i;


import com.cwebview.jswebviewbridge.WVJBWebViewClient;

/**
 *
 * @author Cuckoo
 * @date 2016-12-05
 * @description
 *      JSWebviewBridge通信时的交互事件
 */

public interface IJSBridgeEventListener {
    /**
     * 由H5端主动通知App执行某个JS方法
     * @param jsMethod
     *      约定的方法名称
     * @param data
     *      传输的数据
     * @param callback
     *      通知H5的回调锚点，可通知H5，app端的执行情况
     */
    void jbRequestHandler(String jsMethod, Object data, WVJBWebViewClient.WVJBResponseCallback callback);

    /**
     *  App调用H5的JS方法， 当H5已执行通过该方法进行通知。
     * @param jsMethod
     * @param data
     *      H5端返回的数据
     */
    void jbCalCallback(String jsMethod, Object data);
}
