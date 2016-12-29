package com.cwebview.def;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.cwebview.R;
import com.cwebview.i.IChooseFileListener;
import com.cwebview.i.IFileUploader;
import com.cwebview.i.IWebviewCallback;
import com.cwebview.util.FileUtils;
import com.cwebview.util.PhotosUtils;
import com.cwebview.util.StringUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author Cuckoo
 * @date 2016-12-02
 * @description
 *      处理文件，图片上传
 */

public class DefFileUploader implements IFileUploader,IChooseFileListener{
    private static final int REQ_CAMERA = 2901;
    private static final int REQ_PHOTOS = REQ_CAMERA+1;
    /**
     * 支持的文件类型
     */
    private HashSet<String> supportFormatSet = null ;

    /**
     * 判断是否选择了相册或者相机
     */
    private boolean haveChoosed =false;
    //图片临时文件
    private String tempImagePath;
    //临时图片目录
    private String tempImgDir = null ;
    /**
     * 文件选择/上传
     */
    private ValueCallback mUploadMessage;

    private Activity parent = null ;
    private IWebviewCallback webviewCallback = null ;

    public DefFileUploader(Activity activity,
                           IWebviewCallback webviewCallback ){
        this.parent = activity;
        this.webviewCallback = webviewCallback ;
    }

    @Override
    public void openFileChooser(ValueCallback uploadMsg, String acceptType, String capture) {
        if (mUploadMessage != null)
            return;
        mUploadMessage = uploadMsg;
        if (!checkSDcard())
            return;
        if (webviewCallback != null) {
            //显示选择相机和图库选择框
            webviewCallback.showChooseFileDialog(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //选择/上传 图片/文件
        if (null != mUploadMessage){
            haveChoosed =false;
            Uri uri = null;
            //拍照
            if(requestCode == REQ_CAMERA ){
                uri = afterOpenCamera();
            }
            //选择图片
            else if(requestCode == REQ_PHOTOS){
                uri = afterChosePic(data);
            }
            //将文件上传
            respReceiveFile(uri);
            mUploadMessage = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestory() {
        if( supportFormatSet != null ){
            supportFormatSet.clear();
            supportFormatSet = null ;
        }
        //删除临时目录
        FileUtils.del(tempImgDir);
        tempImagePath = null ;
        mUploadMessage = null ;
        parent = null ;
        webviewCallback = null ;
    }

    /************************************************/
    /*****************选择文件来源监听*******************/
    /************************************************/
    @Override
    public void onChooseCamera() {
        haveChoosed = true;
        File vFile = getTempImageFile();
        tempImagePath = vFile.getAbsolutePath();
        //启动相机
        PhotosUtils.startSysCamera(parent,
                tempImagePath,REQ_CAMERA);
    }

    @Override
    public void onChoosePhotos() {
        haveChoosed = true;
        //调起相册
        PhotosUtils.callSysPhotos(parent, REQ_PHOTOS);
    }

    @Override
    public void onDismiss() {
        if (null != mUploadMessage) {
            if (!haveChoosed) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
        }
    }

    /************************************************/
    /********************具体实现*********************/
    /************************************************/

    /**
     * 将uri中的文件上传
     * @param uri
     */
    private void respReceiveFile(Uri uri){
        if( mUploadMessage != null ){

            if(uri == null ){
                mUploadMessage.onReceiveValue(null);
                return ;
            }
            //判断mUploadMessage的泛型类型是Uri还是Uri数组，android4.4以上版本为Uri数组
            Class paramClass = getValueCallbackT();
            if(Uri.class == paramClass){
                mUploadMessage.onReceiveValue(uri);
            }else if(Uri[].class == paramClass){
                mUploadMessage.onReceiveValue(new Uri[]{uri});
            }else {
                mUploadMessage.onReceiveValue(null);
            }
        }
    }

    /**
     * 获取{@link ValueCallback#onReceiveValue(Object)}方法的参数类型
     * @return
     */
    private Class getValueCallbackT() {
        if (mUploadMessage != null) {
            Method[] methods = mUploadMessage.getClass().getDeclaredMethods();
            if (methods != null) {
                for (Method m : methods) {
                    if (m != null &&
                            "onReceiveValue".equals(m.getName())) {
                        Class[] params = m.getParameterTypes();
                        if (params != null && params.length == 1) {
                            if (params[0] == Uri[].class ||
                                    params[0] == Uri.class) {
                                return params[0];
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 检查SD卡是否存在
     *
     * @return
     */
    public final boolean checkSDcard() {
        boolean flag = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!flag) {
            showTip(R.string.fileuploader_sdcard_unavailable);
        }
        return flag;
    }

    /**
     * 获取图片临时目录
     * @return
     */
    private String getTempImageDir(){
        if( tempImgDir == null ){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String toDay = sdf.format(new Date());
            String filePath = FileUtils.getDiskCachePath(parent);
            filePath += File.separator + "temp"+ File.separator + toDay + File.separator;
            tempImgDir = filePath;
            File dir = new File(tempImgDir);
            dir.mkdirs();
        }
        return tempImgDir;
    }

    /**
     * 获取临时图片文件
     * @return
     */
    private File getTempImageFile(){
        String filePath = getTempImageDir() + UUID.randomUUID().toString() + ".jpg";
        File file = new File(filePath);
        return file;
    }

    /**
     * 拍照结束后
     */
    private Uri afterOpenCamera() {
        File f = new File(tempImagePath);
        if(null!=f&&f.exists()&&f.length()>0){
            PhotosUtils.refreshStatusInGallery(parent,f.getAbsolutePath());
            String tempCompressPath = getTempImageFile().getAbsolutePath();
            File newFile = FileUtils.compressFile(f.getPath(), tempCompressPath);
            return Uri.fromFile(newFile);
        }
        return null ;
    }

    /**
     * 选择照片后结束
     *
     * @param data
     */
    private Uri afterChosePic(Intent data) {
        if(null!=data&&null!=data.getData()){
            Uri selectedImage = data.getData();
            String picturePath = PhotosUtils.getSysPhotoImagePath(parent, selectedImage);
            if(StringUtil.isNotEmpty(picturePath)){
                String temp=picturePath;
                if( isSupportedFormat(temp)){
                    File newFile = FileUtils.compressFile(picturePath, getTempImageFile().getAbsolutePath());
                    return Uri.fromFile(newFile);
                }
                else{
                    showTip(R.string.fileuploader_error_filefromat);
                }
            }else{
                showTip(R.string.fileuploader_error_getimage);
            }
        }
        return null;
    }

    private void showTip(int msgID){
        if( parent != null ){
            Toast.makeText(parent, msgID, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断当前文件格式是否支持上传
     * @param filePath
     * @return
     */
    private boolean isSupportedFormat(String filePath){
        if(StringUtil.isEmpty(filePath)){
            return false ;
        }
        if(supportFormatSet == null ){
            supportFormatSet = new HashSet<String>();
            supportFormatSet.add("png");
            supportFormatSet.add("jpg");
            supportFormatSet.add("jpeg");
        }
        int lastDotIndex = filePath.lastIndexOf(".");
        int startIndex =  lastDotIndex + 1 ;
        if( lastDotIndex >= 0 && startIndex< filePath.length()){
            String postFix = filePath.substring(startIndex,filePath.length()).toLowerCase();
            return supportFormatSet.contains(postFix);
        }
        return false ;
    }
}
