package com.cwebview.def;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.cwebview.i.IFileUploader;
import com.cwebview.i.ILiefCycle;
import com.cwebview.i.IVideoPlayer;
import com.cwebview.i.IWebviewAssister;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      默认处理Webview和Activty生命周期之间的关系<br>
 *       主要处理：<br>
 *           视频播放的resume和pase<br>
 *           Webview的销毁<br>
 *           处理Webview的文件和图片上传
 */

public class DefLiefCycle implements ILiefCycle{
    //Application context
    private Context context ;
    //Webview辅助信息
    private IWebviewAssister webviewAssister = null ;
    private IFileUploader fileUploader = null ;
    private IVideoPlayer videoPlayer = null ;

    public DefLiefCycle(Context context,
                        IWebviewAssister webviewAssister,
                        IFileUploader fileUploader,
                        IVideoPlayer videoPlayer){
        this.context = context;
        this.webviewAssister = webviewAssister;
        this.fileUploader = fileUploader;
        this.videoPlayer = videoPlayer;
    }

    @Override
    public void onResume() {
        setWebViewResume();
    }

    @Override
    public void onPause() {
        setWebViewPause();
    }

    @Override
    public void onDestory() {
        //释放webview
        if(webviewAssister != null ){
            webviewAssister.onDestory();
        }
        if(videoPlayer != null ){
            videoPlayer.onDestory();
        }

        if(fileUploader != null ){
            fileUploader.onDestory();
        }
        context = null ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( fileUploader != null ){
            //处理WebView的文件上传
            fileUploader.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = false ;
        if( videoPlayer != null ){
            result = videoPlayer.onKeyDown(keyCode,event);
            if( result ){
                //消耗当前操作
                return true;
            }
        }

        if( webviewAssister != null ){
            //处理view后退
            result = webviewAssister.onKeyDown(keyCode,event);
            if( result ){
                //消耗当前操作
                return true;
            }
        }

        return false;
    }

    /**************************************************/
    /***********************具体实现*********************/
    /**************************************************/

    /**
     * 处理播放视频：继续播放
     */
    private void setWebViewResume(){
        WebView webView = null ;
        try {
            if( webviewAssister != null ){
                webView = webviewAssister.getWebview();
            }
            if(null!=webView){
                webView.getClass().getMethod("onResume").invoke(webView,(Object[])null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理播放视频：停止播放
     */
    private void setWebViewPause(){
        WebView webView = null ;
        try {
            if( webviewAssister != null ){
                webView = webviewAssister.getWebview();
            }
            if(null!=webView){
                webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**************************************************/
    /***********************Get方法*********************/
    /**************************************************/

    public IWebviewAssister getWebviewAssister() {
        return webviewAssister;
    }

    public IFileUploader getFileUploader() {
        return fileUploader;
    }

    public IVideoPlayer getVideoPlayer() {
        return videoPlayer;
    }
}
