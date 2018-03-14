package com.zwt.photoselect.model;

import java.io.Serializable;

/**
 * Created by zhangweitao on 2018/3/8.
 * 照片实体
 */

public class ImageModel implements Serializable {

    private String path;
    private String name;
    private long time;
    private boolean isChecked = false;

    public ImageModel(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
