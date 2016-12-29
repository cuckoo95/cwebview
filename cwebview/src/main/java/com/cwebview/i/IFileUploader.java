package com.cwebview.i;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      处理文件图片上传相关，由{@link WebChromeClient}回调
 */

public interface IFileUploader extends ILiefCycle {

    /**
     * 由{@link WebChromeClient}回调，用于选择文件
     * @param uploadMsg
     *  当编译版本是4.4以上时， 传入的参数为：ValueCallback<Uri[]>;<br>
     *      4.4以及以下传入的是： ValueCallback<Uri>
     * @param acceptType
     * @param capture
     */
    void openFileChooser(ValueCallback uploadMsg, String acceptType, String capture);
}
