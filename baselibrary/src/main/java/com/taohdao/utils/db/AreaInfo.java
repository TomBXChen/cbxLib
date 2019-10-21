package com.taohdao.utils.db;

/**
 * Created by taohuadao on 2016/7/5.
 */
public class AreaInfo {
    String id;
    String areaName;

    public void setId(String id) {
        this.id = id;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getId() {
        return id;
    }

    public String getAreaName() {
        return areaName;
    }
    public String getPickerViewText(){
        return areaName;
    }
    @Override
    public String toString() {
        return areaName;
    }
}
