package com.yc.DefendAndConquer.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	public static String formatTime(int time){
		int hour = 3600,min=60;
		String timeStr = "";
        if(time>=hour) {
            int h = (int) Math.floor(time/hour);
            timeStr += h<10?("0"+h):h;
            timeStr += ":";
            time -= h*hour;
        }else{
        	timeStr += "00:";
        }
        if(time>=min) {
            int m = (int) Math.floor(time/min);
            timeStr += m<10?("0"+m):m;
            timeStr += ":";
            time -= m*min;
        }else{
        	timeStr += "00:";
        }
        if(time>0){
        	timeStr += time<10?("0"+time):time;
        }else{
        	timeStr += "00";
        }
		return timeStr; 
	}
	
	public static boolean isNetwork(Context context){
		// 判断接入方式
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		// 如果联网
		if(info != null && info.isAvailable()){
			return true;
		}else {
			return false;
		}
	}

}
