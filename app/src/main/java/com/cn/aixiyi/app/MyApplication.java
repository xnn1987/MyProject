package com.cn.aixiyi.app;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.cn.aixiyi.R;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.Md5Utils;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.map.LocationService;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.Constants;

import org.ddq.common.util.JsonUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by xingdapeng on 2016/10/24.
 */
public class MyApplication extends Application {
    public static final String TAG = "MyApplication";
    private static MyApplication Instance = null;
    public static String uid;
    public static String device;
    public static String city = "深圳";
    public static String cityCode = "4403";
    public static double lac = 0;
    public static double lon = 0;
    public static int screenWidth;
    public static float density;//像素密度
    public static DisplayImageOptions options;
    public static DisplayImageOptions optionsHead;
    public static DisplayImageOptions optionsBaner;//banner
    public static Map<String, Object> userInfo = null;
    public static Map<String, Object> userQuanJu = null;
    public static ImageLoader imageLoader;
    public static String versionName = "";
    public static int versionCode = 0;
    public static String versionDes = "更新之后，精彩更多";//版本更新描述
    public static boolean needcookies = true;
    public static String cookies = "";
    public Vibrator mVibrator;
    private static List<Activity> activityList = new LinkedList<Activity>();
    public static Map<String, String> activityTitleMap = new HashMap<>();
    public static int sw, sh;
    public static String tianCaiMoney = "";
    public static float tianCaiMoneyF = 0;
    public static String washState = "1";
    public static boolean isLeft = false;
    public static IWXAPI api;
    public LocationService locationService;
    public static boolean isHorizontalScroll = true;

    @Override
    public void onCreate() {
        super.onCreate();

        getDPI();//  获取像素密度
        getWidthAndHight();//获取屏幕宽，高
        //   getDevidAndUid();
        initImageLoader(this);
        initImageLoad();
        initImageLoadHead();
        initImageLoadBanner();
        getVersionCodeAndName();
        initWeiXinAndZhifubao();
        checkPremisses();
        initBaiduMap();

    }


    private void initWeiXinAndZhifubao() {
        //微信api
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        //支付宝api

    }

    private void initBaiduMap() {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);
    }


    public void getDevidAndUid() {
        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        device = Md5Utils.string2MD5(tm.getDeviceId());//获取设备UUID
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(getApplicationContext(), Contanct.SP_USER_NAME);
        String userInfoStr = sharedPrefUtil.getString(Contanct.SP_USERINFO, null);
        userInfo = userInfoStr != null ? JsonUtil.getInstance().json2Object(userInfoStr, Map.class) : null;
    }

    public void getVersionCodeAndName() {
        try {
            // ---get the package info---
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
    }

    //  初始化信息
    private void initImageLoad() {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.pic_load)// 加载等待 时显示的图片
                .showImageForEmptyUri(R.drawable.pic_load)// 加载数据为空时显示的图片
                .showImageOnFail(R.drawable.pic_load)// 加载失败时显示的图片
                .cacheInMemory().cacheOnDisc() /**
         * .displayer(new
         * RoundedBitmapDisplayer(20))
         **/
                .build();
    }

    //  初始化  头像
    private void initImageLoadHead() {
        optionsHead = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.morentouxiang2)// 加载等待 时显示的图片
                .showImageForEmptyUri(R.mipmap.morentouxiang2)// 加载数据为空时显示的图片
                .showImageOnFail(R.mipmap.morentouxiang2)// 加载失败时显示的图片
                .cacheInMemory().cacheOnDisc()
                .build();
        /**
         * .displayer(new
         * RoundedBitmapDisplayer(20))
         **/
    }

    //  初始化  baner
    private void initImageLoadBanner() {
        optionsBaner = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.detail_banner)// 加载等待 时显示的图片
                .showImageForEmptyUri(R.mipmap.detail_banner)// 加载数据为空时显示的图片
                .showImageOnFail(R.mipmap.detail_banner)// 加载失败时显示的图片
                .cacheInMemory().cacheOnDisc() /**
         * .displayer(new
         * RoundedBitmapDisplayer(20))
         **/
                .build();
    }

    /**
     * 自定义的头像
     *
     * @param resImg
     */
    public DisplayImageOptions custImageOptions(int resImg) {

        DisplayImageOptions custOptions = new DisplayImageOptions.Builder()
                .showStubImage(resImg)// 加载等待 时显示的图片
                .showImageForEmptyUri(resImg)// 加载数据为空时显示的图片
                .showImageOnFail(resImg)// 加载失败时显示的图片
                .cacheInMemory().cacheOnDisc() /**
         * .displayer(new
         * RoundedBitmapDisplayer(20))
         **/
                .build();
        return custOptions;
    }

    private void getDPI() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        density = dm.density;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
    }

    public void getWidthAndHight() {

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // 获取屏幕的宽和高
        sw = display.getWidth();
        sh = display.getHeight();
    }

    //  初始化imageload
    public void initImageLoader(Context context) {
        imageLoader=ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(100)  // 可以缓存的文件数量
                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .build();
                imageLoader.init(config);
    }


    // 添加Activity到容器中
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public static void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    private void checkPremisses() {
        if (checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    public int getSw() {
        return sw;
    }


    public void setSw(int sw) {
        this.sw = sw;
    }


    public int getSh() {
        return sh;
    }


    public void setSh(int sh) {
        this.sh = sh;
    }
}
