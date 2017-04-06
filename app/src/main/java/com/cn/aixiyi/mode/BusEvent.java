package com.cn.aixiyi.mode;

/**
 * Created by sty on 2016/7/5.
 */
public class BusEvent {
    private int mType = 0;

    private Object mParam;

    private String mStrParam;

    private int mIntParam;

    public static  final int UPLOAD_SUCCESS=0;
    public static  final int UPLOAD_FAIL=1;
    public static  final int LOGIN_SUCCESS=2;
    public static  final int LOGIN_FAIL=3;
    public static  final int REGIN_SUCCESS=4;
    public static  final int REGIN_FAIL=5;
    public static  final int YZM_SUCCESS=6;
    public static  final int YZM_FAIL=7;
    public static  final int VERSION_SUCCESS=8;
    public static  final int VERSION_FAIL=9;
    public static  final int DOWNLOAD_SUCCESS=10;
    public static  final int DOWNLOAD_FAIL=11;
    public static  final int EXIT_SUCCESS=12;
    public static  final int  EXIT_FAIL=13;
    public BusEvent() {
    }

    public BusEvent(int mType, Object mParam) {
        this.mType = mType;
        this.mParam = mParam;
    }

    public BusEvent(int mType, String mStrParam) {
        this.mType = mType;
        this.mStrParam = mStrParam;
    }

    public BusEvent(int mType, int mIntParam) {
        this.mType = mType;
        this.mIntParam = mIntParam;
    }

    public BusEvent(int mType, int mIntParam, Object mParam, String mStrParam) {
        this.mType = mType;
        this.mIntParam = mIntParam;
        this.mParam = mParam;
        this.mStrParam = mStrParam;
    }

    public BusEvent(int mType, Object mParam, String mStrParam, int mIntParam) {
        this.mType = mType;
        this.mParam = mParam;
        this.mStrParam = mStrParam;
        this.mIntParam = mIntParam;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public Object getmParam() {
        return mParam;
    }

    public void setmParam(Object mParam) {
        this.mParam = mParam;
    }

    public String getmStrParam() {
        return mStrParam;
    }

    public void setmStrParam(String mStrParam) {
        this.mStrParam = mStrParam;
    }

    public int getmIntParam() {
        return mIntParam;
    }

    public void setmIntParam(int mIntParam) {
        this.mIntParam = mIntParam;
    }


}
