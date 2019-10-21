package com.taohdao.utils.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.taohdao.base.BaseApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taohuadao on 2016/7/5.
 */
public class CityDBManger {
    private static final int BUFFER_SIZE = 10000;
    public static final String DB_NAME = "countryList.db"; //保存的数据库文件名

//    public static final String PACKAGE_NAME = "com.taohdao.master";//工程包名
    public static final String PACKAGE_NAME =  BaseApp.getInstance().getPackageName();//工程包名
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME + "/databases";  //在手机里存放数据库的位置
    private static CityDBManger sInstance;
    private static SQLiteDatabase sqliteDB;

    public static CityDBManger getInstance() {
        if (sInstance == null) {
            sInstance = new CityDBManger();
        }
        return sInstance;
    }

    public CityDBManger() {
        if (sqliteDB == null) {
            sqliteDB = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
        }
    }
    public static void copyDatabase() {
        String dbPath = DB_PATH + "/" + DB_NAME;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            File dbfile = new File(dbPath);
            if(dbfile.exists()){
                Log.e("copyDatabase", "不用重复复制");
                return;
            }

            //执行数据库导入
            InputStream is = BaseApp.getInstance().getResources().getAssets().open("countryList.db"); //欲导入的数据库
            FileOutputStream fos = new FileOutputStream(dbPath);
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();//关闭输出流
            is.close();//关闭输入流
            Log.e("copyDatabase", "复制完了");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected final SQLiteDatabase sqliteDB() {
        open();
        return sqliteDB;
    }

    private void open() {
        if (sqliteDB == null) {
            sqliteDB = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
        }
    }

    private void closeDB() {
        if (sqliteDB != null) {
            sqliteDB.close();
            sqliteDB = null;
        }
    }

    public static THDDistrict getProvinceName(String provinceId){
        THDDistrict district = null;
        String provinceName = "";
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery("select * from province where id=?", new String[]{provinceId});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("provinceName"));
                    district = new THDDistrict(id,null,null);
                    district.provinceName = name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return district;
    }

    /**
     * 搜索区域名称，如果没区域就去搜城市名称
     * @param areaName
     * @return返回该区域或城市的ID
     */
    public static THDDistrict getAreaId(String areaName,String cityName){
        THDDistrict district = null;
        Cursor cursor = null;
        String areaNewName = null;
        if(areaName.endsWith("区")){
            if(areaName.length() == 2){//解决数据库空格问题
                areaNewName  = areaName.substring(0,areaName.length()-1)+"　"+"区";
            }
        }else if(areaName.endsWith("市")){
           return getCityId(cityName);
        }
        try {
            cursor = getInstance().sqliteDB().rawQuery("select * from area where areaName like '%"+(TextUtils.isEmpty(areaNewName)?areaName:areaNewName)+"%'", null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String districtId = cursor.getString(cursor.getColumnIndex("id"));
                    String provinceId = cursor.getString(cursor.getColumnIndex("provinceId"));
                    String cityId = cursor.getString(cursor.getColumnIndex("cityId"));
                    district = new THDDistrict(provinceId,cityId,districtId);
                    break;
                }
            }else{
                return getCityId(cityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return district;
    }
    public static THDDistrict getCityId(String cityName){
        THDDistrict district = null;
        Cursor cursor = null;
        String cityNewName = null;
        if(cityName.endsWith("市")){
            cityNewName  = cityName.substring(0,cityName.length()-1);
        }
        try {
            cursor = getInstance().sqliteDB().rawQuery("select * from city where cityName like '%"+(TextUtils.isEmpty(cityNewName)?cityName:cityNewName)+"%'", null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String cityId = cursor.getString(cursor.getColumnIndex("id"));
                    String provinceId = cursor.getString(cursor.getColumnIndex("provinceId"));
                    district = new THDDistrict(provinceId,cityId,null);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return district;
    }

    public static  THDDistrict  getCityName(String cityId){
        THDDistrict district = null;
        String cityName = "";
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery("select * from city where id=?", new String[]{cityId});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("cityName"));
                    district = new THDDistrict(null,id,null);
                    district.cityName = name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return district;
    }


    public static THDDistrict getAreaName(String areaId){
        THDDistrict district = null;
        String areaName = "";
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery("select * from area where id=?", new String[]{areaId});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("areaName"));
                    district = new THDDistrict(null,null,id);
                    district.districtName = name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return district;
    }

    /**
     * @param provinceId
     * @param isAddTotal  是否在城市前面加"所有城市"
     * @return
     */
    //根据省份id获取城市
    public static List<CityInfo> getCityList(String provinceId,boolean isAddTotal) {
        List<CityInfo> cityList = new ArrayList<CityInfo>();
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery("select * from city where provinceId=?", new String[]{provinceId});
            if (cursor != null && cursor.getCount() > 0) {
                if(isAddTotal){
                    CityInfo ci = new CityInfo();
                    ci.setId(provinceId);
                    ci.setCityName("所有城市");
                    ci.setProvinceid(provinceId);
                    cityList.add(ci);
                }
                while (cursor.moveToNext()) {
                    String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
                    String cityId = cursor.getString(cursor.getColumnIndex("id"));
                    CityInfo ci = new CityInfo();
                    ci.setId(cityId);
                    ci.setCityName(cityName);
                    ci.setProvinceid(provinceId);
                    cityList.add(ci);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return cityList;
    }

    /**
     * @param isAddTotal 是否在省份前面加"全国"
     * @return
     */
    //加载省份数据
    public static List<ProvinceInfo> getProvinceList(boolean isAddTotal) {
        List<ProvinceInfo> provinceList = new ArrayList<ProvinceInfo>();
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery("select * from province", null);
            if (cursor != null && cursor.getCount() > 0) {
                if(isAddTotal){
                    ProvinceInfo pi = new ProvinceInfo();
                    pi.setId("0");
                    pi.setProvinceName("全国");
                    provinceList.add(pi);
                }
                while (cursor.moveToNext()) {
                    String provinceName = cursor.getString(cursor.getColumnIndex("provinceName"));
                    String provinceId = cursor.getString(cursor.getColumnIndex("id"));
                    ProvinceInfo pi = new ProvinceInfo();
                    pi.setId(provinceId);
                    pi.setProvinceName(provinceName);
                    provinceList.add(pi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return provinceList;
    }

    /**
     * @param provinceId
     * @param cityId
     * @param isAddTotal 是否加“所有区域”
     * @return
     */
    //根据省份id和城市id获取区县
    public static  List<AreaInfo> getAreaList(String provinceId, String cityId,boolean isAddTotal) {
        List<AreaInfo> areaList = new ArrayList<AreaInfo>();
        Cursor cursor = null;
        try {
            if (provinceId != null && cityId != null) {
                cursor = getInstance().sqliteDB().rawQuery("select * from area where provinceId=? and cityId=?", new String[]{provinceId, cityId});
                if (cursor != null && cursor.getCount() > 0) {
                    if(isAddTotal){
                        AreaInfo ai = new AreaInfo();
                        ai.setId(cityId);
                        ai.setAreaName("所有区域");
                        areaList.add(ai);
                    }
                    while (cursor.moveToNext()) {
                        String areaName = cursor.getString(cursor.getColumnIndex("areaName"));
                        String areaId = cursor.getString(cursor.getColumnIndex("id"));
                        AreaInfo ai = new AreaInfo();
                        ai.setId(areaId);
                        ai.setAreaName(areaName);
                        areaList.add(ai);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return areaList;
    }

}
