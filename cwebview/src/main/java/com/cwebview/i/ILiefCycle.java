package com.cwebview.i;

import android.content.Intent;
import android.view.KeyEvent;

/**
 *
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *  与Activiy生命周期相关
 */

public interface ILiefCycle {

    /**
     * Activity onResume
     */
    void onResume();

    /**
     * Activity onPause
     */
    void onPause();

    /**
     * 销毁Webview
     */
    void onDestory();

    /**
     * Activity on result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * 处理键盘事件
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyDown(int keyCode, KeyEvent event);
}
