package com.cn.aixiyi.Interface;


/**
 * Created by sty on 2016/7/5.
 */
public interface MyCallBack {
    /**
     * UI Thread
     */
    public void onBefore();
    /**
     * UI Thread
     *
     * @param
     */
    public void onAfter();
    void onResponse(Object response);
    void onError(Exception e);
}
