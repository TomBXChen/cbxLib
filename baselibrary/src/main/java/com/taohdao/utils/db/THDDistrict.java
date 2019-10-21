package com.taohdao.utils.db;

import java.io.Serializable;

/**
 * Created by admin on 2016/11/14.
 */

public class THDDistrict implements Serializable {
    private static final long serialVersionUID = -7828465625538567384L;
    public String provinceName;
    public String provinceId;//省份ID
    public String cityName;
    public String cityId;//城市ID
    public String districtName;
    public String districtId;//区域ID
    public String concatName;

    public THDDistrict() {
    }

    public THDDistrict(String provinceId, String cityId, String districtId) {
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.districtId = districtId;
    }

    public boolean checkArg(THDDistrict newTHDDistrict){
        if(this.provinceId.equals(newTHDDistrict.provinceId)&&
                this.cityId.equals(newTHDDistrict.cityId)&&
                this.districtId.equals(newTHDDistrict.districtId)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "provinceId='" + provinceId + '\'' +
                ",cityId='" + cityId + '\'' +
                ",districtId='" + districtId + '\'';
    }
}
