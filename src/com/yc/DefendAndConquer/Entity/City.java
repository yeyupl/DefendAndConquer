package com.yc.DefendAndConquer.Entity;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.yc.DefendAndConquer.GameActivity;
import com.yc.DefendAndConquer.Utils.SoundUtil;

public class City extends AnimatedSprite {
	
	private Context context;
	private AnimatedSprite selectSprite,upgradeSprite,upgradingSprite,fightingSprite;
	private ChangeableText populationText;
	private int level = 1;  //当前等级
	private int population = 0;  //当前人口
	private int populationStep = 10; //每级增加人口
	private int maxLevel = 5; //最高等级
	private int goldInc = 1; //每次增长金币基数  基数*等级
	private int goldLose = 10; //被占领对方获得金币基数  基数*等级
	private int populationSpeed = 1; //人口增长和减少速度，每多少秒增减一个人口
	private int belong = 0; //归属 未占领为0 自己方1 对方2;
	private int upgradeTime = 5 ; //升级需要时间
	private boolean isUpgrading = false; //是否升级中
	private boolean isSelected = false; //是否选中当前城市
	private GestureDetector doubleTapDetector;
	private int id =0;  //ID 索引
	private SoundUtil sound;
	
	public City(Context pContext,float pX, float pY,int belong,TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
		this.context = pContext;
		this.belong = belong;
		this.sound = new SoundUtil(this.context);
		if(this.belong!=-1){
			//-1为路径点
			this.registerUpdateHandler(new TimerHandler(0.1f, true, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					City.this.isUpgrade();
				}
			}));
			this.doubleTapDetector = new GestureDetector(new DoubleTapGestureListener());
		}
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
		this.doubleTapDetector.onTouchEvent(pSceneTouchEvent.getMotionEvent());
		if(pSceneTouchEvent.isActionDown()) {
			if(this.belong==1){
				//选择
				if(City.this.sound.isSound()){
					((GameActivity)City.this.context).mSoundClick.play();
				}
				this.selected(true);
			}else{
				//攻击
				((GameActivity)City.this.context).attack(this);
			}
		}	
		if(City.this.isUpgrade()){
			return false;
		}
		return true;
	}
	
	public void addSelected(TiledTextureRegion pTiledTextureRegion){
		if(this.selectSprite == null){
			this.selectSprite = new AnimatedSprite(0, 20, pTiledTextureRegion);
			this.selected(false);
			this.attachChild(this.selectSprite);
		}
	}
	
	public void selected(boolean isSelect){
		this.isSelected = isSelect;
		if(this.selectSprite != null){
			this.selectSprite.setVisible(isSelect);
			if(isSelect){
				this.selectSprite.animate(100, true);
			}else{
				this.selectSprite.stopAnimation();
			}
		}
	}
	
	public boolean isSelected(){
		return this.isSelected;
	}
	
	public void addText(Font pFont){
		if(this.populationText == null){
			this.populationText = new ChangeableText(48,110,pFont, "0/10", 5);
			this.attachChild(this.populationText,1);
			this.registerUpdateHandler(new TimerHandler(this.populationSpeed, true, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					//占领状态的才会有人口自动变化
					if(City.this.getPopulation()>City.this.getMaxPopulation()){
						//当前人口大于最高人口     减少
						City.this.population = City.this.getPopulation()-1;
					}else if(City.this.getPopulation()<City.this.getMaxPopulation()){
						//当前人口小于最高人口   增长
						City.this.population = City.this.getPopulation()+1;;
					}
					City.this.setText();
				}
			}));
		}
	}
	
	public void setText(){
		this.populationText.setText(this.getPopulation()+"/"+this.getMaxPopulation());
	}
	
	
	public void addUpgrade(TiledTextureRegion pTiledTextureRegion){
		if(this.upgradeSprite == null){
			this.upgradeSprite = new AnimatedSprite(80, 40, pTiledTextureRegion){
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
					City.this.doubleTapDetector.onTouchEvent(pSceneTouchEvent.getMotionEvent());
					if(pSceneTouchEvent.isActionDown()) {
						if(City.this.isUpgrade()&& City.this.upgradeSprite.isVisible()){
							if(City.this.sound.isSound()){
								((GameActivity)City.this.context).mSoundClickUpgrade.play();
							}
							City.this.upgrade();
							City.this.selected(false);
						}
					}	
					return true;
				}
			};
			this.upgradeSprite.animate(500, true);
			this.isUpgrade();
			this.attachChild(this.upgradeSprite);
		}
	}
	
	public void addUpgrading(TiledTextureRegion pTiledTextureRegion){
		if(this.upgradingSprite == null){
			this.upgradingSprite = new AnimatedSprite(25, 10, pTiledTextureRegion);
			this.upgradingSprite.setVisible(false);
			this.attachChild(this.upgradingSprite);
		}
	}
	
	public void addFighting(TiledTextureRegion pTiledTextureRegion){
		if(this.fightingSprite == null){
			this.fightingSprite = new AnimatedSprite(25, 15, pTiledTextureRegion);
			this.fightingSprite.setVisible(false);
			this.registerUpdateHandler(new TimerHandler(5, true, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					if(City.this.fightingSprite.isVisible() && City.this.fightingSprite.isAnimationRunning()){
						City.this.fightingSprite.stopAnimation();
						City.this.fightingSprite.setVisible(false);
					}
				}
			}));
			this.attachChild(this.fightingSprite);
		}
	}
	
	public AnimatedSprite getUpgradeSprite(){
		return this.upgradeSprite;
	}
	
	//刷新显示
	public void flush(){
		this.stopAnimation(this.belong * 5 + this.level - 1);
		this.setText();
	}
	
	
	//设置归属
	public void setBelong(int belong){
		this.belong = belong;
		this.flush();
	}
	
	public int getBelong(){
		return this.belong;
	}
	
	//是否可升级
	public boolean isUpgrade(){
		if(this.upgradeSprite != null){
			if(!this.isUpgrading && this.level<this.maxLevel && this.getPopulation() > this.getMaxPopulation()/2){
				if(this.belong==1){
					this.upgradeSprite.setVisible(true);
				}
				return true;
			}else{
				this.upgradeSprite.setVisible(false);
			}
		}
		return false;
	}
	
	//升级
	public void upgrade(){
		if(this.isUpgrade()){
			this.isUpgrading = true;
			this.population = this.getPopulation() - this.getMaxPopulation()/2;
			this.flush();
			if(this.upgradingSprite != null){
				this.upgradingSprite.animate(300, true);
				this.upgradingSprite.setVisible(true);
			}
			this.registerUpdateHandler(new TimerHandler(this.upgradeTime, false, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					City.this.level++;
					City.this.flush();
					City.this.isUpgrading = false;
					if(City.this.upgradingSprite != null){
						City.this.upgradingSprite.stopAnimation();
						City.this.upgradingSprite.setVisible(false);
					}
					if(City.this.sound.isSound()){
						((GameActivity)City.this.context).mSoundUpgraded.play();
					}
				}
			}));
		}
	}
	
	//当前最高人口
	public int getMaxPopulation(){
		return this.level * this.populationStep;
	}

	
	//当前人口
	public int getPopulation(){
		return this.population>=0?this.population:0;
	}
	
	public void setPopulation(int population){
		this.population = population;
	}
	//人口满
	public void setPopulationFull(){
		this.setPopulation(this.getMaxPopulation());
	}
	
	//当前等级
	public int getLevel(){
		return this.level;
	}
	
	public void setLevel(int level){
		this.level = level;
		this.flush();
	}
	
	//最高等级
	public int getMaxLevel(){
		return this.maxLevel;
	}
	
	public void setMaxLevel(int level){
		this.maxLevel = level;
	}
	
	//占领获得金币
	public int getGoldLose(){
		return (this.level-1)*this.goldLose;
	}
	
	//自然增长金币
	public int getGoldInc(){
		return this.level * this.goldInc;
	}
	
    //人口增长速度
	public int getPopulationSpeed(){
		return this.populationSpeed;
	}

	
	//战斗过程
	//belong 攻击方 等于当前占领方即为增援  num 军队数量
	public void battle(int belong,int num){
		if(belong == this.belong){
			this.population +=num;
		}else{
			this.population -=num;
			if(!this.fightingSprite.isVisible()){
				this.fightingSprite.animate(300, true);
				this.fightingSprite.setVisible(true);
			}
			if(City.this.sound.isSound()){
				((GameActivity)this.context).mSoundFight.play();
			}
		}
		if(this.getPopulation()==0){
			if(belong == this.belong){
				if(City.this.sound.isSound()){
					((GameActivity)this.context).mSoundLoseCity.play();
				}
			}else{
				if(City.this.sound.isSound()){
					((GameActivity)this.context).mSoundCaptureCity.play();
				}
			}
			//人口等于0 改变归属 初始等级
			this.setBelong(belong);
			this.setLevel(1);
			this.fightingSprite.stopAnimation();
			this.fightingSprite.setVisible(false);
		}
		this.flush();
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public class DoubleTapGestureListener extends SimpleOnGestureListener{

	  public DoubleTapGestureListener(){
	  }

	  @Override
	  public boolean onDoubleTap(MotionEvent e){
		  Log.d("++++++doubleClick++++","1111");
	    if(City.this.belong==1){
	    	Log.d("++++++doubleClick++++","attack");
	    	((GameActivity)City.this.context).attack(City.this);
	    }
	    return true;
	  }

	}

}