package com.taohdao.utils.db;

/**
 * Created by taohuadao on 2016/7/5.
 */
public class CityInfo {
    String id;
    String cityName;
    String provinceid;

    public void setId(String id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getProvinceid() {
        return provinceid;
    }
    public String getPickerViewText(){
        return cityName;
    }
    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    @Override
    public String toString() {
        return cityName;
    }
}
