package com.cn.aixiyi.mode;

/**
 * Created by xingdapeng on 2016/10/25.
 */
public class Urls {
    public final static String MAINURL = "http://api.chebaotec.com/washaccess";
    //   public final static String MAINURL = "https://api.chebaotec.com/washaccess";
 //   public final static String MAINURL = "http://192.168.1.168:8095";
    public final static String VERSION_URL = "";

    /**
     * 新版本下载
     * 暂未提供。。。                                                                                                                                          bbbbbbbbbbbbbbbbbbnnnnm,,././.,mnbv/50
     */
    public final static String BANBEN_UPDATE_download = "http://app-10058268.cos.myqcloud.com/versionName/AiXiYi.apk";
    //
    public final static String UserHeader = MAINURL + "";
    public final static String USER_LOGIN = MAINURL + "/um/login";
    public final static String USER_RANDOM = MAINURL + "/um/getValidCode";
    public final static String USER_REGISTER = MAINURL + "/um/registe";
    public final static String USER_INFO = MAINURL + "/um/getUserInfo";
    public final static String USER_INFO_UPDATE = MAINURL + "/um/updateUserInfo";
    public final static String USER_FORGET =  MAINURL +"/um/resetPass";
    public final static String ADS_URLS = "";
    public final static String MAININDEX_URLS = MAINURL +"/wash/getIndexInfo";
    public final static String WASHING_ADDRESS_URLS = MAINURL +"/wash/myCollection";
    public final static String WASHING_MASHINGNUM_URLS = MAINURL + "/wash/nearWashAreaList";
    public final static String WASHING_MASHINGNUMLIST_URLS = MAINURL + "/wash/freeWashingList";
    public final static String WASHING_MASHINGDETAIL_URLS = MAINURL + "/wash/washingDetail";
    public final static String WASHING_MASHINGDETAILADD_URLS = MAINURL + "/wash/addOrder";
    public final static String MYORDER_URLS = MAINURL + "/um/myOrder";
    public final static String WASH_URLS = MAINURL + "/um/updateWashingStep";
    public final static String MONEY_URLS = MAINURL + "/um/myWallet";
    public final static String ADSMONEY_URLS = "";
    public final static String MYMASSGE_URLS = MAINURL + "/um/myMessageCenter";
    public final static String SUGGEST_URLS = MAINURL + "/um/myFeedBack";
    public final static String EXITING_LOGIN_URLS = MAINURL + "/um/logout";
    public final static String RECHARE_URLS = MAINURL + "/um/getUser";
    public final static String PAYRESULT_URLS = "";
    public final static String ADSEXPLAIN_URLS = "";
    public final static String QIABAOEXPLAIN_URLS = "";
    public final static String MESSAGEDETAIL_URLS = MAINURL + "/um/myMessageCenterDetail";
    public final static String SUGGESTDEAILT_URLS = MAINURL + "/um/myFeedBackDetail";
    public final static String SUGGESTDEAILTSEND_URLS = MAINURL + "/um/addFeedBack";
    public final static String SETSUGGEST_URLS =MAINURL + "/um/addFeedBack";
    public final static String DELETE_DINGDAN_URLS = MAINURL +  "/wash/cancelOrder";
    public final static String SAVE_WASHING_URLS = MAINURL +  "/wash/addCollection";
    public final static String CANCEL_WASHING_URLS = MAINURL +  "/wash/cancelCollection";
    public final static String HEADER_UPLOGD_URLS = MAINURL +  "/uploadFile/uploadHead";
    public final static String SUGGEST_UPLOGD_URLS = MAINURL +  "/uploadFile/uploadFeedBack";
    // 设备ID
    public final static String DeviceUrl = MAINURL + "/inventory/managedObjects?fragmentType=c8y_IsDevice";
    //警报alarms
    public final static String AlarmsUrl = MAINURL + "/alarm/alarms";
    //警报alarmsalarmId
    public final static String AlarmsAlarmIdUrl = MAINURL + "/alarm/alarms/10300";
    public final static String MeasurementUrl = MAINURL + "/inventory/managedObjects/14526";
    // binaries
    public final static String Binaries = MAINURL + "/inventory/binaries";
    // notifications
    public final static String Notifications = MAINURL + "/devicecontrol/notifications";
    // s
    public final static String S = MAINURL + "/s/";

    //   module
    public final static String Model = MAINURL + "/cep/modules/2";
    //options
    public final static String Pptions = MAINURL + "/tenant/options/mycategory2/mykey2";
    // cep
    public final static String Cep = MAINURL + "/cep/modules";
    // user
    public final static String User = MAINURL + "/user/washingmachine/users";

}
