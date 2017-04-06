package com.cn.aixiyi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSONObject;
import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.JSQUtil;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.PictureUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.ddq.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xingdapeng
 * @date 2016/11/1
 * @description 某个地方的洗衣机详情
 */
public class WashMachDetailActivity extends CommomAcitivity {
    private Intent intent;
    private View leftLay;
    private View rightLay;
    private View headerView;
    private ImageView mIvRIcon;
    private ImageView mImgAds;
    private ImageView mImgTitle;
    private TextView mTxtName;
    private TextView mTxtState;
    private TextView mTxtAddress;
    private TextView mTxtFloor;

    private TextView mTxtNormal;
    private TextView mTxtMoney;
    private TextView mTxtTime;

    private ToggleButton mTBXyy, mTBRs;

    private View normalView;
    private View bigView;
    private View fastlView;
    private View lostwaterView;
    private ImageView normalImg;
    private ImageView bigImg;
    private ImageView fastlImg;
    private ImageView lostwaterImg;

    private Button updateBtn;
    private TextView payTxt;
    private TextView otherTxt;
    private TextView ensureTxt;
    private ImageView cancelImg;
    private float totalNum = 0;//总共添财
    private int payNum = 0;//支付洗衣
    private float payXyyNum = 0;//洗衣液
    private float payRsjNum = 0;//柔顺剂
    private int stateInt = 0;
    private float payTotalNum = 0;
    private float lastFloat = 0;
    private int xiyiye = 0, rousunji = 0;
    private Fragment mContent;
    private String washingId = "";
    private String washingHeader = "";
    private String washingName = "";
    private String address = "";
    private String Id = "";
    private int positions = 0;
    private List<Map<String, Object>> bannersList = new ArrayList<>();
    private Map<String, Object> mapOne = new HashMap<>();
    private String xyyType = "";
    private String xyyPrice = "";
    private String rsjType = "";
    private String rsjPrice = "";
    private String kcode = "";//夸克编码
    private String snum = "";//序列号
    private boolean isSave = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.washing_mach_detail_lay);
        intent = getIntent();
        positions = intent.getIntExtra("position", 0);
        washingId = intent.getStringExtra("washingId");
        kcode = intent.getStringExtra("kcode");
        snum = intent.getStringExtra("snum");
        DebugLog.e("Response", "positions=" + positions);
        initView();
        setListener();
        getDetailData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyMoneyData();
    }

    private void initView() {
        leftLay = findViewById(R.id.ThreeTitle2Icon_ll_back);
        rightLay = findViewById(R.id.ThreeTitle2Icon_ll_Icon);
        mIvRIcon = (ImageView) findViewById(R.id.ThreeTitle2Icon_iv_icon);
        headerView = findViewById(R.id.xiyji_detial_header_lay);
        mImgAds = (ImageView) findViewById(R.id.wash_header_img);
        mImgTitle = (ImageView) findViewById(R.id.title_img);
        mTxtName = (TextView) findViewById(R.id.waching_style_txt);
        mTxtState = (TextView) findViewById(R.id.waching_state_txt);
        mTxtAddress = (TextView) findViewById(R.id.waching_address_txt);
        mTxtFloor = (TextView) findViewById(R.id.waching_floor_txt);
        mTxtNormal = (TextView) findViewById(R.id.normal_txt);
        mTxtMoney = (TextView) findViewById(R.id.pay_txt);
        mTxtTime = (TextView) findViewById(R.id.time_txt);
        mIvRIcon.setImageResource(R.drawable.detail_save_white);
        mTBXyy = (ToggleButton) findViewById(R.id.xiyiye_tbtn);
        mTBRs = (ToggleButton) findViewById(R.id.rousunji_btn);
        normalView = findViewById(R.id.normal_lay);
        bigView = findViewById(R.id.dawu_lay);
        fastlView = findViewById(R.id.kuaisu_lay);
        lostwaterView = findViewById(R.id.tuoshui_lay);
        normalImg = (ImageView) findViewById(R.id.normal_img);
        bigImg = (ImageView) findViewById(R.id.big_img);
        fastlImg = (ImageView) findViewById(R.id.fast_img);
        lostwaterImg = (ImageView) findViewById(R.id.water_img);
        updateBtn = (Button) findViewById(R.id.update_btn);

    }

    private void setListener() {
        leftLay.setOnClickListener(this);
        rightLay.setOnClickListener(this);
        mIvRIcon.setOnClickListener(this);
        normalView.setOnClickListener(this);
        bigView.setOnClickListener(this);
        fastlView.setOnClickListener(this);
        lostwaterView.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        mTBXyy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    xiyiye = 0;
                    if (bannersList.size() > 0) {
                        mapOne = bannersList.get(4);
                        if (!TextUtils.isEmpty(TextUtil.getTextToString(mapOne.get("price")))) {
                            payXyyNum = Float.parseFloat(TextUtil.getTextToString(mapOne.get("price")));
                        }
                        xyyType = TextUtil.getTextToString(mapOne.get("typeName"));
                        xyyPrice = TextUtil.getTextToString(mapOne.get("price"));
                    }

                } else {
                    xiyiye = 1;
                    payXyyNum = 0;
                    xyyType = "";
                    xyyPrice = "";
                }
            }
        });
        mTBRs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    rousunji = 0;
                    if (bannersList.size() > 0) {
                        mapOne = bannersList.get(6);
                        if (!TextUtils.isEmpty(TextUtil.getTextToString(mapOne.get("price")))) {
                            payRsjNum = Float.parseFloat(TextUtil.getTextToString(mapOne.get("price")));
                            ;
                        }
                        rsjType = TextUtil.getTextToString(mapOne.get("typeName"));
                        rsjPrice = TextUtil.getTextToString(mapOne.get("price"));
                    }

                } else {
                    rousunji = 1;
                    payRsjNum = 0;
                    rsjType = "";
                    rsjPrice = "";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ThreeTitle2Icon_ll_back:
                finish();
                break;
            case R.id.ThreeTitle2Icon_iv_icon:
                SaveWashDetail();
                break;
            case R.id.normal_lay:
                stateInt = 0;
                if (bannersList.size() > 0) {
                    mapOne = bannersList.get(2);
                }
                if (!TextUtils.isEmpty(TextUtil.getTextToString(mapOne.get("price")))) {
                    payNum = Integer.parseInt(TextUtil.getTextToString(mapOne.get("price")));
                }
                setViewOnClickCheck(true, false, false, false);
                setCheckData(TextUtil.getTextToString(mapOne.get("typeName")), TextUtil.getTextToString(mapOne.get("price")) + "元", TextUtil.getTextToString(mapOne.get("washingTime")) + "分钟");
                break;
            case R.id.dawu_lay:
                stateInt = 1;
                if (bannersList.size() > 0) {
                    mapOne = bannersList.get(3);
                }
                if (!TextUtils.isEmpty(TextUtil.getTextToString(mapOne.get("price")))) {
                    payNum = Integer.parseInt(TextUtil.getTextToString(mapOne.get("price")));
                }
                setViewOnClickCheck(false, true, false, false);
                setCheckData(TextUtil.getTextToString(mapOne.get("typeName")), TextUtil.getTextToString(mapOne.get("price")) + "元", TextUtil.getTextToString(mapOne.get("washingTime")) + "分钟");
                break;
            case R.id.kuaisu_lay:
                stateInt = 2;
                if (bannersList.size() > 0) {
                    mapOne = bannersList.get(0);
                }
                if (!TextUtils.isEmpty(TextUtil.getTextToString(mapOne.get("price")))) {
                    payNum = Integer.parseInt(TextUtil.getTextToString(mapOne.get("price")));
                }
                setViewOnClickCheck(false, false, true, false);
                setCheckData(TextUtil.getTextToString(mapOne.get("typeName")), TextUtil.getTextToString(mapOne.get("price")) + "元", TextUtil.getTextToString(mapOne.get("washingTime")) + "分钟");
                break;
            case R.id.tuoshui_lay:
                stateInt = 3;
                if (bannersList.size() > 0) {
                    mapOne = bannersList.get(1);
                }
                if (!TextUtils.isEmpty(TextUtil.getTextToString(mapOne.get("price")))) {
                    payNum = Integer.parseInt(TextUtil.getTextToString(mapOne.get("price")));
                }
                setViewOnClickCheck(false, false, false, true);
                setCheckData(TextUtil.getTextToString(mapOne.get("typeName")), TextUtil.getTextToString(mapOne.get("price")) + "元", TextUtil.getTextToString(mapOne.get("washingTime")) + "分钟");
                break;
            case R.id.update_btn:
                showPopupWindow();
                break;
        }
    }


    /**
     * 获取详情订单
     */
    public void getDetailData() {
        DebugLog.e("Response", "getDetailData=" + washingId);
        //https请求
        String detailUrls = Urls.WASHING_MASHINGDETAIL_URLS + "?washingId=" + washingId;
        MyOkHttpUtils.getRequest(detailUrls).execute(new MapCallback(WashMachDetailActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        Map<String, Object> data = (Map<String, Object>) rMap.get("data");
                        Id = TextUtil.getTextToString(data.get("id"));
                        washingHeader = TextUtil.getTextToString(data.get("img"));
                        washingName = TextUtil.getTextToString(data.get("machineName"));
                        address = TextUtil.getTextToString(data.get("position"));
                        bannersList = (List<Map<String, Object>>) data.get("washingType");
                        mTxtName.setText(washingName);
                        mTxtAddress.setText(address);
                        if ("1".equals(TextUtil.getTextToString(data.get("washingStatus")))) {
                            mTxtState.setText("空闲");
                        } else {
                            mTxtState.setText("使用中");
                        }
                        mapOne = bannersList.get(2);
                        if (!TextUtils.isEmpty(washingHeader)){
                            MyApplication.imageLoader.displayImage(washingHeader, mImgAds, MyApplication.options);
                            DisplayImageOptions options = PictureUtil.setImageOptions(WashMachDetailActivity.this, R.drawable.pic_load, 4);
                            MyApplication.imageLoader.displayImage(washingHeader, mImgTitle, options);
                        }
                        setCheckData(TextUtil.getTextToString(mapOne.get("typeName")), TextUtil.getTextToString(mapOne.get("price")) + "元", TextUtil.getTextToString(mapOne.get("washingTime")) + "分钟");
                    }
                }
            }
        });
    }

    /**
     * 增加详情订单
     */

    private void addDingDanData() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.WASHING_MASHINGDETAILADD_URLS, map).execute(new MapCallback(WashMachDetailActivity.this,Contanct.DIALOG_UPLOAD) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        MyApplication.tianCaiMoneyF = lastFloat;
                        MyApplication.tianCaiMoney = TextUtil.getTextToString(lastFloat);
                        if (MathCommon.isLogin(WashMachDetailActivity.this)) {
                            //支付之后返回结果正确。。。。。
                            Intent intent = new Intent();
                            intent.setClass(WashMachDetailActivity.this, MyOrderActivity.class);
                            intent.putExtra("order", 0);
                            intent.putExtra("kcode", kcode);
                            intent.putExtra("snum", snum);
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }
    private String getParams() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> smap = new HashMap<>();
        smap.put("washingId", Id);
        smap.put("washingName", washingName);
        map.put("washing", smap);
        if (bannersList != null && bannersList.size() > 0) {
            if (stateInt == 0) {
                mapOne = bannersList.get(2);
            } else if (stateInt == 1) {
                mapOne = bannersList.get(3);
            } else if (stateInt == 2) {
                mapOne = bannersList.get(0);
            } else if (stateInt == 3) {
                mapOne = bannersList.get(1);
            }
        }
        String Normal = TextUtil.getTextToString(mapOne.get("typeName"));
        String Money = TextUtil.getTextToString(mapOne.get("price"));
        String Time = TextUtil.getTextToString(mapOne.get("washingTime"));
        Map<String, Object> nmap = new HashMap<>();
        nmap.put("typeName", Normal);
        nmap.put("price", Money);
        nmap.put("washingTime", Time);
        map.put("washType", nmap);
        if (xiyiye == 0) {
            Map<String, Object> xmap = new HashMap<>();
            xmap.put("typeName", xyyType);
            xmap.put("price", payXyyNum);
            map.put("liquid", xmap);
        }
        if (rousunji == 0) {
            Map<String, Object> rmap = new HashMap<>();
            rmap.put("typeName", rsjType);
            rmap.put("price", payRsjNum);
            map.put("softener", rmap);
        }
        String data = ParamsUtil.getParamsFromMap(map);
        DebugLog.e("Response", "data=" + data);
        return data;
    }

    private void showPopupWindow() {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popuwindow_lay, null);
        // 设置按钮的点击事件
        cancelImg = (ImageView) contentView.findViewById(R.id.delete_img);
        ensureTxt = (TextView) contentView.findViewById(R.id.ensure_txt);
        payTxt = (TextView) contentView.findViewById(R.id.num_txt);
        otherTxt = (TextView) contentView.findViewById(R.id.yuer_txt);
        totalNum = MyApplication.tianCaiMoneyF;
        payTotalNum = payNum + payXyyNum + payRsjNum;
        lastFloat = totalNum - payTotalNum;
        if (lastFloat >= 0) {
            payTxt.setText("￥" + payTotalNum);
            otherTxt.setText("￥" + lastFloat + "");
        } else {
            Intent intent = new Intent();
            intent.setClass(WashMachDetailActivity.this, MyQBRechargeActivity.class);
            startActivity(intent);
            ToastUtil.ToastShow(getBaseContext(), "余额不足,请充值!");
        }


        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(updateBtn, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
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
                addDingDanData();
            }
        });
        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void setCheckData(String normal, String money, String time) {
        mTxtNormal.setText(normal);
        mTxtMoney.setText(money);
        mTxtTime.setText(time);

    }

    //收藏
    private void SaveWashDetail() {
        String params = getSaveParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.SAVE_WASHING_URLS, map).execute(new MapCallback(WashMachDetailActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        mIvRIcon.setImageResource(R.drawable.detial_save_yellow);
                    }
                }
            }
        });
    }
    private String getSaveParams() {
        Map<String, Object> map = new HashMap<>();
        map.put("washingId", Id);
        String data = ParamsUtil.getParamsFromMap(map);
        return data;
    }

    private void getMyMoneyData() {
        String moneyUrls = Urls.MONEY_URLS + "?type=" + 0 + "&p=" + 1;
        MyOkHttpUtils.getRequest(moneyUrls).execute(new MapCallback(WashMachDetailActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        Map<String, Object> attrMap = (Map<String, Object>) rMap.get("data");
                        if (!TextUtils.isEmpty(TextUtil.getTextToString(attrMap.get("balance")))) {
                            MyApplication.tianCaiMoneyF = Float.parseFloat(TextUtil.getTextToString(attrMap.get("balance")));
                            MyApplication.tianCaiMoney = TextUtil.getTextToString(attrMap.get("balance"));
                        }
                    }
                }
            }
        });
    }
    private void setViewOnClickCheck(boolean left, boolean middle, boolean next, boolean right) {
        if (left == true) {
            normalView.setBackgroundResource(R.drawable.btn_shape_coners_detial_blue);
            normalImg.setBackgroundResource(R.drawable.detial_wash_white);
        } else {
            normalView.setBackgroundResource(R.drawable.btn_shape_coners_detial_white);
            normalImg.setBackgroundResource(R.drawable.dedtial_wash_blue);
        }
        if (middle == true) {
            bigView.setBackgroundResource(R.drawable.btn_shape_coners_detial_blue);
            bigImg.setBackgroundResource(R.drawable.detail_big_whit);
        } else {
            bigView.setBackgroundResource(R.drawable.btn_shape_coners_detial_white);
            bigImg.setBackgroundResource(R.drawable.detail_big_blue);
        }
        if (next == true) {
            fastlView.setBackgroundResource(R.drawable.btn_shape_coners_detial_blue);
            fastlImg.setBackgroundResource(R.drawable.dedail_fast_white);
        } else {
            fastlView.setBackgroundResource(R.drawable.btn_shape_coners_detial_white);
            fastlImg.setBackgroundResource(R.drawable.detail_fast_blue);
        }
        if (right == true) {
            lostwaterView.setBackgroundResource(R.drawable.btn_shape_coners_detial_blue);
            lostwaterImg.setBackgroundResource(R.drawable.detial_water_white);
        } else {
            lostwaterView.setBackgroundResource(R.drawable.btn_shape_coners_detial_white);
            lostwaterImg.setBackgroundResource(R.drawable.detial_water_blue);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
