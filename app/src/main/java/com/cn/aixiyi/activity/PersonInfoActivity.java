package com.cn.aixiyi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.bean.ImageBean;
import com.cn.aixiyi.bean.MainActivityDestroyBean;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myview.MyHeaderCricleView;
import com.cn.aixiyi.myview.imageReduce.ImageCompressActivity;
import com.cn.aixiyi.myview.picker.DatePicker;
import com.cn.aixiyi.myview.picker.OptionPicker;
import com.cn.aixiyi.myview.picker.SexPicker;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.DialogUtil;
import com.cn.aixiyi.utils.ImageUploadServerUtil;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.PictureUtil;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.ypy.eventbus.EventBus;

import org.ddq.common.util.JsonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/11.
 */
public class PersonInfoActivity extends SlidBackActivity {
    private ImageUploadServerUtil commonUploadPicture = new ImageUploadServerUtil();
    private LinearLayout mLlOvalDot;
    private LinearLayout mMenuLay, mRightlay;
    private TextView topTextView;
    private View headerView;
    private View phoneView;
    private View addressView;
    private View nameView;
    private View sexView;
    private View collageView;
    private View classView;
    private View yearView;
    private View passView;
    private View payView;
    private View threeView;
    private MyHeaderCricleView hmHeaderView;
    private TextView mTelTxt;
    private TextView mAddressTxt;
    private TextView mNameTxt;
    private TextView mSaxTxt;
    private TextView mCollageTxt;
    private TextView mClassTxt;
    private TextView mYearTxt;
    private TextView mPassTxt;
    private TextView mPayTxt;
    private TextView mThreeTxt;
    private Button mExitBtn;
    private String headerUrl = "";

