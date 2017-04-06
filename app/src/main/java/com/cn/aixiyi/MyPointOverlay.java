package com.cn.aixiyi;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */
public class MyPointOverlay extends OverlayManager {
    private PoiResult result;
    private boolean flag = false;

    public void setResult(PoiResult result) {
        this.result = result;
    }

    public MyPointOverlay(BaiduMap baiduMap) {
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
//        mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
//                .poiUid(poi.uid));
        // }
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
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.wash_dingwei);
        for (int i = 0; i < pois.size(); i++) {
            op = new MarkerOptions().position(pois.get(i).location).icon(bitmap);
            ops.add(op);
            mBaiduMap.addOverlay(op).setZIndex(i);
        }
        return ops;
    }
}
