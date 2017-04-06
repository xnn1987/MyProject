package com.cn.aixiyi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;


import com.cn.aixiyi.myview.ButtonBack;
import com.cn.aixiyi.myview.CustomDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by cheng on 13-7-5.
 */
public class PhotoUtil {

	public static final int PHOTO_ALBUM = 1;
	public static final int CAMERA = 2;
	public static final int CROP = 3;
	private final Activity mActivity;

	public PhotoUtil(Activity activity) {
		mActivity = activity;
	}

	/**
	 * 选择提示对话框
	 */
	public void showPickDialog() {
		new CustomDialog(mActivity, "上传图片","拍 照","相 册", new ButtonBack() {

			@Override
			public void submite() {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				// intent.setType("image/*");
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				mActivity.startActivityForResult(intent, PHOTO_ALBUM);
			}

			@Override
			public void cancel() {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getExternalCacheDir(mActivity), "crop.jpg")));
				mActivity.startActivityForResult(intent, CAMERA);

			}
		}).show();


	}

	/**
	 * 保存裁剪之后的图片数据
	 */
	public String setPicToView(Bitmap bitmap) {
		if (bitmap != null) {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
			byte[] bytes = bStream.toByteArray();
			return Base64.encodeToString(bytes, Base64.DEFAULT);
		}
		return "";
	}

	public  File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				Toast.makeText(mActivity, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
			}
		}
		return appCacheDir;
	}
}
