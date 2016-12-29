package com.cwebview.def;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cwebview.CWebView;
import com.cwebview.i.IWebviewAssister;
import com.cwebview.i.IWebviewParams;
import com.cwebview.util.CLog;
import com.cwebview.util.WebviewSettingUtil;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @descrpiton
 *   WebView辅助类， 可接受从外部已创建好的webview
 */

public class DefWebviewAssister implements IWebviewAssister {
    //Webview相关部分参数以及回调
    private IWebviewParams webviewParams = null ;
    private WebView webView = null ;
    private Activity context = null ;

    public DefWebviewAssister(Activity context, IWebviewParams webviewParams){
        this(context,null,webviewParams);
    }

    public DefWebviewAssister(Activity context, WebView webView, IWebviewParams webviewParams){
        this.context = context ;
        this.webView = webView ;
        this.webviewParams = webviewParams;
        //检查webview，如果为null则创建
        checkWebView();
    }

    /**
     * 检查webview，如果为null则创建
     */
    private void checkWebView(){
        if(webView == null ){
            webView = new CWebView(context);
        }
    }



    @Override
    public WebView getWebview() {
        return webView;
    }

    @Override
    public WebSettings getWebSettings() {
        if( webView != null ){
            return webView.getSettings();
        }
        return null;
    }

    @Override
    public void doConfigure() {
        //设置Webview的通用配置
        WebviewSettingUtil.initUniversalConfigs(getWebview());
        //设置UserAgent
        appendUserAgent();
        //添加DownloadListener
        addDownloadListener();
    }

    public void onDestroyWebView() {
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void loadUrl(String url) {
        if( webView != null ){
            webView.loadUrl(url);
        }
    }

    @Override
    public void reload() {
        if( webView != null ){
            CLog.i("Reload url:" + webView.getUrl());
            webView.reload();
        }
    }

    /***************************************************/
    /********************与生命周期相关*********************/
    /***************************************************/
    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestory() {
        onDestroyWebView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(webView != null ){
                boolean isCanGoback = true ;
                if(webviewParams != null ){
                    isCanGoback = webviewParams.isCanGoback();
                }
                if( isCanGoback && webView.canGoBack()){
                    //采用webview默认的回退
                    webView.goBack();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 添加USerAgent
     */
    private void appendUserAgent(){
        if(webviewParams != null ){
            String userAgent = webviewParams.getUserAgent();
            WebviewSettingUtil.setUserAgentString(getWebview(),userAgent);
        }
    }

    /**
     * 添加DownLoadListener
     */
    private void addDownloadListener(){
        if(webView != null ){
            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype, long contentLength) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }
    }
}
