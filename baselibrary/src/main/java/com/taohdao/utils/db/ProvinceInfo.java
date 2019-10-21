package com.taohdao.utils.db;

/**
 * Created by taohuadao on 2016/7/5.
 * 省份
 */
public class ProvinceInfo {
    String id;
    String provinceName;

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getPickerViewText(){
        return provinceName;
    }

    public String getId() {
        return id;
    }

    //重写toString方法填充数据到下拉框
    @Override
    public String toString() {
        return provinceName;
    }
}
