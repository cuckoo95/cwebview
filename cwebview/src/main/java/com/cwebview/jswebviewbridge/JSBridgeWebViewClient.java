package com.cwebview.jswebviewbridge;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.cwebview.i.IWebviewClientCallback;
import com.cwebview.jswebviewbridge.i.IJSBridgeEventListener;
import com.cwebview.util.StringUtil;

import java.util.ArrayList;

/**
 *
 * @author Cuckoo
 * @date 2016-12-05
 * @descripton
 * 		基于JSWebViewBridge的{@link WebViewClient},实现方只需要处理一下{}
 */
public class JSBridgeWebViewClient extends WVJBWebViewClient {
	private IWebviewClientCallback callback = null;
	public JSBridgeWebViewClient(WebView webView) {
		// support js send
		super(webView, new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object data, WVJBResponseCallback callback) {
			}
		});
	}

	public void setCallback(IWebviewClientCallback callback) {
		this.callback = callback;
	}

	/***********************************************************/
	/****************注册接收事件和对H5发出通知**********************/
	/***********************************************************/

	/**
	 * 向H5注册多个接收事件， 收到事件后通过{@link IJSBridgeEventListener}进行回调
	 * @param jsMethodList
	 * 		JS方法列表
	 * @param eventListener
	 * 		H5触发的回调函数
     */
	public void registerHandlers(ArrayList<String> jsMethodList, final IJSBridgeEventListener eventListener){
		if( jsMethodList == null ){
			return ;
		}
		for(String jsMethod: jsMethodList){
			if(StringUtil.isEmpty(jsMethod)){
				continue;
			}
			registerHandler(jsMethod,eventListener);
		}
	}

	/**
	 * 向H5注册单个事件
	 * @param jsMethod
	 * @param eventListener
     */
	public void registerHandler(final String jsMethod, final IJSBridgeEventListener eventListener){

		if(StringUtil.isEmpty(jsMethod)){
			return;
		}
		registerHandler(jsMethod,
				new WVJBHandler() {
					@Override
					public void request(Object data, WVJBResponseCallback callback) {
						if( eventListener != null ){
							eventListener.jbRequestHandler(jsMethod,data,callback);
						}
					}
				});
	}

	/**
	 * 通知H5执行某个方法
	 * @param jsMethod
	 * @param data
	 * @param eventListener
	 * 		H5端执行完的回调信息
     */
	public void callHandler(final String jsMethod, Object data, final IJSBridgeEventListener eventListener){
		if( StringUtil.isEmpty(jsMethod)){
			return ;
		}
		callHandler(jsMethod, data, new WVJBResponseCallback() {
			@Override
			public void callback(Object respData) {
				if( eventListener != null  ){
					eventListener.jbCalCallback(jsMethod,respData);
				}
			}
		});
	}


	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		if(callback != null ){
			callback.onPageStarted(view,url,favicon);
		}
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if(callback != null ){
			callback.onPageFinished(view,url);
		}
		super.onPageFinished(view, url);
	}
	
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		if( view != null ){
			view.loadData("","text/html", "UTF-8");//错误处理
		}
		if(callback != null ){
			callback.onReceivedError(view,errorCode,description,failingUrl);
		}
	}

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		boolean isHandle = false;
		if(callback != null ){
			isHandle = callback.onReceivedSslError(view,handler,error);
		}
		if( isHandle ){
			//忽略SSL证书
			handler.proceed();
		}else {
			super.onReceivedSslError(view, handler, error);
		}
	}

	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		if(callback != null ){
			WebResourceResponse resp = callback.shouldInterceptRequest(view,url);
			if(resp != null){
				return resp ;
			}
		}
		return super.shouldInterceptRequest(view, url);
	}

}
