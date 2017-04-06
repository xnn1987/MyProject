package com.cn.aixiyi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.cn.aixiyi.NearByDemo;
import com.cn.aixiyi.OverlayManager;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/15.
 */
public class WashAddressMapActivity extends SlidBackActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private SDKReceiver mReceiver;
    private String washingId = "";
    private List<Map<String,Object>> mylist=new ArrayList<>();
    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                ToastUtil.ToastShow(WashAddressMapActivity.this,"key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                ToastUtil.ToastShow(WashAddressMapActivity.this,"key 验证成功! 功能可以正常使用");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                ToastUtil.ToastShow(WashAddressMapActivity.this,"网络出错");
            }
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wash_address_map_lay);
        SetTitleBar.setTitleText(this,"附近洗衣机");
        Intent intent=getIntent();
        mylist = (List<Map<String, Object>>)intent.getSerializableExtra("lists");
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        if (mylist.size()>0){
            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);
            mBaiduMap.clear();
            //定义Maker坐标点
            //构建Marker图标  ，这里可以自己替换
            MyPoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setResult(mylist);
            overlay.addToMap();
            overlay.zoomToSpan();
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(20).build()));
        }else {
            LatLng convertLatLng = new LatLng(MyApplication.lac,MyApplication.lon);
            OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.dingwei_map));
            mBaiduMap.addOverlay(ooA);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 18.0f);
            mBaiduMap.animateMapStatus(u);
            //画当前定位标志
            MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(convertLatLng);
            mBaiduMap.animateMapStatus(uc);
            mMapView.showZoomControls(true);
        }
    }
    private class MyPoiOverlay extends OverlayManager {
        private List<Map<String,Object>> data=null;

        public void setResult( List<Map<String,Object>> data) {
            this.data = data;
        }

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            onClick(marker.getZIndex());
            return true;
        }

        public boolean onClick(int index) {
//            Map<String, Object> map=data.get(index);
//            Intent intent = new Intent();
//            intent.setClass(WashAddressMapActivity.this, WashMachListActivity.class);
//            intent.putExtra("washingAreaId", TextUtil.getTextToString(map.get("washingAreaId")));
//            intent.putExtra("washingArea", TextUtil.getTextToString(map.get("displayName")));
//            startActivity(intent);
//            finish();
            return true;
        }

        @Override
        public boolean onPolylineClick(Polyline arg0) {
            return false;
        }

        @Override
        public List<OverlayOptions> getOverlayOptions() {
            List<OverlayOptions> ops = new ArrayList<OverlayOptions>();
            OverlayOptions op = null;
            // BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.dituicon);
            if (data.size()>0){
                for (int i = 0; i <data.size(); i++) {
                    Map<String,Object> map= data.get(i);
                    String lac=TextUtil.getTextToString(map.get("latitude")) ;
                    String lon= TextUtil.getTextToString(map.get("longitude"));
                    String numStr=TextUtil.getTextToString(map.get("freeNums"));
                    TextView textView=new TextView(WashAddressMapActivity.this);
                    textView.setBackground(getResources().getDrawable(R.mipmap.dingwei_map));
                    textView.setText(numStr+"台");
                    textView.setTextSize(getResources().getDimension(R.dimen.aixiyi_little_smaller));
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setGravity(Gravity.CENTER);
                    LatLng ll=new LatLng(Double.parseDouble(lac),Double.parseDouble(lon));
                    BitmapDescriptor bitmapView = BitmapDescriptorFactory.fromView(textView);
                    op = new MarkerOptions().position(ll).icon(bitmapView);
                    ops.add(op);
                    mBaiduMap.addOverlay(op).setZIndex(i);
                }
            }
            return ops;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
    }
}
