package com.cn.aixiyi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.activity.MySaveActivity;
import com.cn.aixiyi.activity.UserLoginActivity;
import com.cn.aixiyi.activity.WashMachListActivity;
import com.cn.aixiyi.activity.WashingMachineActivity;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myAdapter.MyXyAddressAdapter;
import com.cn.aixiyi.myview.PullToRefreshView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragment;

import org.ddq.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */
public class MyXyAddressFragment extends BaseFragment implements  PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{
    private MySaveActivity fca;
    private View view=null;
    private ListView mLvXyAddress;
    private PullToRefreshView mPrfContain;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    private MyXyAddressAdapter mAdapter;
    private ImageView mIvJl;
    private int positions=0;
    private int currentPage = 1;
    private String messageId = "";
    private final int TYPE_NO = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
    private String washingAreaId = "";
    private String washingId = "";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TYPE_NO:
                    resetRefresh(-1,true);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fca= (MySaveActivity) getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_xymach_frag_lay, null);
        findViews(view);
        setAdapter();
        zeroStatus(data);
        sendType(TYPE_NO);
        return view;
    }


    private void findViews(View view) {
        mLvXyAddress= (ListView) view.findViewById(R.id.MMA_lv_msg);
        mIvJl = (ImageView) view.findViewById(R.id.MMA_iv_wujilu);
        mPrfContain = (PullToRefreshView) view.findViewById(R.id.MMA_ptr_contain);
        mPrfContain.setOnHeaderRefreshListener(this);
        mPrfContain.setOnFooterRefreshListener(this);
        mPrfContain.setHeadBackgroudResource(R.color.backgroud_f7f7f7);
        mPrfContain.setFootBackgroudResource(R.color.backgroud_f7f7f7);

    }

    private void setAdapter() {
        mAdapter = new MyXyAddressAdapter(fca, data);
        mLvXyAddress.setAdapter(mAdapter);
        mLvXyAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map=data.get(positions);
                Intent intent = new Intent();
                intent.setClass(getActivity(), WashMachListActivity.class);
                intent.putExtra("washingAreaId", TextUtil.getTextToString(map.get("washingAreaId")));
                intent.putExtra("washingArea", TextUtil.getTextToString(map.get("displayName")));
                intent.putExtra("freeNums", TextUtil.getTextToString(map.get("freeNums")));
                startActivity(intent);
            }
        });
        mLvXyAddress.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                Map<String, Object> map = data.get(position);
                positions=position;
                washingId = TextUtil.getTextToString(map.get("id"));
                showPopupWindow(view);
                return true;
            }
        });
    }
    public void getAddressData(final int type, boolean isDialog) {
        String addressUrl=Urls.WASHING_ADDRESS_URLS+"?p="+currentPage;
        MyOkHttpUtils.getRequest(addressUrl).execute(new MapCallback(fca,isDialog) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap!=null){
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        Map<String, Object> datamap = (Map<String, Object>) rMap.get("data");
                        List<Map<String, Object>> bannersList = (List<Map<String, Object>>) datamap.get("area");
                        DebugLog.e("Response", "rMap=" +rMap.toString());
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
    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(fca).inflate(
                R.layout.my_save_delete_lay, null);
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
                cancelSaveWashDetail();
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
//    private void cancelSaveWashDetail() {
//        String params = getCancelParams();
//        //https请求
//        MyYingShiVolleyUtils.getIntance(fca).OkHttpPostStringTypeOne(Urls.CANCEL_WASHING_URLS, params, new MyXiaoYaCallBack(fca) {
//            @Override
//            public void onResponse(Object response) {
//                if (!TextUtils.isEmpty(response.toString())) {
//                    DebugLog.e("Response", "MainActivity=" + response);
//                    Map<String, Object> map = JsonUtil.getInstance().json2Object(response.toString(), Map.class);
//                    int rcode = (int) map.get("rcode");
//                    if (rcode == 0) {
//                        data.remove(positions);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//            }
//        });
//
//    }
    private void cancelSaveWashDetail() {
        String params = getCancelParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.CANCEL_WASHING_URLS, map).execute(new MapCallback(fca) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        data.remove(positions);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    private String getCancelParams() {
        Map<String, Object> map = new HashMap<>();
        map.put("collectionId", washingId);
        String data = ParamsUtil.getParamsFromMap(map);
        DebugLog.e("Response", "data=" + data);
        return data;
    }
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        currentPage++;
        getAddressData(TYPE_FOOT,false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        resetRefresh(TYPE_HEAD,false);
    }

    //  重置刷新
    private void resetRefresh(int type,boolean isDialog){
        currentPage = 1;
        data.clear();
        getAddressData(type,isDialog);
    }
    /**
     * 是否有数据
     * @param list
     */
    private void zeroStatus(List list){
        mIvJl.setVisibility(list.size()>0? View.GONE:View.VISIBLE);
    }
    private void sendType(int type){
        handler.sendEmptyMessage(type);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
