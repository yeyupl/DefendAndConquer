package com.yc.DefendAndConquer.Entity;

import java.util.ArrayList;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;

import android.content.Context;

import com.yc.DefendAndConquer.GameActivity;

public class Army extends AnimatedSprite {
	
	private Context context;
	
	private City city;
	private int num =1;
	private int attack = 1;  //单体攻击力 抵消人口
	private int speed = 100; //移动速度  每秒距离
	
	public Army(Context context,City city, int num,TiledTextureRegion pTiledTextureRegion,Font pFont) {
		super(city.getX(), city.getY(), pTiledTextureRegion);
		this.city = city;
		this.num = num;
		this.context = context;
		this.attachChild(new ChangeableText(0,0,pFont, this.num+"", 2));
	}
	
	
	//出发
	public void depart(ArrayList<City> cityList,final City aimCity,final Path path){
		this.registerEntityModifier(new LoopEntityModifier(new PathModifier(path.getLength()/this.speed, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {

			}
			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
				float[] pointsX = path.getCoordinatesX();
				float[] pointsY = path.getCoordinatesY();
				
				double angle = Army.this.getAngle(pointsX[pWaypointIndex],pointsY[pWaypointIndex],pointsX[pWaypointIndex+1],pointsY[pWaypointIndex+1]);
				double abcAngle = Math.abs(angle);
				long[] stepDuration = new long[]{100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100};
				if(abcAngle<=22.5){
					//右直
					Army.this.animate(stepDuration, 0, 15, true);
				}else if(abcAngle<=67.5){
					if(angle<0){
						//右上
						Army.this.animate(stepDuration, 16, 31, true);
					}else{
					  //右下
						Army.this.animate(stepDuration, 96, 111, true);
					}
				}else if(abcAngle<=112.5){
					if(angle<0){
						//上直
						Army.this.animate(stepDuration, 32, 47, true);
					}else{
						//下直
						Army.this.animate(stepDuration, 112, 127, true);
					}
				}else if(abcAngle<=157.5){
					if(angle<0){
						//左上
						Army.this.animate(stepDuration, 48, 63, true);
					}else{
						//左下
						Army.this.animate(stepDuration, 80, 95, true);
					}
				}else{
					//左直
					Army.this.animate(stepDuration, 64, 79, true);
				}
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				((GameActivity)Army.this.context).runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						 aimCity.battle(Army.this.city.getBelong(), Army.this.num);
						((GameActivity)Army.this.context).getScene().detachChild(Army.this);
					}
				});
				
			}
		}, EaseSineInOut.getInstance()),1));
	}
	
	//攻击力
	public int getAttack(){
		return this.attack;
	}

	public void setAttack(int attack){
		this.attack = attack;
	}
	
	private double getAngle(float x1,float y1,float x2,float y2){
	     return  (Math.atan2((y2-y1), (x2-x1))*180/Math.PI);
	}


}