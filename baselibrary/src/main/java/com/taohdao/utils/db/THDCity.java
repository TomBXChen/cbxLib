package com.taohdao.utils.db;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by taohuadao on 2016/7/5.
 */
public class THDCity implements IPickerViewData {
    private ProvinceInfo mProvinceInfo;
    private CityInfo mCityInfo;
    private AreaInfo mAreaInfo;

    public THDCity(ProvinceInfo mProvinceInfo, CityInfo mCityInfo, AreaInfo mAreaInfo) {
        this.mProvinceInfo = mProvinceInfo;
        this.mCityInfo = mCityInfo;
        this.mAreaInfo = mAreaInfo;
    }

    public ProvinceInfo getProvinceInfo() {
        return mProvinceInfo;
    }

    public CityInfo getCityInfo() {
        return mCityInfo;
    }

    public AreaInfo getAreaInfo() {
        return mAreaInfo;
    }

    @Override
    public String getPickerViewText() {
        if(mProvinceInfo!=null){
            return mProvinceInfo.getProvinceName();
        }else if(mCityInfo!=null){
            return mCityInfo.getPickerViewText();
        }else{
            return mAreaInfo.getPickerViewText();
        }
    }
}
