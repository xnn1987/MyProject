package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.bean.ImageBean;
import com.cn.aixiyi.bean.SuggestBean;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myview.imageReduce.ImageCompressActivity;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ImageUploadServerUtil;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.PictureUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.ypy.eventbus.EventBus;

import org.ddq.common.util.JsonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/4.
 */
public class SuggestRKStyleActivity  extends SlidBackActivity {
    private  Intent intent;
    private  View menuLay;
    private TextView headerTxt;
    private ImageUploadServerUtil commonUploadPicture = new ImageUploadServerUtil();
    private View rightLay;
    private TextView rightTxt;
    private  View mViewUploadLay;
    private ImageView mIvUploadPic;
    private ImageView mIvUploadPicImg;
    private String feedBackImg;
    private EditText mEtContent;
    private  View ruanjianLay,jiqiLay,fuwulay,qitaLay;
    private  TextView ruanjianTxt,jiqiTxt,fuwuTxt,qitaTxt;
    private Button ensureBtn;
    private  int style=0;
    private  String title="";
    private  String  content="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.suggest_back_lay);
        initViews();
        setListener();
    }

    private void initViews() {
        menuLay=findViewById(R.id.titleBar_ll_back);
        headerTxt= (TextView) findViewById(R.id.titleBar_tv_title);
        mViewUploadLay=findViewById(R.id.Feedback_ll_uploadPicture);
        mEtContent = (EditText) this.findViewById(R.id.cust_feedback_content);
        mIvUploadPic = (ImageView) findViewById(R.id.Feedback_iv_uploading);
        mIvUploadPicImg = (ImageView) findViewById(R.id.Feedback_iv_uploading_img);
        ruanjianLay=findViewById(R.id.runajian_lay);
        jiqiLay=findViewById(R.id.jiqi_lay);
        fuwulay=findViewById(R.id.xiangguan_lay);
        qitaLay=findViewById(R.id.qita_lay);
        ruanjianTxt= (TextView) findViewById(R.id.runajian_txt);
        jiqiTxt= (TextView) findViewById(R.id.jiqi_txt);
        fuwuTxt= (TextView) findViewById(R.id.xiangguan_txt);
        qitaTxt= (TextView) findViewById(R.id.qita_txt);
        ensureBtn= (Button) findViewById(R.id.ensure_btn);
        headerTxt.setText("意见反馈");
    }

    private void setListener() {
        menuLay.setOnClickListener(this);
        mViewUploadLay.setOnClickListener(this);
        ruanjianLay.setOnClickListener(this);
        jiqiLay.setOnClickListener(this);
        fuwulay.setOnClickListener(this);
        qitaLay.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);

    }

    /**
     * 返回的 图片 url
     * @param bean
     */
    public void onEventMainThread(ImageBean bean){
        if (Contanct.UPLOAD_STYLE.equals(Contanct.UPLOAD_FEEDBACK)){
            feedBackImg = bean.getImgStr();
            MyApplication.imageLoader.displayImage(feedBackImg, mIvUploadPicImg, MyApplication.options);
        }

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.titleBar_ll_back:
                EventBus.getDefault().post(Contanct.MAINACTIVTTY);
                finish();
                break;
            case R.id.Feedback_ll_uploadPicture:
                commonUploadPicture.setDialogPictur(this);
                break;
            case R.id.runajian_lay:
                style = 0;
                setViewOnClickCheck(true, false, false,false);
                break;
            case R.id.jiqi_lay:
                style = 1;
                setViewOnClickCheck(false, true, false,false);
                break;
            case R.id.xiangguan_lay:
                style = 2;
                setViewOnClickCheck(false, false, true,false);
                break;
            case R.id.qita_lay:
                style = 3;
                setViewOnClickCheck(false, false, false,true);
                break;
            case R.id.ensure_btn:
                if (TextUtils.isEmpty(mEtContent.getText().toString())) {
                    Toast.makeText(SuggestRKStyleActivity.this, "请输入反馈内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                upLoadingSuggestAndPic();

                break;
        }
    }
    private void upLoadingSuggestAndPic() {
        content=mEtContent.getText().toString();
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.SETSUGGEST_URLS, map).execute(new MapCallback(SuggestRKStyleActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        if (MathCommon.isLogin(SuggestRKStyleActivity.this)){
                            Intent intent = new Intent();
                            intent.setClass(SuggestRKStyleActivity.this, SuggestRebackActivity.class);
                            intent.putExtra("style", style);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
    private String getParams() {
        if (style==0){
            title="软件问题";
        }else if(style==1){
            title="机器运行";
        }else if(style==2){
            title="相关服务";
        }else if(style==3){
            title="其他";
        }
        Map<String,Object> map=new HashMap<>();
        map.put("title",title);
        map.put("content",content);
        map.put("type",style+"");
        map.put("img",feedBackImg);
        String data= ParamsUtil.getParamsFromMap(map);
        return data;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            File imgFile = null;
            switch (requestCode){
                case 1: //  图片
                    imgFile = commonUploadPicture.getGalleryImg(data,SuggestRKStyleActivity.this);
                    intentToImageCompressActivity(imgFile);
                    break;
                case 2://  相机
                    imgFile = commonUploadPicture.getCameraImgFile();
                    intentToImageCompressActivity(imgFile);
                    break;
            }
            return;
        }
    }

    /**
     * 跳转剪切图片  activity
     */
    private void intentToImageCompressActivity( File imgFile){
        Intent intent2 = new Intent(getApplicationContext(), ImageCompressActivity.class);
        intent2.putExtra(Contanct.UPLOAD_PHOTO, imgFile.getAbsolutePath());
        intent2.putExtra(Contanct.UPLOAD_TPYE, Contanct.UPLOAD_FEEDBACK);
        startActivityForResult(intent2,Contanct.PICTER_INTENT);
    }
    private void setViewOnClickCheck(boolean left, boolean middle, boolean next, boolean right) {
        if (left == true) {
            ruanjianLay.setBackgroundResource(R.drawable.btn_suggest_back_blue);
            ruanjianTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        } else {
            ruanjianLay.setBackgroundResource(R.drawable.btn_suggest_back_gray);
            ruanjianTxt.setTextColor(getResources().getColor(R.color.aixiyi_setting_gray));
        }
        if (middle == true) {
            jiqiLay.setBackgroundResource(R.drawable.btn_suggest_back_blue);
            jiqiTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        } else {
            jiqiLay.setBackgroundResource(R.drawable.btn_suggest_back_gray);
            jiqiTxt.setTextColor(getResources().getColor(R.color.aixiyi_setting_gray));
        }
        if (next == true) {
            fuwulay.setBackgroundResource(R.drawable.btn_suggest_back_blue);
            fuwuTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        } else {
            fuwulay.setBackgroundResource(R.drawable.btn_suggest_back_gray);
            fuwuTxt.setTextColor(getResources().getColor(R.color.aixiyi_setting_gray));
        }
        if (right == true) {
            qitaLay.setBackgroundResource(R.drawable.btn_suggest_back_blue);
            qitaTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        } else {
            qitaLay.setBackgroundResource(R.drawable.btn_suggest_back_gray);
            qitaTxt.setTextColor(getResources().getColor(R.color.aixiyi_setting_gray));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
