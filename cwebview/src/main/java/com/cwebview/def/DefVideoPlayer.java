package com.cwebview.def;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;

import com.cwebview.i.IVideoPlayer;
import com.cwebview.i.IVideoPlayerParams;
import com.cwebview.i.IWebviewCallback;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      播放全屏/取消全屏视频时UI显示
 */

public class DefVideoPlayer implements IVideoPlayer {

    /** 全屏播放时Webview回传的View,主要用于加到外部全屏view上 */
    private View fullScreenVideoView = null;
    /**  */
    private WebChromeClient.CustomViewCallback customViewCallBack = null;
    private IWebviewCallback webviewCallback = null ;
    //Webview相关参数
    private IVideoPlayerParams videoPlayerParams = null ;

    public DefVideoPlayer(IVideoPlayerParams params,
                          IWebviewCallback webviewCallback){
        this.webviewCallback = webviewCallback;
        this.videoPlayerParams = params ;
    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        //如果一个视图已经存在，那么立刻终止并新建一个
        if (fullScreenVideoView != null) {
            callback.onCustomViewHidden();
            return;
        }
        fullScreenVideoView = view;

        //全屏显示
        showFullScreenVideoView(true);
        customViewCallBack = callback;
    }

    @Override
    public void onHideCustomView() {
        if (fullScreenVideoView == null) {
            return;
        }
        //取消全屏显示
        showFullScreenVideoView(false);
        fullScreenVideoView = null;
        if(customViewCallBack != null ){
            customViewCallBack.onCustomViewHidden();
        }
    }


    /**
     * 显示或隐藏全屏View
     */
    private void showFullScreenVideoView(boolean isShow){
        ViewGroup parentView = null ;
        if( videoPlayerParams != null ){
            parentView = videoPlayerParams.getFullScreenVideoLayout();
        }
        if( parentView != null ){
            if( fullScreenVideoView != null ){
                if(isShow){
                    //显示全屏播放
                    parentView.setVisibility(View.VISIBLE);
                    parentView.setBackgroundColor(Color.BLACK);
                    //将视频视图添加到全屏视频布局中
                    parentView.addView(fullScreenVideoView);
                }else {
                    //隐藏全屏播放，显示默认播放UI
                    parentView.removeView(fullScreenVideoView);
                    parentView.setVisibility(View.GONE);
                }
            }
        }
        setVideoViewChanged(isShow);
    }

    /**
     * 设置屏幕大小变更回调
     * @param isFullScreen
     */
    private void setVideoViewChanged(boolean isFullScreen){
        if(webviewCallback != null){
            webviewCallback.onVideoViewChanged(fullScreenVideoView,isFullScreen);
        }
    }

    /*********************************************/
    /******************生命周期相关*****************/
    /*********************************************/
    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestory() {
        fullScreenVideoView = null ;
        customViewCallBack = null ;
        webviewCallback = null ;
        videoPlayerParams = null ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //如果处在全屏状态，先退出全屏
            if(fullScreenVideoView != null  ){
                onHideCustomView();
                return true ;
            }
        }
        return false;
    }
}
