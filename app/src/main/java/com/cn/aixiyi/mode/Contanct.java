package com.cn.aixiyi.mode;

/**
 * Created by sty on 2016/7/12.
 */
public class Contanct {
    //用户名和密码
    public static final String USER_NAME = "xiaoya:1234QWER";
    public static final String SP_USER = "userlogin";
    public static final String SP_USERNAME = "name";
    public static final String SP_USERPASS = "pass";
    public static final String SP_USER_NAME = "user";
    public static final String SP_USER_UID = "uid";
    public static final String SP_USERINFO = "userinfo";
    // 存储sp的名字
    public static final String SP_UPDATA_APP = "updataApp";

    //上传类型
    public static final int ZXING_RESULT =50;
    public static final String UPLOAD_TPYE = "type";//  上传头像的类型  type
    public static final String UPLOAD_HEAD= "head";//  上传头像  type
    public static final String UPLOAD_ER_WEI_MA= "qrcode";//  上传二维码
    public static final String UPLOAD_PHOTO = "Photo";//  上传 图片
    public static final String UPLOAD_FEEDBACK = "feedback";//  上传  意见反馈 type
    public static final int PICTER_INTENT = 59;//  图片上传
    public static  String UPLOAD_STYLE= "";//  上传类型区分参数  type
    //普通网络加载
    public static final int LOADING = 7;
    public static final int LOAD_SUCCESS =8;
    public static final int LOAD_SUCCESS_WEIXIN = 9;
    public static final int LOAD_ERROR = 10;
    public static final int FOOT_REFRESH = 11;
    public static final int HEAD_REFRESH = 12;
    public static final int STOP_FRESH = 13;
    //  网络请求
    public static final String DIALOG_SHOW = "正在加载,请稍等...";
    public static final String DIALOG_UPLOADING = "正在上传,请等待...";
    public static final String DIALOG_ERROR = "网络请求失败";
    public static final String NO_ANYTHING_DATA = "没有新数据";
    public static final String DIALOG_DEL = "正在删除，请稍等...";
    public static final String DIALOG_UPLOAD = "正在提交...";

    //回到主页
    public static final String MAINACTIVTTY = "BackToAiMainActivity";
    //回到主页
    public static final String BASEACTIVITY = "BaseActivity";
    public static final String LEFTACTIVITY = "LeftActivity";
    //网络请求返回失败提示
    public static final String LoginBackFail = "登录失败!";
    public static final String RandomFail = "发送验证码失败!";
    public static final String RegisterFail = "注册失败!";
    public static final String ForgetPassFail = "密码找回失败!";
    public static final String UserInfoFail = "获取用户信息失败!";
    public static final String UpdateUserInfoFail = "更新用户信息失败!";
    public static final String RechargeFail = "充值失败!";

}
