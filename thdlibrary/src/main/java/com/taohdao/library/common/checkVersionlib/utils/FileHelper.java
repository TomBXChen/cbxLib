package com.taohdao.library.common.checkVersionlib.utils;

import android.os.Environment;

import java.io.File;

public class FileHelper {

	public static String getDownloadApkCachePath() {

		String appCachePath = null;


		if (checkSDCard()) {
			appCachePath = Environment.getExternalStorageDirectory() + "/thdVersionPath/" ;
		} else {
			appCachePath = Environment.getDataDirectory().getPath() + "/thdVersionPath/" ;
		}
		File file = new File(appCachePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return appCachePath;
	}



	/**
	 *
	 */
	public static boolean checkSDCard() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		return sdCardExist;

	}



}