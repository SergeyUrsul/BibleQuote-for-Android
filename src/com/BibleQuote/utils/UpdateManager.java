package com.BibleQuote.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.BibleQuote.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class UpdateManager {
	
	private final static String TAG = "UpdateManager";
	
	static public void Init(Context context) {

		SharedPreferences Settings = PreferenceManager.getDefaultSharedPreferences(context);

		// Инициализация каталога программы
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File dir_modules = new File(DataConstants.FS_EXTERNAL_DATA_PATH);
			if (!dir_modules.exists()) {
				Log.i(TAG, String.format("Create directory %1$s", dir_modules));
				dir_modules.mkdirs();
			}
		}

		int currVersionCode = Settings.getInt("versionCode", 0);
		
		if (currVersionCode == 0 && Environment.MEDIA_MOUNTED.equals(state)) {
			Log.i(TAG, "Copying built-in module the Bible on external storage");
			saveExternalModule(context);
		}
		if (currVersionCode < 30) {
			Log.i(TAG, "Update to version 0.04.05");
			Settings.edit().remove("myversion");
			File cacheDir = context.getCacheDir();
			for (File currFile : cacheDir.listFiles()) {
				android.util.Log.i(TAG, String.format("Delete library cache file %1$s", currFile.getName()));
				currFile.delete();
			}
		}
		
		try {
			int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			Settings.edit().putInt("versionCode", versionCode).commit();
		} catch (NameNotFoundException e) {
			Settings.edit().putInt("versionCode", 30).commit();
		}
	}

	private static void saveExternalModule(Context context) {
		try {
			InputStream moduleStream = context.getResources().openRawResource(R.raw.rst);
			File moduleDir = new File(DataConstants.FS_EXTERNAL_DATA_PATH);
			OutputStream newModule = new FileOutputStream(new File(moduleDir, "rst.zip"));
			byte[] buf = new byte[1024];
			int len;
			while ((len = moduleStream.read(buf)) > 0) {
				newModule.write(buf, 0, len);
			}
			moduleStream.close();
			newModule.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
