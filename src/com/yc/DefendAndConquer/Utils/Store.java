package com.yc.DefendAndConquer.Utils;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Store {

	private SharedPreferences sp;
	private Editor editor;
	private String name = "CMDU";
	private int mode = Context.MODE_PRIVATE;

	public Store(Context context) {
		this.sp = context.getSharedPreferences(name, mode);
		this.editor = sp.edit();
	}

	public void set(Map<String, String> map) {
		Set<String> set = map.keySet();
		for (String key : set) {
			editor.putString(key, map.get(key));
		}
		editor.commit();
	}

	public void set(String key, String val) {
		editor.putString(key, val);
		editor.commit();
	}

	public void set(String key, int val) {
		editor.putInt(key, val);
		editor.commit();
	}

	public void deleteAll() throws Exception {
		editor.clear();
		editor.commit();
	}

	public void delete(String key) throws Exception {
		editor.remove(key);
		editor.commit();
	}

	public String get(String key) {
		if (sp != null) {
			return sp.getString(key, "");
		}
		return "";
	}
	
	public String get(String key,String defVal) {
		if (sp != null) {
			return sp.getString(key, defVal);
		}
		return defVal;
	}
	
	public int getInt(String key) {
		if (sp != null) {
			return sp.getInt(key, 0);
		}
		return 0;
	}

}
