package com.cwebview.refresh;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cwebview.i.IChooseFileListener;

/**
 *
 * @author Cuckoo
 * @date 216-16-19
 * @description
 *      下拉刷新接口类
 *
 */

public interface IPullRefreshWebView {

    /**
     * 获取Webview
     * @return
     */
    WebView getWebview();

    /**
     * loading所在布局
     * @return
     */
    ViewGroup getLoadingView();

    /** 网页视频全屏布局 */
    ViewGroup getVideoFullSceenLayout();

    /** WebView所在的布局 */
    ViewGroup getWebviewLayout() ;


    /**
     * 设置刷新状态
     * @param isRefreshable
     *     true:允许下拉刷新
     *     false:禁止下拉刷新
     */
    void setRefreshMode(boolean isRefreshable);

    /**
     * 停止刷新
     */
    void onRefreshComplete();

    /**
     * 显示文件选择提示框
     * @param chooseFileListener
     */
    void showChooseFileDialog(final IChooseFileListener chooseFileListener);
}
