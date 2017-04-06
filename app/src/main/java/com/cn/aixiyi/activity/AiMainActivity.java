package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.bean.MainActivityDestroyBean;
import com.cn.aixiyi.bean.ZxingBean;
import com.cn.aixiyi.fragment.LeftFragment;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myAdapter.AiXyHelperAdapter;
import com.cn.aixiyi.myview.MyPagerGalleryView;
import com.cn.aixiyi.myview.Pull2RefreshListView;
import com.cn.aixiyi.myview.XCSlideMenu;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.map.BaiDuMapUtil;
import com.cn.aixiyi.utils.zxing.CaptureActivity;
import com.ypy.eventbus.EventBus;

import org.ddq.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * @author xingdapeng
 * @date 2016/11/1
 * @description 主页面
 */
public class AiMainActivity extends BaseFragmentActivity implements View.OnFocusChangeListener,
        Pull2RefreshListView.OnRefreshListener, Pull2RefreshListView.OnLoadMoreListener  {
    private LinearLayout mMenuLay, mRightlay;
    private TextView topTextView;
    private ImageView backImg, saveImg;
    private LeftFragment mLeftFragment;
    private FragmentTransaction t;
    private Fragment mContent;
    private long firstTime = 0;
    private XCSlideMenu mMenu;
    private View middleView;
    private View washView, zxingView, dingDanView;
    private TextView washTxt, zxingTxt, dingDanTxt;
    private ImageView washImg, zxingImg, dingDanImg;
    private Pull2RefreshListView mPrfContain;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private AiXyHelperAdapter mAdapter;
    private ImageView redIconImg;
    private MyPagerGalleryView mRollViewPager;
    private ArrayList<ImageView> data = new ArrayList<>();
    private List<Map<String, Object>> urls = new ArrayList<>();
    private List<String> mNetImageList = new ArrayList<>();
    private LinearLayout mLlOvalDot;
    private final int TYPE_STOP = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
    private BaiDuMapUtil baiDuMapUtil;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_HEAD:
                    downLoadDatas(-1);
                    break;
                case TYPE_FOOT:
                    downLoadDatas(-1);
                    break;
                case TYPE_STOP:
                    mPrfContain.stopAllRefresh();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_sliding);
        EventBus.getDefault().register(this);
        initTitleView();
        initHeaderView();
        initView();
        setListeners();
        setAdapter();
        initSlidingMenu(savedInstanceState);
        baiDuMapUtil = new BaiDuMapUtil(this,null);
        downLoadDatas(-1);
        getMyOrderData();
    }
    @Override
    public void onStart() {
        super.onStart();
        baiDuMapUtil.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initTitleView() {
        backImg = (ImageView) findViewById(R.id.back_img);
        saveImg = (ImageView) findViewById(R.id.ThreeTitle2Icon_iv_icon);
        mMenuLay = (LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_back);
        topTextView = (TextView) findViewById(R.id.titleBar_tv_title);
        mRightlay = (LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_Icon);
        mMenu = (XCSlideMenu) findViewById(R.id.slideMenu);
    }
    private void initHeaderView() {
        middleView=findViewById(R.id.aimain_header_gallery_lay);// banner滑动
        mRollViewPager = (MyPagerGalleryView) middleView.findViewById(R.id.roll_view_pager);
        mLlOvalDot = (LinearLayout) middleView. findViewById(R.id.homeTitle_ll_ovalLayout1);//  标题点容器
        washView =  middleView.findViewById(R.id.washing_lay);
        zxingView = middleView. findViewById(R.id.shaoyishao_lay);
        dingDanView =  middleView.findViewById(R.id.dingdan_lay);
        washTxt = (TextView) middleView. findViewById(R.id.ai_xiyi_txt);
        zxingTxt = (TextView)  middleView.findViewById(R.id.ai_sys_txt);
        dingDanTxt = (TextView)  middleView.findViewById(R.id.ai_dingdan_txt);
        washImg = (ImageView)  middleView.findViewById(R.id.ai_xiyi_img);
        zxingImg = (ImageView)  middleView.findViewById(R.id.ai_sys_img);
        dingDanImg = (ImageView)  middleView.findViewById(R.id.ai_dingdan_img);
        redIconImg= (ImageView) middleView.findViewById(R.id.ai_dingdanicon_img);

    }
    private void initView() {
        mPrfContain = (Pull2RefreshListView) findViewById(R.id.HomeFM_wlv_main);
        mPrfContain.setOnRefreshListener(this);
        mPrfContain.setOnLoadListener(this);
        backImg.setImageResource(R.mipmap.more);
        topTextView.setText(R.string.app_name);
        saveImg.setImageResource(R.drawable.soucang_white);
    }

    private void setListeners() {
        mMenuLay.setOnClickListener(this);
        mRightlay.setOnClickListener(this);
        zxingView.setOnClickListener(this);
        washView.setOnClickListener(this);
        dingDanView.setOnClickListener(this);
    }


    /**
     * 初始化侧边栏
     */
    private void initSlidingMenu(Bundle savedInstanceState) {
        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new LeftFragment()).commit();
        mMenu.setTintManager(tintManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void SetAdsAdapter() {
        if (urls != null && urls.size() > 0) {
            mNetImageList = new ArrayList<>();
            for (int i = 0; i < urls.size(); i++) {
                mNetImageList.add(urls.get(i).get("img").toString());
            }
        }
        // 第二和第三参数 2选1 ,参数2为 图片网络路径数组 ,参数3为图片id的数组,本地测试用 ,2个参数都有优先采用 参数2
        mRollViewPager.start(AiMainActivity.this, mNetImageList, R.mipmap.detail_banner, 3000, mLlOvalDot,
                R.drawable.dot_focused, R.drawable.dot_normal, null, null);
        mRollViewPager.setMyOnItemClickListener(new MyPagerGalleryView.MyOnItemClickListener() {
            @Override
            public void onItemClick(int curIndex) {
                Map<String, Object> map = urls.get(curIndex);
                String webViewUrl = TextUtil.getTextToString(map.get("url"));
                DebugLog.e("Response", "webViewUrl=="+webViewUrl );
                if (!TextUtils.isEmpty(webViewUrl)){
                    Intent intent = new Intent();
                    intent.setClass(AiMainActivity.this, AiMainWebViewActivity.class);
                    intent.putExtra("Url", webViewUrl);
                    startActivity(intent);
                }
            }
        });
    }

    private void setAdapter() {
        mAdapter = new AiXyHelperAdapter(this, datas);
        mPrfContain.setAdapter(mAdapter);
        mPrfContain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Map<String, Object> map = datas.get(position-1);
                String webViewUrl = TextUtil.getTextToString(map.get("url"));
                if (!TextUtils.isEmpty(webViewUrl)){
                    Intent intent = new Intent();
                    intent.setClass(AiMainActivity.this, AiMainWebViewActivity.class);
                    intent.putExtra("Url", webViewUrl);
                    startActivity(intent);
                }
            }
        });

    }

    //下载生活帮助

    public void downLoadDatas(final int type) {
        MyOkHttpUtils.getRequest(Urls.MAININDEX_URLS).execute(new MapCallback(AiMainActivity.this,false) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap!=null){
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        DebugLog.e("Response", "55555555555555" );
                        Map<String, Object> dataMap = (Map<String, Object>) rMap.get("data");
                        List<Map<String, Object>> bannersList = (List<Map<String, Object>>) dataMap.get("common");
                        urls.clear();
                        datas.clear();
                        if (bannersList.size()<datas.size()) {
                            //.............
                        } else {
                            //设置生活助手
                            datas.addAll(bannersList);
                            mAdapter.notifyDataSetChanged();
                            handler.sendEmptyMessage(TYPE_STOP);
                            DebugLog.e("Response", "77777777777777" );
                        }
                        urls = (List<Map<String, Object>>) dataMap.get("carouse");
                        //设置广告页
                        SetAdsAdapter();
                    }else{
                        handler.sendEmptyMessage(TYPE_STOP);
                    }

                }
            }
        });
    }
    /**
     * 回到主页
     *
     * @param content
     */
    public void onEventMainThread(String content) {
        if (Contanct.MAINACTIVTTY.equals(content)) {
            DebugLog.e("Response", "MainActivity=" );
            mMenu.switchMenu();
        }
    }

    /**
     * eventbus
     *
     * @param bean
     */

    public void onEventMainThread(ZxingBean bean) {
        if (bean != null) {
            String barcodeStr = bean.getBarcodeStr();
            String textStr = bean.getTextStr();
        }

    }

    /**
     * eventbus
     * 销毁 -->    1、 settingActivity中的退出登录
     *
     * @param bean
     */
    public void onEventMainThread(MainActivityDestroyBean bean) {
        if (bean.isDestroy()) {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ThreeTitle2Icon_ll_back:
                mMenu.switchMenu();
                break;
            case R.id.ThreeTitle2Icon_ll_Icon:
                //防止突然来电造成的cookie丢失
                if (MathCommon.isLogin(AiMainActivity.this)){
                    intent.setClass(AiMainActivity.this, MySaveActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.shaoyishao_lay:
                setViewOnClickCheck(true, false, false);
                intent.setClass(AiMainActivity.this, CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.washing_lay:
                setViewOnClickCheck(false, true, false);
                //防止突然来电造成的cookie丢失
                if (MathCommon.isLogin(AiMainActivity.this)){
                    intent.setClass(AiMainActivity.this, WashingMachineActivity.class);
                    intent.putExtra("lac",MyApplication.lac);
                    intent.putExtra("lon",MyApplication.lon);
                    startActivity(intent);
                }

                break;
            case R.id.dingdan_lay:
                //防止突然来电造成的cookie丢失
                if (MathCommon.isLogin(AiMainActivity.this)){
                    setViewOnClickCheck(false, false, true);
                    intent.setClass(AiMainActivity.this, MyOrderActivity.class);
                    startActivity(intent);
                }
                break;


        }
    }
    private void
    getMyOrderData() {
        String  orderUrls=Urls.MYORDER_URLS+"?p="+1;
        MyOkHttpUtils.getRequest(orderUrls).execute(new MapCallback(AiMainActivity.this,false) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        List<Map<String, Object>> bannersList = (List<Map<String, Object>>) rMap.get("data");
                        for(int i=0;i<bannersList.size();i++){
                            String status=TextUtil.getTextToString(bannersList.get(i).get("status"));
                            if ("0".equals(status)){
                                redIconImg.setVisibility(View.VISIBLE);
                            }else {
                                redIconImg.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        });
    }
    private void setViewOnClickCheck(boolean left, boolean middle, boolean right) {
        if (left == true) {
            zxingView.setBackgroundResource(R.color.aixiyi_center_txt_blue);
            zxingTxt.setTextColor(getResources().getColor(R.color.white));
            zxingImg.setBackgroundResource(R.drawable.saoyisao_white);
        } else {
            zxingView.setBackgroundResource(R.color.white);
            zxingTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
            zxingImg.setBackgroundResource(R.drawable.saoyisao_blue);
        }
        if (middle == true) {
            washView.setBackgroundResource(R.color.aixiyi_center_txt_blue);
            washTxt.setTextColor(getResources().getColor(R.color.white));
            washImg.setBackgroundResource(R.drawable.xiyi_white);
        } else {
            washView.setBackgroundResource(R.color.white);
            washTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
            washImg.setBackgroundResource(R.drawable.xiyi_blue);
        }
        if (right == true) {
            dingDanView.setBackgroundResource(R.color.aixiyi_center_txt_blue);
            dingDanTxt.setTextColor(getResources().getColor(R.color.white));
            dingDanImg.setBackgroundResource(R.drawable.dingdanwhite);
        } else {
            dingDanView.setBackgroundResource(R.color.white);
            dingDanTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
            dingDanImg.setBackgroundResource(R.drawable.dingdanblue);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800) {//如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(AiMainActivity.this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                MyApplication.exit();
            }
        }
        return super.onKeyUp(keyCode, event);
    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(TYPE_HEAD);
    }

    @Override
    public void onLoadMore() {
        handler.sendEmptyMessage(TYPE_FOOT);
    }
    @Override
    public void onStop() {
        baiDuMapUtil.onDestroy();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
