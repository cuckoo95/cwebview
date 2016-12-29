package com.cwebview.i;

/**
 * 选择相册还是相机监听
 */
public interface IChooseFileListener {
    /**
     * 选择相册
     */
    void onChoosePhotos();

    /**
     * 选择相机
     */
    void onChooseCamera();

    /**
     * 选择框关闭
     */
    void onDismiss();
}
