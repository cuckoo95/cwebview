package com.cwebview.i;

import android.view.View;
import android.webkit.WebChromeClient;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      视频播放相关,由{@link WebChromeClient}回调
 */

public interface IVideoPlayer extends ILiefCycle{

    /**
     *
     * 开始全屏播放
     * @param view
     * @param callback
     */
    void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback);

    /**
     * 结束全屏播放
     */
    void onHideCustomView();

}
