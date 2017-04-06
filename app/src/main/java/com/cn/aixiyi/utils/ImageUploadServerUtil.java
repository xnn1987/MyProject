package com.cn.aixiyi.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;

import org.ddq.common.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/6.
 */
public class ImageUploadServerUtil {
    private TextView mTvTakePhoto;
    private TextView mTvPhotoBox;
    private TextView mTvCancel;
    private Activity activity;
    private int PHOTO_REQUEST_GALLERY1 = 1;
    private int PHOTO_REQUEST_CAREMA2 = 2;
    private File cameraImgFile;
    private File galleryImgFile;


    /**
     * 弹出对话框---->>  选择照片对话框
     *
     * @param activity
     */
    public void setDialogPictur(Activity activity) {
        this.activity = activity;
        //对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Dialog dialog = builder.create();
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_check_photo, null);
        initDialogViews(view);
        setDialogListener(dialog);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        window.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dialog_window_bg));
        window.setContentView(view);

    }

    /**
     * 对话框控件初始化
     *
     * @param view
     */
    private void initDialogViews(View view) {
        mTvTakePhoto = (TextView) view.findViewById(R.id.dialog_tv_photo);
        mTvPhotoBox = (TextView) view.findViewById(R.id.dialog_tv_photoBox);
        mTvCancel = (TextView) view.findViewById(R.id.dialog_tv_cancle);
    }


    /**
     * 监听  点击事件
     *
     * @param dialog
     */
    private void setDialogListener(final Dialog dialog) {
        mTvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                camera();
            }
        });
        //从图库选取
        mTvPhotoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gallery();
            }
        });
        //取消
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 从图库获取
     */
    private void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY1);
    }

    /**
     * 从相机获取  拍摄的绝对路径
     */
    private void camera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String saveDir = Environment.getExternalStorageDirectory() + "/camera";
            File imageDir = new File(saveDir);
            if (!imageDir.exists()) {
                imageDir.mkdir();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = format.format(new Date(System.currentTimeMillis())) + ".JPEG";
            cameraImgFile = new File(saveDir, time);
            if (!cameraImgFile.exists()) {
                try {
                    cameraImgFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImgFile));
            activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA2);
        }
    }


    /**
     * 从图册中返回到  activityForResutl 然后获取想响应的图片
     *
     * @param data
     * @param activity
     */
    public File getGalleryImg(Intent data, Activity activity) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columIndext = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columIndext);
        cursor.close();
        galleryImgFile = new File(picturePath);
        return galleryImgFile;
    }


    /**
     * 上传  图片文件 的路径
     *
     * @param activity
     * @param fileType
     * @param handler
     */
    public void uploadFileImgFileToService(final Activity activity, String imgFileUrl, String fileType, final Handler handler) {
        File file = new File(imgFileUrl);
        String upLoadUrl="";
        if (!file.exists()) {
            Toast.makeText(activity, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (Contanct.UPLOAD_HEAD.equals(fileType)){//判断是头像上传
            upLoadUrl= Urls.HEADER_UPLOGD_URLS;
            Contanct.UPLOAD_STYLE=Contanct.UPLOAD_HEAD;
        }else if(Contanct.UPLOAD_FEEDBACK.equals(fileType)){//判断是意见反馈上传
            upLoadUrl=Urls.SUGGEST_UPLOGD_URLS;
            Contanct.UPLOAD_STYLE=Contanct.UPLOAD_FEEDBACK;
        }
        MyOkHttpUtils.postFile(activity,upLoadUrl, null, imgFileUrl)
                .execute(new MapCallback(activity) {
                    @Override
                    public void onResponse(Map rMap) {
                        super.onResponse(rMap);
                        if (rMap != null) {
                            int rcode = (int) rMap.get("rcode");
                            if (rcode == 0) {
                                ToastUtil.ToastShow(activity, "上传成功");
                                String url = TextUtil.getTextToString(rMap.get("path"));
                                Message message = handler.obtainMessage();
                                message.obj = url;
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        }
                    }
                });
    }


    /**
     * 获取到相册 的file
     *
     * @return
     */
    public File getGalleryImgFile() {
        return galleryImgFile;
    }

    /**
     * 获取到 拍照 的file
     *
     * @return
     */
    public File getCameraImgFile() {
        return cameraImgFile;
    }
}
