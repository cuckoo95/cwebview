package com.cwebview.def;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.cwebview.i.IFileUploader;
import com.cwebview.i.IVideoPlayer;
import com.cwebview.i.IWebviewCallback;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      继承{@link WebChromeClient},主要是处理文件上传和全屏视频播放
 */

public class DefWebChromeClient extends WebChromeClient {
    //文件上传相关
    private IFileUploader fileUploader = null ;
    //视频播放相关
    private IVideoPlayer videoPlayer = null ;
    private IWebviewCallback webviewCallback = null ;

    public DefWebChromeClient(IWebviewCallback webviewCallback,
                              IFileUploader fileUploader, IVideoPlayer videoPlayer){
        this.fileUploader = fileUploader;
        this.videoPlayer = videoPlayer ;
        this.webviewCallback = webviewCallback ;
    }

    public void onProgressChanged(WebView view, int progress) {
        super.onProgressChanged(view, progress);
        if(webviewCallback != null ){
            //通知加载进度
            webviewCallback.onProgressChanged(view,progress);
        }
    }

    // 以上代码放在在Activity或则Fragment中的onCreate方法中
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {// 新版本的方法
        String message = consoleMessage.message();
        int lineNumber = consoleMessage.lineNumber();
        String sourceID = consoleMessage.sourceId();
        ConsoleMessage.MessageLevel messageLevel = consoleMessage.messageLevel();
        return super.onConsoleMessage(consoleMessage);
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooser(uploadMsg,acceptType,"");
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    // For Android > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        if( fileUploader != null ){
            fileUploader.openFileChooser(uploadMsg,acceptType,capture);
        }
    }
    //For Android >4.4
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if( fileUploader != null ){
            fileUploader.openFileChooser(filePathCallback,null,"");
            return true ;
        }
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }

    //全屏播放视频
    @SuppressLint("NewApi")
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if(videoPlayer != null ){
            videoPlayer.onShowCustomView(view,callback);
        }
    }

    //结束全屏播放视频
    @SuppressLint("NewApi")
    @Override
    public void onHideCustomView() {
        if(videoPlayer != null ){
            videoPlayer.onHideCustomView();
        }
    }

    public void onDestory(){
        fileUploader = null ;
        videoPlayer = null ;
        webviewCallback = null ;
    }
}
