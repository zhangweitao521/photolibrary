package com.zwt.photoselect.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangweitao on 2018/3/8.
 * 文件夹实体
 */

public class FolderModel implements Serializable {

    private String name;
    private String path;
    private String firstImagePath;
    private int imageNum;
    private ArrayList<ImageModel> childImages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }

    public ArrayList<ImageModel> getChildImages() {
        return childImages;
    }

    public void setChildImages(ArrayList<ImageModel> childImages) {
        this.childImages = childImages;
    }
}
