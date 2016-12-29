package com.cwebview.refresh;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.cwebview.i.IWebviewClientCallback;

/**
 *
 * @author Cuckoo
 * @dat 2016-12-08
 * @description
 *      默认WebviewClient回调实现
 */

public class DefWebviewClientCallback implements IWebviewClientCallback{
    public PullWebView pullWebView = null ;

    public DefWebviewClientCallback(PullWebView pullWebView ){
        this.pullWebView = pullWebView ;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        pullWebView.showLoadingView();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        pullWebView.disLoadingView();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        return true;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return null ;
    }
}
