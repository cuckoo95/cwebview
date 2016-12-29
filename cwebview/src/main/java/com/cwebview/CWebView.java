package com.cwebview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 * 		添加对Webview的滚动监听
 */
public class CWebView extends WebView {
	//滚动监听
	private OnScrollChangeListener onScrollChangeListener;
	public CWebView(Context context) {
		super(context);
	}

	public CWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onScrollChanged(final int l, final int t, final int oldl,
								   final int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		if (onScrollChangeListener != null) {
			onScrollChangeListener.onScroll(this,l - oldl, t - oldt);
		}
	}

	/**
	 * 设置Webview滚动监听
	 * @param onScrollChangedCallback
     */
	public void setOnScrollChangeListener(
			OnScrollChangeListener onScrollChangedCallback) {
		this.onScrollChangeListener = onScrollChangedCallback;
	}

	/**
	 * Impliment in the activity/fragment/view that you want to listen to the webview
	 */
	public interface OnScrollChangeListener {
		void onScroll(WebView webView, int dx, int dy);
	}
}
