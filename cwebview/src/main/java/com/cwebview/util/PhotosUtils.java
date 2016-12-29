package com.cwebview.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;

/**
 *
 * @author Cuckoo
 * @date 2016-12-05
 * @description
 *      相册相关工具类
 */
public class PhotosUtils {

    /**
     * 调起相册
     * @param con
     * @param requestCode
     *  {@link Activity#onActivityResult(int, int, Intent)}中的请求码
     */
    public static void callSysPhotos(Activity con, int requestCode) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT <19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        String IMAGE_UNSPECIFIED = "image/*";
        intent.setType(IMAGE_UNSPECIFIED); // 查看类型
        Intent wrapperIntent = Intent.createChooser(intent, null);
        con.startActivityForResult(wrapperIntent, requestCode);
    }

    /**
     *  启动系统相机
     * @param tempFilePath
     *      生成图片临时存放位置
     * @param reqCode
     *      请求码
     * @return
     *      返回文件Uri
     */
    public static Uri startSysCamera(Activity activity, String tempFilePath,
                                     int reqCode){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(tempFilePath);
        Uri cameraUri = Uri.fromFile(vFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        activity.startActivityForResult(intent, reqCode);
        return cameraUri;
    }

    /**
     * 获取相册中选中图片的绝对路径
     * @param con
     * @param selectedImage
     * @return
     */
    public static String getSysPhotoImagePath(Context con, Uri selectedImage){
        return PhotoAlbumUtil.getSelectedImgPath(con, selectedImage);
    }

    /**
     * 刷新图片在相册中的状态，防止文件存在了，但是相册数据库没有更新
     * @param filePath
     */
    public static void refreshStatusInGallery(Activity parent, String filePath) {
        if(StringUtil.isEmpty(filePath) ||
                parent == null ){
            return ;
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        parent.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
