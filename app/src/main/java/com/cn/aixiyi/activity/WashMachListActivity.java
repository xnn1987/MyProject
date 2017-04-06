package com.cn.aixiyi.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myAdapter.MyXyMachingAdapter;
import com.cn.aixiyi.myAdapter.WashPopuAdapter;
import com.cn.aixiyi.myview.PullToRefreshView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.SlidBackActivity;

import org.ddq.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xingdapeng
 * @date 2016/11/1
 * @description 某个地方的洗衣机列表
 */
public class WashMachListActivity extends SlidBackActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    //washing_list_item_lay
    private Intent intent;
    private LinearLayout mMenuLay, mRightlay;
    private ImageView mIvJl;
    private ImageView mIvRIcon;
    private TextView topTextView;
    private String washingAreaId = "";
    private String washingArea = "";
    private ListView mLvWashingList;
    private PullToRefreshView mPrfContain;
    private ArrayList<Map<String, Object>> data = new ArrayList<>();
    private MyXyMachingAdapter mAdapter;
    private TextView mTxtNum, mTxtFloor;
    private int positions = 0;
    private int currentPage = 1;
    private String freeNums = "";
    private String washingId = "";
    private final int TYPE_NO = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
    private boolean isSave = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_NO:
                    resetRefresh(-1, true);
                    break;
                case TYPE_HEAD:
                    mPrfContain.onHeaderRefreshComplete();
                    break;
                case TYPE_FOOT:
                    mPrfContain.onFooterRefreshComplete(data.size());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.washing_list_lay);
        intent = getIntent();
        washingAreaId = intent.getStringExtra("washingAreaId");
        washingArea = intent.getStringExtra("washingArea");
        freeNums = intent.getStringExtra("freeNums");
        initView();
        setListener();
        setAdapter();
        zeroStatus(data);
        sendType(TYPE_NO);

    }

    private void initView() {
        mMenuLay= (LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_back);
        topTextView= (TextView) findViewById(R.id.titleBar_tv_title);
        mIvRIcon= (ImageView) findViewById(R.id.ThreeTitle2Icon_iv_icon);
        mRightlay=(LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_Icon);
        mTxtNum = (TextView) findViewById(R.id.washing_num_txt);
        mTxtFloor = (TextView) findViewById(R.id.washing_floor_txt);
        mLvWashingList = (ListView) findViewById(R.id.MMA_lv_msg);
        mIvJl = (ImageView) findViewById(R.id.MMA_iv_wujilu);
        mPrfContain = (PullToRefreshView) findViewById(R.id.MMA_ptr_contain);
        mPrfContain.setOnHeaderRefreshListener(this);
        mPrfContain.setOnFooterRefreshListener(this);
        mPrfContain.setHeadBackgroudResource(R.color.backgroud_f7f7f7);
        mPrfContain.setFootBackgroudResource(R.color.backgroud_f7f7f7);
        topTextView.setText(washingArea);
        mTxtNum.setText("洗衣机"+freeNums+"台");
        mTxtFloor.setText("所有楼层");
        mIvRIcon.setBackgroundResource(R.drawable.soucang_white);
    }

    private void setAdapter() {
        mAdapter = new MyXyMachingAdapter(WashMachListActivity.this, this.onClickListener, data);
        mLvWashingList.setAdapter(mAdapter);

    }

    private void setListener() {
        mMenuLay.setOnClickListener(this);
        mTxtFloor.setOnClickListener(this);
        mRightlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ThreeTitle2Icon_ll_back:
                finish();
                break;
            case R.id.ThreeTitle2Icon_ll_Icon:
                SaveWashDetailTwo();
                break;
            case R.id.washing_floor_txt:
             //   getPoPuWindow(mTxtFloor);
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Button btn = (Button) view;
            positions = (Integer) btn.getTag();
            Map<String, Object> map = data.get(positions);
             washingId = TextUtil.getTextToString(map.get("id"));
             String kcode =TextUtil.getTextToString(map.get("kcode"));//夸克编码
             String snum = TextUtil.getTextToString(map.get("snum"));//序列号
            DebugLog.e("Response", "washingId=" + washingId);
            if (MathCommon.isLogin(WashMachListActivity.this)){
                Intent intent = new Intent();
                intent.setClass(WashMachListActivity.this, WashMachDetailActivity.class);
                intent.putExtra("position", positions + 1);
                intent.putExtra("washingId", washingId);
                intent.putExtra("kcode", kcode);
                intent.putExtra("snum", snum);
                startActivity(intent);
            }

        }
    };
    public void getWashingData(final int type, boolean isDialog) {
        String washUrls = Urls.WASHING_MASHINGNUMLIST_URLS + "?washingAreaId=" + washingAreaId+ "&p=" + currentPage;
        MyOkHttpUtils.getRequest(washUrls).execute(new MapCallback(WashMachListActivity.this,isDialog) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap!=null){
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        List<Map<String, Object>> bannersList = (List<Map<String, Object>>) rMap.get("data");
                        if (bannersList.size() == 0) {
                            currentPage--;
                        } else {
                            data.addAll(bannersList);
                            mAdapter.notifyDataSetChanged();
                            zeroStatus(data);
                        }
                        sendType(type);
                    }
                }
            }
        });
    }

    //收藏
    private void SaveWashDetailTwo() {
        String params = getSaveTwoParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.SAVE_WASHING_URLS, map).execute(new MapCallback(WashMachListActivity.this) {
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
    private String getSaveTwoParams() {
        Map<String, Object> map = new HashMap<>();
        map.put("areaId", washingAreaId);
        String data = ParamsUtil.getParamsFromMap(map);
        return data;
    }



    public void getPoPuWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.wash_list_floor_popu, null);
        // 设置按钮的点击事件
        ListView floors= (ListView) contentView.findViewById(R.id.wash_list_floor_lv);
        WashPopuAdapter wasAdapter=new WashPopuAdapter(this,data);
        floors.setAdapter(wasAdapter);
        floors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Map<String,Object> map=data.get(position);
                 mTxtFloor.setText(TextUtil.getTextToString(map.get("floor")));
                // mTxtNum.setText("洗衣机"+TextUtil.getTextToString(map.get("freeNums"))+"台");
            }
        });
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);  //设置点击屏幕其它地方弹出框消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
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




    }
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        currentPage++;
        getWashingData(TYPE_FOOT, false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        resetRefresh(TYPE_HEAD, false);
    }

    //  重置刷新
    private void resetRefresh(int type, boolean isDialog) {
        currentPage = 1;
        data.clear();
        getWashingData(type, isDialog);
    }

    /**
     * 是否有数据
     *
     * @param list
     */
    private void zeroStatus(List list) {
        mIvJl.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
    }

    private void sendType(int type) {
        handler.sendEmptyMessage(type);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
