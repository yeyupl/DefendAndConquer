package com.yc.DefendAndConquer.Utils;

import android.content.Context;

public class SoundUtil {

	private Store store;

	public SoundUtil(Context context) {
		this.store = new Store(context);
	}
	
	public boolean isMusic(){
		if(this.store.getInt("music")==0){
			return true;
		}
		return false;
	}
	
	public void setMusicOn(boolean is){
		if(is){
			this.store.set("music",0);
		}else{
			this.store.set("music",1);
		}
	}
	
	public boolean isSound(){
		if(this.store.getInt("sound")==0){
			return true;
		}
		return false;
	}
	
	public void setSoundOn(boolean is){
		if(is){
			this.store.set("sound",0);
		}else{
			this.store.set("sound",1);
		}
	}

}
