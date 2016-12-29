package com.cwebview.i;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 *
 * @author Cuckoo
 * @date 2016-12-05
 * @description
 *      {@link WebViewClient} 回调通知
 */

public interface IWebviewClientCallback {
    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onPageFinished(WebView view, String url);

    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

    boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);

    WebResourceResponse shouldInterceptRequest(WebView view, String url);
}

