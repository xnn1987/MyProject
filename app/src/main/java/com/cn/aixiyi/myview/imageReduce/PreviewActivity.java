package com.cn.aixiyi.myview.imageReduce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.bean.ImageBean;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ImageUploadServerUtil;
import com.cn.aixiyi.utils.ImageUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.ypy.eventbus.EventBus;

public class PreviewActivity extends SlidBackActivity {
    private ImageView preview;
    private Bitmap bitmap = null;
    private String imgUrl;
    private String uploadType;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    imgUrl = (String) msg.obj;
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgStr(imgUrl);
                    EventBus.getDefault().post(imageBean);
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_preview);
        setTitleBars();
        getIntentDatas();

        preview = (ImageView) this.findViewById(R.id.iv_preview);
        if (bitmap != null) {
            preview.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap = null;
    }

    private void setTitleBars() {
        SetTitleBar.setTitleText(this, "预览图片", "保存");
        findViewById(R.id.ThreeTitleBar_ll_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFileUrl = ImageUtil.saveBitMapToFile(PreviewActivity.this, bitmap);
                DebugLog.e( "response--> " , "newFileUrl===" +newFileUrl) ;
                if(!TextUtils.isEmpty(newFileUrl)){
                    new ImageUploadServerUtil().uploadFileImgFileToService(PreviewActivity.this, newFileUrl, uploadType, handler);
                }

            }
        });
    }

    public void getIntentDatas() {
        Intent in = getIntent();
        if (in != null) {
            byte[] bis = in.getByteArrayExtra("bitmap");
            bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            uploadType = in.getStringExtra(Contanct.UPLOAD_TPYE);
        }
    }
}
