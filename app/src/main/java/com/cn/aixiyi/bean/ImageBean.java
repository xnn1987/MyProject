package com.cn.aixiyi.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ImageBean {

    private List<String> imgList;
    private String imgStr;
    private String imgFrom;

    public String getImgFrom() {
        return imgFrom;
    }

    public void setImgFrom(String imgFrom) {
        this.imgFrom = imgFrom;
    }

    public String getImgStr() {
        return imgStr;
    }

    public void setImgStr(String imgStr) {
        this.imgStr = imgStr;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }
}
