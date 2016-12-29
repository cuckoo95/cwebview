package com.cwebview.refresh;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cwebview.WebviewController;
import com.cwebview.i.IChooseFileListener;
import com.cwebview.i.IJavaScriptInterface;
import com.cwebview.i.ILiefCycle;
import com.cwebview.i.IVideoPlayerParams;
import com.cwebview.i.IWebview;
import com.cwebview.i.IWebviewCallback;
import com.cwebview.i.IWebviewClientCallback;
import com.cwebview.i.IWebviewParams;
import com.cwebview.jswebviewbridge.i.IJSBridgeEventListener;
import com.cwebview.util.CLog;
import com.cwebview.util.StringUtil;

import java.util.ArrayList;


/**
 * @author Cuckoo
 * @date 2016-12-06
 * @description
 * 		带下拉刷新的Webview,负责Webview的初始化以及下拉相关操作。<br>
 * 		 调用者需要调{@link ILiefCycle}中的所有方法
 */
public class PullWebView implements ILiefCycle, IWebviewCallback{

	protected IPullRefreshWebView pullRefreshWebView;
	protected WebView mWebView;


	private Activity parent = null ;
	//Webview控制类
	private IWebview webviewInfo = null ;
	//创建Webview所需的参数
	private IWebviewParams webviewParams = null ;
	public PullWebView(Activity activity,IPullRefreshWebView pullRefreshWebView,
					   IWebviewParams webviewParams){
		this.parent = activity;
		this.webviewParams = webviewParams ;
		this.pullRefreshWebView = pullRefreshWebView;
		//初始化
		initial();
	}

	/**
	 * 初始化数据
	 */
	public void initial() {
		if(pullRefreshWebView != null ){
			mWebView = pullRefreshWebView.getWebview();
			//配置webview相关信息
			initWebviewInfo();
		}
	}

	/**
	 * 获取Webview正在加载的url地址
	 * @return
     */
	public String getLoadingUrl(){
		String loadingUrl = null ;
		if( getWebView() != null ){
			loadingUrl = getWebView().getUrl();
		}
		return StringUtil.f(loadingUrl);
	}

	/**
	 * 获取webview
	 * @return
     */
	public WebView getWebView(){
		if(webviewInfo != null ){
			return webviewInfo.getWebview();
		}
		return null ;
	}

	/**
	 * 配置webview相关信息
	 */
	private void initWebviewInfo(){
		if( webviewInfo == null ){

			webviewInfo = new WebviewController(parent, mWebView, webviewParams, this, new IVideoPlayerParams() {
				@Override
				public ViewGroup getFullScreenVideoLayout() {
					return pullRefreshWebView.getVideoFullSceenLayout();
				}
			});
			webviewInfo.doConfigure();
			webviewInfo.setJBWebviewClient(new DefWebviewClientCallback(this));
		}
	}

	/**缩放状态-开启缩放 true 开启  false 关闭*/
	public void setAllowZoomEnable(boolean isAllowZoom) {
		if( webviewInfo != null ){
			WebSettings webSettings = webviewInfo.getWebSettings();
			if(webSettings != null ){
				webSettings.setSupportZoom(isAllowZoom);
			}
		}
	}

	/**
	 * 设置是否支持下拉刷新
	 * @param refreshable
	 * 		true:支持下拉刷新<br>
	 * 		false:禁止下拉刷新
	 */
	public void setPull2Refreshable(boolean refreshable) {
		if (pullRefreshWebView ==null) {
			return;
		}
		pullRefreshWebView.setRefreshMode(refreshable);
	}

	/**
	 * 停止下拉刷新
	 */
	public void onRefreshComplete() {
		//添加下拉刷新后，下拉刷新的加载框架要和页面的加载同步
		if (pullRefreshWebView !=null) {
			pullRefreshWebView.onRefreshComplete();
		}
	}

	/**
	 * 注册原生的JS交互
	 * @param handler
	 *  Native与H5的通信串为{@link IJavaScriptInterface#getInterfaceName()}
	 */
	public void registerDefaultJS(IJavaScriptInterface handler){
		if(webviewInfo != null ){
			webviewInfo.registerDefaultJS(handler);
		}
	}

