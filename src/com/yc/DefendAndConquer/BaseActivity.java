package com.yc.DefendAndConquer;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.os.Bundle;

import com.mobclick.android.MobclickAgent;
import com.yc.DefendAndConquer.Utils.Store;

public class BaseActivity extends BaseGameActivity{
	public float gold = 0f;
	public Store store;
	
	@Override
	public Engine onLoadEngine() {
		return null;
	}

	@Override
	public void onLoadResources() {
		
	}

	@Override
	public Scene onLoadScene() {
		return null;
	}

	@Override
	public void onLoadComplete() {
		
	}
	

	@Override
	public void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		
	       
		MobclickAgent.onError(this); 
		
		MobclickAgent.update(this); 
		
		this.store = new Store(this);

	}
	
	public void onPause(){
	    super.onPause();
	    MobclickAgent.onPause(this); 
	}

	public void onResume(){
		super.onResume();
		MobclickAgent.onResume(this);		
	 }
	  
	public void onDestroy(){
		 super.onDestroy();
	 }

}