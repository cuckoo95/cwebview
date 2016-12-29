package com.cwebview.i;

import android.view.View;
import android.webkit.WebView;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      与Webview相关的参数以及回调
 */

public interface IWebviewCallback {
    /**
     * Webview的加载进度
     * @param view
     * @param progress
     */
    void onProgressChanged(WebView view, int progress);

    /**
     * 视频播放窗口变更
     * @param fullScreenVideoView
     *  全屏时Webview回传的View
     * @param isFullScreen
     *      true:全屏
     *      false:非全屏
     */
    void onVideoViewChanged(View fullScreenVideoView, boolean isFullScreen);


    /**
     * 显示相册，相机选择框
     * @param chooseFileListener
     *      点击按钮以及dialog关闭事件的监听
     */
    void showChooseFileDialog(IChooseFileListener chooseFileListener);
}