	/**
	 * 设置全屏播放以及上传图片信息
	 * @param webChromeClient
	 *  如果为null，采用默认值
	 */
	public void setWebChromeClient(WebChromeClient webChromeClient){
		if(webviewInfo != null ){
			webviewInfo.setWebChromeClient(webChromeClient);
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
		if(webviewInfo != null ){
			webviewInfo.setJBWebviewClient(callback);
		}
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
		if(webviewInfo != null ){
			webviewInfo.registerJBHandlers(jsMethodList,eventListener);
		}
	}

	/**
	 * 向H5注册单个事件
	 * @param jsMethod
	 * @param eventListener
	 */
	public void registerJBHandler(String jsMethod, IJSBridgeEventListener eventListener){
		if(webviewInfo != null ){
			webviewInfo.registerJBHandler(jsMethod,eventListener);
		}
	}

	/**
	 * 通知H5执行某个方法
	 * @param jsMethod
	 * @param data
	 * @param eventListener
	 * 		H5端执行完的回调信息
	 */
	public void callJBHandler(final String jsMethod, Object data, IJSBridgeEventListener eventListener){
		if(webviewInfo != null ){
			webviewInfo.callJBHandler(jsMethod,data,eventListener);
		}
	}

	/**
	 * 在Webview区域显示loading
	 */
	public void showLoadingView(){
		if(pullRefreshWebView.getLoadingView() != null &&
				mWebView != null ){
			pullRefreshWebView.getLoadingView().setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 关闭Webview区域Loading
	 */
	public void disLoadingView(){

		if(parent != null &&
				pullRefreshWebView.getLoadingView() != null &&
				mWebView != null ){
			parent.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pullRefreshWebView.getLoadingView().setVisibility(View.GONE);
					mWebView.setVisibility(View.VISIBLE);
				}
			});
		}
	}
	
	/**清除WebView的历史记录及缓存*/
	public void clearCache() {
		if (mWebView!=null) {
			mWebView.clearHistory();
			mWebView.clearCache(true);
			mWebView.clearFormData();
		}
	}

	/**
	 * 加载网址
	 * @param url
	 */
	public void loadUrl(String url) {
		if(webviewInfo != null ){
			CLog.i(url);
			webviewInfo.loadUrl(url);
		}
	}

	/**
	 * 重新加载
	 */
	public void reloadUrl(){
		if(webviewInfo != null ){
			webviewInfo.reload();
		}
	}

	/**
	 * 获取Webview的相关信息
	 * @return
     */
	public IWebview getWebviewInfo() {
		return webviewInfo;
	}


	@Override
	public void onResume() {
		if( webviewInfo != null ){
			webviewInfo.onResume();
		}
	}
	
	@Override
	public void onPause() {
		if( webviewInfo != null ){
			webviewInfo.onPause();
		}
	}

	@Override
	public void onDestory() {
		if( webviewInfo != null ){
			webviewInfo.onDestory();
		}

		pullRefreshWebView = null ;
		mWebView = null ;
		parent = null ;
		webviewInfo = null ;
		webviewParams = null ;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( webviewInfo != null ){
			webviewInfo.onActivityResult(requestCode,resultCode,data);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean r = false ;
		if( webviewInfo != null ){
			r = webviewInfo.onKeyDown(keyCode,event);
			if( r ){
				return true;
			}
		}
		return r;
	}

	@Override
	public void onProgressChanged(WebView view, int progress) {
		CLog.i(view.getUrl() + " progress: "+ progress + "/%");
		if (progress == 100) {
			onRefreshComplete();
		}
	}

	@Override
	public void onVideoViewChanged(View fullScreenVideoView, boolean isFullScreen) {
		ViewGroup webviewLayout = pullRefreshWebView.getWebviewLayout();
		if( webviewLayout == null ){
			return ;
		}
		if( isFullScreen ) {
			//设置全屏
			webviewLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
			//隐藏网页布局
			webviewLayout.setVisibility(View.GONE);
		}else {
			//设置屏幕纵向
			parent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			//设置非全屏
			webviewLayout.setSystemUiVisibility(View.INVISIBLE);
			//显示网页布局
			webviewLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showChooseFileDialog(final IChooseFileListener chooseFileListener) {
		if( chooseFileListener == null ){
			return ;
		}

		pullRefreshWebView.showChooseFileDialog(chooseFileListener);
	}

	/**
	 * 控制ImageView动画的开始与结束
	 * @param animationImage
	 * @param animation
	 * @param onOff
     */
	private void animationOnOff(ImageView animationImage, final AnimationDrawable animation, boolean onOff){
		if(animationImage != null && animation != null ){
			if(onOff){
				if(!animation.isRunning()){
					animationImage.post(new Runnable() {
						@Override
						public void run() {
							animation.start();
						}
					});
				}
			}else{
				animationImage.post(new Runnable() {
					@Override
					public void run() {
						animation.stop();
					}
				});
			}
		}
	}
}