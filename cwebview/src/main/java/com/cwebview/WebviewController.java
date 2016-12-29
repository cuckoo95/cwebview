package com.cwebview;


import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cwebview.def.DefFileUploader;
import com.cwebview.def.DefLiefCycle;
import com.cwebview.def.DefVideoPlayer;
import com.cwebview.def.DefWebChromeClient;
import com.cwebview.def.DefWebviewAssister;
import com.cwebview.i.IFileUploader;
import com.cwebview.i.IJavaScriptInterface;
import com.cwebview.i.ILiefCycle;
import com.cwebview.i.IVideoPlayer;
import com.cwebview.i.IVideoPlayerParams;
import com.cwebview.i.IWebview;
import com.cwebview.i.IWebviewAssister;
import com.cwebview.i.IWebviewCallback;
import com.cwebview.i.IWebviewClientCallback;
import com.cwebview.i.IWebviewParams;
import com.cwebview.jswebviewbridge.JSBridgeWebViewClient;
import com.cwebview.jswebviewbridge.i.IJSBridgeEventListener;
import com.cwebview.util.StringUtil;

import java.util.ArrayList;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      WebView控制类， 主要负责Webview UI初始化，配置信息以及与H5之间的交互<br>
 *      调用者需要调{@link ILiefCycle}中的所有方法
 */
public class WebviewController implements IWebview{
    private IFileUploader fileUploader = null ;
    private ILiefCycle liefCycleManager = null ;
    private IVideoPlayer videoPlayer = null ;
    private IWebviewAssister webviewAssister = null ;

    private Activity activity = null ;
    private IWebviewCallback webviewCallback = null ;
    private IWebviewParams webviewParams = null ;
    private DefWebChromeClient defWebChromeClient = null;
    //基于JSWebviewBridge的WebViewClient
    private JSBridgeWebViewClient jsWebviewClient = null ;

    public WebviewController(Activity activity, WebView webView,
                             IWebviewParams webviewParams,
                             IWebviewCallback webviewCallback,
                             IVideoPlayerParams videoPlayerParams){
        this.activity = activity;
        this.webviewParams = webviewParams ;
        this.webviewCallback = webviewCallback;
        fileUploader = new DefFileUploader(activity, webviewCallback);
        videoPlayer = new DefVideoPlayer(videoPlayerParams, webviewCallback);
        webviewAssister = new DefWebviewAssister(activity,webView,webviewParams);
        liefCycleManager = new DefLiefCycle(activity,webviewAssister,
                fileUploader,videoPlayer);
        //设置默认的WebChromeClient
        setWebChromeClient(null);
        //设置默认WebClient
        setJBWebviewClient(null);
    }


    /**
     * 设置全屏播放以及上传图片信息
     * @param webChromeClient
     */
    public void setWebChromeClient(WebChromeClient webChromeClient){
        if(getWebview() != null ) {
            if (webChromeClient == null) {
                webChromeClient = getDefWebChromeClient();
            }
            getWebview().setWebChromeClient(webChromeClient);
        }
    }

    /**
     * 注册原生的JS交互
     * @param handler
     */
    public void registerDefaultJS(IJavaScriptInterface handler) {
        if(getWebview() != null && handler != null ){
            getWebview().addJavascriptInterface(handler, StringUtil.f(handler.getInterfaceName()));
        }
    }

    /**************************************************/
    /***************JSWebviewBridge相关******************/
    /**************************************************/
    /**
     * 设置基于JSWebviewBridge的{@link WebViewClient}
     * @param callback
     *      {@link WebViewClient}的回调方法
     */
    public void setJBWebviewClient(IWebviewClientCallback callback){
        if( jsWebviewClient == null ){
            jsWebviewClient = new JSBridgeWebViewClient(getWebview());

        }
        jsWebviewClient.setCallback(callback);
        getWebview().setWebViewClient(jsWebviewClient);
    }


    /**
     * 向H5注册多个接收事件， 收到事件后通过{@link IJSBridgeEventListener}进行回调
     * @param jsMethodList
     * 		JS方法列表
     * @param eventListener
     * 		H5触发的回调函数
     */
    public void registerJBHandlers(ArrayList<String> jsMethodList,
                                 IJSBridgeEventListener eventListener){
        if( jsWebviewClient != null ){
            jsWebviewClient.registerHandlers(jsMethodList,eventListener);
        }
    }

    /**
     * 向H5注册单个事件
     * @param jsMethod
     * @param eventListener
     */
    public void registerJBHandler(final String jsMethod, IJSBridgeEventListener eventListener) {
        if( jsWebviewClient != null ){
            jsWebviewClient.registerHandler(jsMethod,eventListener);
        }
    }

    /**
     * 通知H5执行某个方法
     * @param jsMethod
     * @param data
     * @param eventListener
     * 		H5端执行完的回调信息
     */
    public void callJBHandler(final String jsMethod, Object data, IJSBridgeEventListener eventListener) {
        if( jsWebviewClient != null ){
            jsWebviewClient.callHandler(jsMethod,data,eventListener);
        }
    }

    /**
     * 获取默认WebChromeClient
     * @return
     */
    private WebChromeClient getDefWebChromeClient(){
        if(defWebChromeClient == null ){
            defWebChromeClient = new DefWebChromeClient(webviewCallback,fileUploader,videoPlayer);
        }
        return defWebChromeClient;
    }

    /**************************************************/
    /*********************接口相关***********************/
    /**************************************************/
    @Override
    public WebView getWebview() {
        return webviewAssister.getWebview();
    }

    @Override
    public WebSettings getWebSettings() {
        return webviewAssister.getWebSettings();
    }

    @Override
    public void doConfigure() {
        webviewAssister.doConfigure();
    }

    @Override
    public void loadUrl(String url) {
        webviewAssister.loadUrl(url);
    }

    @Override
    public void reload() {
        webviewAssister.reload();
    }

    /***************************************************************/
    /*******************WebView与Activity生命周期相关*******************/
    /***************************************************************/
    @Override
    public void onResume() {
        liefCycleManager.onResume();
    }

    @Override
    public void onPause() {
        liefCycleManager.onPause();
    }

    @Override
    public void onDestory() {
        liefCycleManager.onDestory();
        if(defWebChromeClient != null ){
            defWebChromeClient.onDestory();
        }

        fileUploader = null ;
        liefCycleManager = null ;
        videoPlayer = null ;
        webviewAssister = null ;

        activity = null ;
        webviewCallback = null ;
        webviewParams = null ;
        defWebChromeClient = null ;
        jsWebviewClient = null ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        liefCycleManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = liefCycleManager.onKeyDown(keyCode,event);
        if( result ){
            return true ;
        }
        return false;
    }

}
