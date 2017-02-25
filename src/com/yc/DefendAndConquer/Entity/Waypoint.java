package com.yc.DefendAndConquer.Entity;

import android.graphics.Point;

public class Waypoint extends Point {
	
	private float x = 0,y = 0;
	private int belong = -1;
	
	public Waypoint(float pX,float pY){
		this.x = pX;
		this.y = pY;
	}
	

	public Waypoint(float pX,float pY,int belong){
		this.x = pX;
		this.y = pY;
		this.belong = belong;
	}
	
	public float getX(){
		if(this.belong!=-1){
			return this.x+50;
		}
		return this.x;
	}
	
	public float getY(){
		if(this.belong!=-1){
			return this.y+64;
		}
		return this.y;
	}
	
	public void setBelong(int belong){
		this.belong = belong;
	}
	
	public int getBelong(){
		return this.belong;
	}
	
}