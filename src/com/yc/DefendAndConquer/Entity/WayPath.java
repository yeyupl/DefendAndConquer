package com.yc.DefendAndConquer.Entity;

import java.util.ArrayList;

import org.anddev.andengine.entity.modifier.PathModifier.Path;

import com.yc.DefendAndConquer.Data.Levels;


public class WayPath{
	
	private ArrayList<City> cityList;
	private City city,aimCity;
	private int level;
	private ArrayList<Integer> findIdList = new ArrayList<Integer>();
	
	public WayPath(ArrayList<City> cityList,City city,City aimCity,int level){
		this.cityList = cityList;
		this.city = city;
		this.aimCity = aimCity;
		this.level = level;
	}
	
	public boolean findWay(){
		ArrayList<Integer> idList = new ArrayList<Integer>();
		idList.add(this.city.getId());
		return this.find(idList,this.city.getId(), this.aimCity.getId());
	}
	private boolean find(ArrayList<Integer> idList,int id,int aimId){
		ArrayList<Integer> oldIdList = (ArrayList<Integer>) idList.clone();
		int[] related = Levels.getRelated(this.level,id);
		for(int i=0;i<related.length;i++){
			if(related[i]== aimId){
				idList.add(related[i]);
				this.findIdList = idList;
				this.print(idList,"found");
				return true;
			}
			if(this.isPass(related[i],idList)){
				idList.add(related[i]);
				if(this.find(idList,related[i],aimId)){
					return true;
				}else{
					idList = listClone(oldIdList,related[i]);
					this.print(idList,"next");
				}
			}
		}
		return false;
	}
	private boolean isPass(int id,ArrayList<Integer> idList){
		for(int mId:idList){
			if(mId == id){
				return false;
			}
		}
		int belong = this.getCity(id).getBelong();
		if(belong!=-1 && belong!=this.city.getBelong()){
			return false;
		}
		return true;
	}
	
	private ArrayList<Integer> listClone(ArrayList<Integer> idList,int id){
		ArrayList<Integer> newIdList = new ArrayList<Integer>();
		for(int mId:idList){
			if(mId != id){
				newIdList.add(mId);
			}else{
				break;
			}
		}
		return newIdList;
	}
	
	private City getCity(int id){
		for(City mCity:this.cityList){
			if(mCity.getId() == id){
				return mCity;
			}
		}
		return null;
	}
	
	public Path getPath(){
		if(this.findWay()){
			//this.print(this.findIdList,"path");
			Path path = new Path(this.findIdList.size());
			for(int id:this.findIdList){
				City mCity = this.getCity(id);
				Waypoint point = new Waypoint(mCity.getX(),mCity.getY(),mCity.getBelong());
				path.to(point.getX(), point.getY());
				//Log.d("+++++waypoint+++++",id+"---"+point.getX()+","+point.getY());
			}
			return path;
		}
		return null;
	}
	
	public void print(ArrayList<Integer> idList,String tag){
		String path = "";
		for(int id : idList){
			path += "->"+id;
		}
	  //Log.d("++++idList:"+tag+"++++",path);
	}
	//获取两点之间的距离
	public double getDist(Waypoint sPoint,Waypoint ePoint){
		return Math.sqrt((ePoint.getX()-sPoint.getX())*(ePoint.getX()-sPoint.getX())+(ePoint.getY()-sPoint.getY())*(ePoint.getY()-sPoint.getY()));
	}
	
}
