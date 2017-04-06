package com.cn.aixiyi;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ddq.common.util.LogerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */
public class NearByDemo extends FragmentActivity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private Overlay mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    /**
     * 搜索关键字输入窗口
     */
    private EditText editCity = null;
    MapView mMapView;
    BaiduMap mBaiduMap;
    private List<String> suggest;
    boolean isFirstLoc = true; // 是否首次定位
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private Button mRequestLocation;
    private ListView mListView;
    private ListAdapter adapter;
    private int locType = 0;
    private double longitude = 0, latitude = 0;
    private float radius = 0, direction = 0;
    private String addrStr = "", province = "", city = "", district = "";
    private String tyleStr = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_nearby_lay);
        dataList = new ArrayList<PoiInfo>();
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        editCity = (EditText) findViewById(R.id.city);
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        sugAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        keyWorldsView.setThreshold(1);
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        keyWorldsView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }

                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(editCity.getText().toString()));
            }
        });
    }


    ArrayList<PoiInfo> dataList = new ArrayList<PoiInfo>();

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }

            locType = location.getLocType();
            Log.i("mybaidumap", "当前定位的返回值是：" + locType);

            longitude = location.getLongitude();
            latitude = location.getLatitude();
            if (location.hasRadius()) {// 判断是否有定位精度半径
                radius = location.getRadius();
            }

            if (locType == BDLocation.TypeNetWorkLocation) {
                addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
                Log.i("mybaidumap", "当前定位的地址是：" + addrStr);
            }

            direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
            province = location.getProvince();// 省份
            city = location.getCity();// 城市
            district = location.getDistrict();// 区县

            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

            //将当前位置加入List里面
            PoiInfo info = new PoiInfo();
            info.address = location.getAddrStr();
            info.city = location.getCity();
            info.location = ll;
            info.name = location.getAddrStr();
            dataList.add(info);

            Log.i("mybaidumap", "province是：" + province + " city是" + city + " 区县是: " + district);


            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            //画标志
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(ll);
            converter.from(CoordinateConverter.CoordType.COMMON);
            LatLng convertLatLng = converter.convert();

            OverlayOptions ooA = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));
            mCurrentMarker = mBaiduMap.addOverlay(ooA);


            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 10.0f);
            mBaiduMap.animateMapStatus(u);

            //画当前定位标志
            MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(uc);
            mMapView.showZoomControls(false);
            //poi 搜索周边
            searchNearbyProcess(location);
        }

        /**
         * 响应周边搜索按钮点击事件
         *
         * @param
         */
        public void searchNearbyProcess(BDLocation poiLocation) {

            LatLng center = new LatLng(poiLocation.getLatitude(), poiLocation.getLongitude());
            int radius = 5000;
            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption()
                    .keyword("洗衣")
                    .sortType(PoiSortType.distance_from_near_to_far)
                    .location(center)
                    .radius(radius)
                    .pageNum(0);
            mPoiSearch.searchNearby(nearbySearchOption);
        }

    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(NearByDemo.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        List<PoiInfo> data = new ArrayList<>();
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            MyPoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setResult(poiResult);
            overlay.addToMap();
            overlay.zoomToSpan();

            String strInfo = "在";
            for (PoiInfo poiInfo : poiResult.getAllPoi()) {
                strInfo += poiInfo.address;
                strInfo += ",";
            }
            strInfo += "找到结果";

            Log.i("PoiResult", strInfo);
//            Toast.makeText(NearByDemo.this, strInfo, Toast.LENGTH_LONG)
//                    .show();
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(NearByDemo.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }
        sugAdapter = new ArrayAdapter<String>(NearByDemo.this, android.R.layout.simple_dropdown_item_1line, suggest);
        keyWorldsView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    private class MyPoiOverlay extends OverlayManager {
        private PoiResult result;
        private boolean flag = false;

        public void setResult(PoiResult result) {
            this.result = result;
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
            PoiInfo poi = result.getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            Toast.makeText(NearByDemo.this, poi+poi.city+poi.name, Toast.LENGTH_LONG)
                    .show();
          LatLng latLng=  poi.location;
            double one=latLng.latitude;
            double two=latLng.longitude;
            double one1=latLng.latitudeE6;
            double two1=latLng.longitudeE6;
            Log.i("PoiResult",one+"");
            Log.i("PoiResult",two+"");
            Log.i("PoiResult",one1+"");
            Log.i("PoiResult",two1+"");
            return true;
        }

        @Override
        public boolean onPolylineClick(Polyline arg0) {
            return false;
        }

        @Override
        public List<OverlayOptions> getOverlayOptions() {
            List<OverlayOptions> ops = new ArrayList<OverlayOptions>();
            List<PoiInfo> pois = result.getAllPoi();
            OverlayOptions op = null;
           // BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.dituicon);
            for (int i = 0; i < pois.size(); i++) {
                TextView textView=new TextView(NearByDemo.this);
                textView.setBackground(getResources().getDrawable(R.mipmap.wash_dingwei));
                textView.setText(i+1+"");
                textView.setGravity(Gravity.CENTER);
                BitmapDescriptor bitmapView = BitmapDescriptorFactory.fromView(textView);
                op = new MarkerOptions().position(pois.get(i).location).icon(bitmapView);
                ops.add(op);
                mBaiduMap.addOverlay(op).setZIndex(i);
            }
            return ops;
        }
    }
}
