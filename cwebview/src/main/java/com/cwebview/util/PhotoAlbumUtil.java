package com.cwebview.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * 
 * @author Cuckoo
 * @date 2013-09-17
 * @description
 * 		相册相关工具类
 *
 */
public class PhotoAlbumUtil {

	/**
	 * 从相册获取图片地址
	 * @param con
	 * @param targetUri
     * @return
     */
	public static String getSelectedImgPath(Context con, Uri targetUri){
		if(con==null||targetUri==null){
			return "";
		}
		String picturePath = null;
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			picturePath = getPathAfter19(con, targetUri);
		} else {
			picturePath = getPathBefore19(targetUri, con);
		}
		return picturePath;
	}

	/**
	 * 4.4以后通过URI获取绝对路径方法
	 * 以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPathAfter19(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * 4.4版本以下通过URI获取绝对路径方法
	 * @param uri
	 * @param context
	 * @return
	 */
	public static String getPathBefore19(Uri uri, Context context){
		String filename = "" ;
		try {
			if (uri.getScheme().toString().compareTo("content") == 0) {
				Cursor cursor = context.getContentResolver().query(uri,
						new String[] {MediaStore.Audio.Media.DATA}, null, null, null);
				if (cursor.moveToFirst()) {
					filename = cursor.getString(0);
				}
			}else if (uri.getScheme().toString().compareTo("file") == 0){       //file:///开头的uri
				filename = uri.toString().replace("file://", "");
				//替换file://
				if(!filename.startsWith("/mnt")){
					//加上"/mnt"头
					filename += "/mnt";
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		return filename;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

}
