package com.cwebview.i;

import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author Cuckoo
 * @date 2016-12-06
 * @description
 *      视频播放相关参数
 */

public interface IVideoPlayerParams {

    /**
     * 当视频全屏播放时，视频所依附的View, 如果不设置值，当VideoView屏幕切换时在{@link IWebviewCallback#onVideoViewChanged(View, boolean)}<br>
     * 方法中自行处理.
     * @return
     */
    ViewGroup getFullScreenVideoLayout();
}
