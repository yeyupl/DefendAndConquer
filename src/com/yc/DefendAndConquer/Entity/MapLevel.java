package com.yc.DefendAndConquer.Entity;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;

import com.yc.DefendAndConquer.MapActivity;
import com.yc.DefendAndConquer.Utils.Store;

public class MapLevel extends AnimatedSprite {
	private Context context;
	private int level = 1;
	private Store store;

	public MapLevel(Context context,float pX, float pY,int level,TiledTextureRegion pTiledTextureRegion,Font pFont) {
		super(pX, pY, pTiledTextureRegion);
		this.context = context;
		this.level = level;
		this.store = new Store(context);
		this.attachChild(new ChangeableText(28,47,pFont, this.level+"", 1));
		if(!this.isUnLocked()){
			this.stopAnimation(1);
		}
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
		if(pSceneTouchEvent.isActionDown()) {
			this.setScale(1.25f);
			try{ 
				Thread.sleep(300); 
			}catch(InterruptedException   e){ 
				e.printStackTrace(); 
			} 
			this.setScale(1.0f);
			((MapActivity)MapLevel.this.context).showInfo(this);
		}	
		return false;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
	
	public boolean isUnLocked(){
		if( this.level>1 && !this.isActive()){
			return false;
		}
		return true;
	}
	
	public boolean isActive(){
		if(this.level==1 || this.store.getInt("time_"+(this.level-1))>0){
			return true;
		}
		return false;
	}
	
	public void flush(){
		if(!this.isUnLocked()){
			this.stopAnimation(1);
		}else{
			this.stopAnimation(0);
		}
	}

}