    private String type = "", content = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.my_person_info_lay);
        findViews();
        setListeners();
        setUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void findViews() {
        mMenuLay = (LinearLayout) findViewById(R.id.titleBar_ll_back);
        mTelTxt = (TextView) findViewById(R.id.person_tel_txt);
        topTextView = (TextView) findViewById(R.id.titleBar_tv_title);
        headerView = findViewById(R.id.PIA_rl_head);
        phoneView = findViewById(R.id.phone_lay);
        addressView = findViewById(R.id.address_lay);
        nameView = findViewById(R.id.name_lay);
        sexView = findViewById(R.id.sex_lay);
        collageView = findViewById(R.id.school_lay);
        classView = findViewById(R.id.class_lay);
        yearView = findViewById(R.id.year_lay);
        passView = findViewById(R.id.loginpass_lay);
        payView = findViewById(R.id.paypass_lay);
        threeView = findViewById(R.id.thirdlogin_lay);
        hmHeaderView = (MyHeaderCricleView) findViewById(R.id.person_header_img);
        mAddressTxt = (TextView) findViewById(R.id.person_add_txt);
        mNameTxt = (TextView) findViewById(R.id.person_name_txt);
        mSaxTxt = (TextView) findViewById(R.id.person_sex_txt);
        mCollageTxt = (TextView) findViewById(R.id.person_collage_txt);
        mClassTxt = (TextView) findViewById(R.id.person_class_txt);
        mYearTxt = (TextView) findViewById(R.id.person_year_txt);
        mPassTxt = (TextView) findViewById(R.id.person_loginpass_txt);
        mPayTxt = (TextView) findViewById(R.id.person_zhifu_txt);
        mThreeTxt = (TextView) findViewById(R.id.person_three_txt);
        mExitBtn = (Button) findViewById(R.id.gz_result_btn);
        topTextView.setText("我的信息");
    }

    private void setUserInfo() {
        Map<String, Object> map = MyApplication.userInfo;
        if (map != null) {
            if (!TextUtils.isEmpty((String) map.get("phone"))) {
                mTelTxt.setText(map.get("phone") + "");
            }
            if (!TextUtils.isEmpty((String) map.get("address"))) {
                mAddressTxt.setText(map.get("address") + "");
            }
            if (!TextUtils.isEmpty((String) map.get("head"))) {
                mNameTxt.setText(map.get("userName") + "");
            }
            if (!TextUtils.isEmpty((String) map.get("sex"))) {
                mSaxTxt.setText(map.get("sex") + "");
            }
            if (!TextUtils.isEmpty((String) map.get("school"))) {
                mCollageTxt.setText(map.get("school") + "");
            }
            if (!TextUtils.isEmpty((String) map.get("grade"))) {
                mClassTxt.setText(map.get("grade") + "");
            }
            if (!TextUtils.isEmpty((String) map.get("birth"))) {
                mYearTxt.setText(map.get("birth") + "");
            }
            if (!TextUtils.isEmpty((String) map.get("head"))) {
                headerUrl = (String) map.get("head");
                DisplayImageOptions options = PictureUtil.setImageOptions(PersonInfoActivity.this, R.drawable.pic_load, 8);
                MyApplication.imageLoader.displayImage(headerUrl, hmHeaderView, options);
            }
        }
    }

    private void setListeners() {
        mMenuLay.setOnClickListener(this);
        mPassTxt.setOnClickListener(this);
        mPayTxt.setOnClickListener(this);
        mThreeTxt.setOnClickListener(this);
        mExitBtn.setOnClickListener(this);
        headerView.setOnClickListener(this);
        phoneView.setOnClickListener(this);
        addressView.setOnClickListener(this);
        nameView.setOnClickListener(this);
        sexView.setOnClickListener(this);
        collageView.setOnClickListener(this);
        classView.setOnClickListener(this);
        yearView.setOnClickListener(this);
        passView.setOnClickListener(this);
        payView.setOnClickListener(this);
        threeView.setOnClickListener(this);

    }

    /**
     * 返回的 图片 url
     *
     * @param bean
     */
    public void onEventMainThread(ImageBean bean) {
        if (Contanct.UPLOAD_STYLE.equals(Contanct.UPLOAD_HEAD)) {
            headerUrl = bean.getImgStr();
            MyApplication.options = PictureUtil.setImageOptions(PersonInfoActivity.this, R.drawable.pic_load, 8);
            MyApplication.imageLoader.displayImage(headerUrl, hmHeaderView, MyApplication.options);
        }

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(PersonInfoActivity.this, PersonInfoAlertActivity.class);
        switch (view.getId()) {
            case R.id.titleBar_ll_back:
                EventBus.getDefault().post(Contanct.MAINACTIVTTY);
                finish();
                break;
            case R.id.PIA_rl_head:
                commonUploadPicture.setDialogPictur(this);
                break;
            case R.id.phone_lay:
                break;
            case R.id.address_lay:
                type = "0";
                intent.putExtra("title", "地址");
                intent.putExtra("type", type);
                startActivityForResult(intent, 10);
                // showPopupWindow(addressView);
                break;
            case R.id.name_lay:
                type = "1";
                intent.putExtra("title", "姓名");
                intent.putExtra("type", type);
                startActivityForResult(intent, 11);
                //    showPopupWindow(nameView);
                break;
            case R.id.sex_lay:
                type = "2";
                initSexData();
                break;
            case R.id.school_lay:
                type = "3";
                intent.putExtra("title", "学校");
                intent.putExtra("type", type);
                startActivityForResult(intent, 12);
                //   showPopupWindow(collageView);
                break;
            case R.id.class_lay:
                type = "4";
                intent.putExtra("title", "班级");
                intent.putExtra("type", type);
                startActivityForResult(intent, 13);
                // showPopupWindow(classView);
                break;
            case R.id.year_lay:
                type = "5";
                initYearData();
                break;
            case R.id.loginpass_lay:
                break;
            case R.id.paypass_lay:
                break;
            case R.id.thirdlogin_lay:
                break;
            case R.id.person_loginpass_txt:
                break;
            case R.id.person_zhifu_txt:
                break;
            case R.id.person_three_txt:
                break;
            case R.id.gz_result_btn:
                exitingLogin();
                break;
        }
    }


    private void initSexData() {
        SexPicker picker = new SexPicker(this);
        picker.setOffset(1);
        picker.setSelectedIndex(0);
        picker.setTextSize(14);
        picker.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        picker.setLineColor(getResources().getColor(R.color.aixiyi_setting_gray));
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option, int selectedIndex) {
                content = option;
                if (TextUtils.isEmpty(content)) {
                    content = "男";
                }
                uploadPersonInfo();
            }
        });
        picker.show();
    }


    private void initYearData() {
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        picker.setRange(1990, 2020);
        picker.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        picker.setLineColor(getResources().getColor(R.color.aixiyi_setting_gray));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                content = year + "-" + month + "-" + day;
                uploadPersonInfo();
            }
        });
        picker.show();

    }

    //上传个人信息

    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.person_info_edit_lay, null);
        final EditText mcontentEdt = (EditText) contentView.findViewById(R.id.dialog_tv_personinfo);
        TextView ensureTxt = (TextView) contentView.findViewById(R.id.dialog_tv_ensure);
        TextView cancleTxt = (TextView) contentView.findViewById(R.id.dialog_tv_cancle);


        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.dialog_layout_shape));
        ensureTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                if (TextUtils.isEmpty(mcontentEdt.getText().toString().trim())) {
                    ToastUtil.ToastShow(PersonInfoActivity.this, getResources().getString(R.string.personinfo));
                    return;
                }
                content = mcontentEdt.getText().toString().trim();
                uploadPersonInfo();
            }

        });
        cancleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void uploadPersonInfo() {
        String params = getParams(content);
        Map map = new HashMap();
        map.put("data", params);
        MyOkHttpUtils.postRequest(Urls.USER_INFO_UPDATE, map).execute(new MapCallback(PersonInfoActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        if ("2".equals(type)) {
                            mSaxTxt.setText(content);
                            mSaxTxt.setTextColor(getResources().getColor(R.color.aixiyi_qianbao_lightgray));
                        } else if ("5".equals(type)) {
                            mYearTxt.setText(content);
                            mYearTxt.setTextColor(getResources().getColor(R.color.aixiyi_qianbao_lightgray));
                        }
                    }
                }
            }
        });
    }
    private void exitingLogin() {
        MyOkHttpUtils.postRequest(Urls.EXITING_LOGIN_URLS, null).execute(new MapCallback(PersonInfoActivity.this, Contanct.DIALOG_UPLOAD) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(PersonInfoActivity.this, Contanct.SP_USER_NAME);
                        sharedPrefUtil.clear();
                        SharedPrefUtil sharedUserUtil = new SharedPrefUtil(PersonInfoActivity.this, Contanct.SP_USER);
                        sharedUserUtil.clear();
                        MyApplication.userInfo = null;
                        MyApplication.cookies = null;
                        exitAccount();
                    }
                }
            }
        });
    }

    private String getParams(String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("info", content);
        String data = ParamsUtil.getParamsFromMap(map);
        return data;
    }

    /**
     * 退出当前账号
     */
    private void exitAccount() {
        Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
        MyApplication.needcookies = true;
        MyApplication.cookies = "";
        Intent intent = new Intent();
        intent.setClass(this, UserLoginActivity.class);
        startActivity(intent);
        destroyMainActivity();
        finish();
    }

    /**
     * 同时销毁MainActivity
     */
    private void destroyMainActivity() {
        MainActivityDestroyBean bean = new MainActivityDestroyBean();
        bean.setDestroy(true);
        EventBus.getDefault().post(bean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File imgFile = null;
            switch (requestCode) {
                case 1: //  图片
                    imgFile = commonUploadPicture.getGalleryImg(data, PersonInfoActivity.this);
                    intentToImageCompressActivity(imgFile);
                    break;
                case 2://  相机
                    imgFile = commonUploadPicture.getCameraImgFile();
                    intentToImageCompressActivity(imgFile);
                    break;
                case 10://  地址
                    content = data.getStringExtra("content");
                    mAddressTxt.setText(content);
                    DebugLog.e("Response", "mAddressTxt=");
                    break;
                case 11://  姓名
                    content = data.getStringExtra("content");
                    mNameTxt.setText(content);
                    DebugLog.e("Response", "mNameTxt=");
                    break;
                case 12://  学校
                    content = data.getStringExtra("content");
                    mCollageTxt.setText(content);
                    DebugLog.e("Response", "mCollageTxt=");
                    break;
                case 13://  班级
                    content = data.getStringExtra("content");
                    mClassTxt.setText(content);
                    DebugLog.e("Response", "mClassTxt=");
                    break;
            }
            return;
        }
    }

    /**
     * 跳转剪切图片  activity
     */
    private void intentToImageCompressActivity(File imgFile) {
        Intent intent2 = new Intent(getApplicationContext(), ImageCompressActivity.class);
        intent2.putExtra(Contanct.UPLOAD_PHOTO, imgFile.getAbsolutePath());
        intent2.putExtra(Contanct.UPLOAD_TPYE, Contanct.UPLOAD_HEAD);
        startActivityForResult(intent2, Contanct.PICTER_INTENT);
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
