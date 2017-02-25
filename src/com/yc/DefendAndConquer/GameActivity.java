package com.yc.DefendAndConquer;

import java.io.IOException;
import java.util.ArrayList;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;

import com.mobclick.android.MobclickAgent;
import com.yc.DefendAndConquer.Data.Levels;
import com.yc.DefendAndConquer.Entity.Army;
import com.yc.DefendAndConquer.Entity.City;
import com.yc.DefendAndConquer.Entity.GameDialog;
import com.yc.DefendAndConquer.Entity.WayPath;
import com.yc.DefendAndConquer.Utils.SoundUtil;
import com.yc.DefendAndConquer.Utils.Utils;


public class GameActivity extends BaseActivity {
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private Camera mCamera;
	private Scene mScene;
	private int level;
	private BitmapTextureAtlas mBitmapTexture;
	private TextureRegion mBackgroundTextureRegion;
	private TiledTextureRegion mCityTextureRegion,mCitySelectedTextureRegion,mCityUpgradeTextureRegion;
	private TiledTextureRegion mCityUpgradingTextureRegion,mUnitWalkTextureRegion,mUnitWalk2TextureRegion,mFightTextureRegion;
	
	private Music mMusic;
	public Sound mSoundClick,mSoundClickUpgrade,mSoundUpgraded,mSoundGoArmy,mSoundFight,mSoundCaptureCity,mSoundLoseCity;
	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private ArrayList<City> cityList = new ArrayList<City>();
	private TimerHandler timerHandler;
	protected int time;
	protected GameDialog mDialog;
	protected TextureRegion mDialogBg;
	private Font mArmyFont;
	private BitmapTextureAtlas mArmyFontTexture;
	private TextureRegion mTopBg;
	private BitmapTextureAtlas mGoldFontTexture;
	private Font mGoldFont;
	private ChangeableText goldText;
	private ChangeableText timeText;
	private SoundUtil sound;
	private TextureRegion mTopLeftBg;
	private TextureRegion mHelpBg;
	private Sprite mHelp;
	private TextureRegion mHelpText;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new FillResolutionPolicy(), this.mCamera).setNeedsSound(true).setNeedsMusic(true));
	}

	@Override
	public void onLoadResources() {
		this.level = this.getIntent().getExtras().getInt("level");
		MobclickAgent.onEvent(this, "level", "level_"+this.level); 

		
		this.mBitmapTexture = new BitmapTextureAtlas(2048, 2048, TextureOptions.DEFAULT);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "level/"+this.level+".jpg", 0, 0);
		this.mCityTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "city/0.png", 0, 480, 5, 3);
		this.mCitySelectedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "anim/select_city.png", 0, 864, 8, 1);
		this.mCityUpgradeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "anim/upgrade.png", 0, 992, 2, 1);
		this.mCityUpgradingTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "anim/upgrading.png", 0, 1024, 8, 1);
		this.mUnitWalkTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "anim/unit_walk.png", 0, 1104, 32, 4);
		this.mUnitWalk2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "anim/unit2_walk.png", 0, 1232, 32, 4);
		this.mFightTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "anim/fight.png", 800, 0, 8, 1);
		this.mDialogBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "gui/game_dialog.png", 1024, 1024);
		this.mTopBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "gui/top_bg.png", 1424, 1024);
		this.mTopLeftBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "gui/top_left_bg.png", 1680, 1024);
		this.mHelpBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "gui/help_bg.png", 0, 1360);
		this.mHelpText = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "gui/help_text.png", 1936, 1024);
		
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "music/forest_ambient.ogg");
			this.mMusic.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
		try {
			this.mSoundClick = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/click.ogg");
			this.mSoundClickUpgrade = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/click_upgrade.ogg");
			this.mSoundUpgraded = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/upgraded.ogg");
			this.mSoundGoArmy = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/go_army.ogg");
			this.mSoundFight = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/fight_army.ogg");
			this.mSoundCaptureCity = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/capture_city.ogg");
			this.mSoundLoseCity = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/lost_city.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}

		this.mFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 18, true, Color.WHITE);
		this.mArmyFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mArmyFont = new Font(this.mArmyFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 13, true, Color.WHITE);
		
		this.mGoldFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mGoldFont = new Font(this.mGoldFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20, true, Color.RED);
		
		
		this.mEngine.getTextureManager().loadTextures(this.mBitmapTexture,this.mFontTexture,this.mArmyFontTexture,this.mGoldFontTexture);
		this.mEngine.getFontManager().loadFonts(this.mFont,this.mArmyFont,this.mGoldFont);
	}

	@Override
	public Scene onLoadScene() {
		//this.mEngine.registerUpdateHandler(new FPSLogger());
		this.sound = new SoundUtil(this);
		
		this.mScene = new Scene();
		
		this.mScene.setBackground(new SpriteBackground(new Sprite(0,0,this.mBackgroundTextureRegion)));
		
		
		this.mScene.attachChild(new Sprite(0, 0, this.mTopLeftBg));
		
		Sprite helpTap = new Sprite(20, 12, this.mHelpText){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
				if(pSceneTouchEvent.isActionDown()) {
					if(GameActivity.this.sound.isSound()){
						GameActivity.this.mSoundClick.play();
					}	
					GameActivity.this.mHelp.setVisible(true);
					GameActivity.this.mScene.registerTouchArea(GameActivity.this.mHelp);
				}	
				return true;
			}
		};
		this.mScene.registerTouchArea(helpTap);
		this.mScene.attachChild(helpTap);
				
		this.mScene.attachChild(new Sprite(544, 0, this.mTopBg));
		
		this.mScene.attachChild(new Text(120, 14, this.mFont, Levels.getName(this.level), HorizontalAlign.LEFT));
		
		this.goldText = new ChangeableText(720,15,this.mGoldFont, this.gold+"", 4);
		this.mScene.attachChild(this.goldText);
		
		this.timeText = new ChangeableText(600,15,this.mFont,  Utils.formatTime(this.time), 8);
		this.mScene.attachChild(this.timeText);
		
		for (int i = 0; i < Levels.getCityNum(this.level); i++) {
			int[] city = Levels.getCity(this.level,i);
			final City mCity = new City(this, city[1], city[2], city[3],this.mCityTextureRegion.clone());
			mCity.setId(city[0]);
			if (mCity.getBelong() != -1) {
				mCity.addSelected(this.mCitySelectedTextureRegion.clone());
				mCity.addUpgrading(this.mCityUpgradingTextureRegion.clone());
				mCity.addText(this.mFont);
				mCity.addUpgrade(this.mCityUpgradeTextureRegion.clone());
				mCity.addFighting(this.mFightTextureRegion.clone());
				mCity.setLevel(city[4]);
				mCity.setMaxLevel(city[5]);
				mCity.setPopulationFull();
				mCity.flush();
				this.mScene.attachChild(mCity);
				this.mScene.registerTouchArea(mCity);
				this.mScene.registerTouchArea(mCity.getUpgradeSprite());
			}
			this.cityList.add(mCity);
		}
		
		this.mHelp = new Sprite(150,0, this.mHelpBg){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
				if(pSceneTouchEvent.isActionDown()) {
					if(GameActivity.this.sound.isSound()){
						GameActivity.this.mSoundClick.play();
					}	
					GameActivity.this.mHelp.setVisible(false);
					GameActivity.this.mScene.unregisterTouchArea(GameActivity.this.mHelp);
				}	
				return true;
			}
		};
		this.mHelp.setVisible(false);
		this.mScene.attachChild(this.mHelp);
		
		this.timerHandler = new TimerHandler(1.0f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				GameActivity.this.time++;
				GameActivity.this.timeText.setText( Utils.formatTime(GameActivity.this.time));
				//判断游戏是否结束
				GameActivity.this.isFinish();
			}
		});
		this.mScene.registerUpdateHandler(this.timerHandler);
		
		return this.mScene;
	}
	
	//攻击目标城市
	public void attack(City aimCity){
		for(City city:this.cityList){
			if(city.getBelong()==1 && city.isSelected()){
				//重置选中状态
				city.selected(false);
				//出兵
				if(!city.equals(aimCity) && city.getPopulation()>1){
					Path path = new WayPath(this.cityList,city,aimCity,this.level).getPath();
					if(path!=null){
						if(this.sound.isSound()){
							this.mSoundGoArmy.play();
						}
						//出兵数量
						int armyNum = (int) Math.floor(city.getPopulation()/2);
						city.setPopulation(city.getPopulation()-armyNum);
						//for(int i=0;i<armyNum;i++){
							Army army = new Army(this,city,armyNum,this.mUnitWalkTextureRegion,this.mArmyFont);
							army.depart(this.cityList,aimCity,path);
							this.mScene.attachChild(army);
						//}
					}
				}
			}
		}
	}
	
	//电脑方攻击目标城市
	public void attackAI(City selfCity){
		//优先攻击未占领的
		City aimCity = this.getAttackCity(selfCity,0);
		if(aimCity!=null){
			Path path = new WayPath(this.cityList,selfCity,aimCity,this.level).getPath();
			if(path!=null){
				//出兵数量
				int armyNum = (int) Math.floor(selfCity.getPopulation()/2);
				selfCity.setPopulation(selfCity.getPopulation()-armyNum);
				Army army = new Army(this,selfCity,armyNum,this.mUnitWalk2TextureRegion,this.mArmyFont);
				army.depart(this.cityList,aimCity,path);
				this.mScene.attachChild(army);
				return;
			}
		}
	
		//全部被占领后  攻击对方
		City aimCity2 = this.getAttackCity(selfCity,1);
		if(aimCity2!=null){
			Path path = new WayPath(this.cityList,selfCity,aimCity2,this.level).getPath();
			if(path!=null){
				//出兵数量
				int armyNum = (int) Math.floor(selfCity.getPopulation()/2);
				selfCity.setPopulation(selfCity.getPopulation()-armyNum);
				Army army = new Army(this,selfCity,armyNum,this.mUnitWalk2TextureRegion,this.mArmyFont);
				army.depart(this.cityList,aimCity2,path);
				this.mScene.attachChild(army);
				return;
			}
		}
		
	}
	
	//获得攻击目标
	public City getAttackCity(City selfCity,int belong){
		City AimCity = null;
		float distance = 99999;
		for(City city:this.cityList){
			if(city.getBelong() == belong ){
				Path path = new WayPath(this.cityList,selfCity,city,this.level).getPath();
				if(path!=null){
					//距离
					if(path.getLength()<distance){
						distance = path.getLength();
						AimCity = city;
					}
				}
			}
		}
		return AimCity;
	}
	
	public Scene getScene(){
		return this.mScene;
	}
	@Override
	public void onLoadComplete() {
		super.onLoadComplete();
		//电脑方AI 每秒判断一 次决策
		this.mScene.registerUpdateHandler(new TimerHandler(1.0f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				for(City city:GameActivity.this.cityList){
					//电脑方占领的才参与决策
					if(city.getBelong()==2){
						//能升级的进行升级
						if(city.isUpgrade()){
							city.upgrade();
						}else{
							//如果当前兵力到达一半 出兵
							if(city.getPopulation()>city.getMaxPopulation()/2){
								GameActivity.this.attackAI(city);
							}
						}
					}
				}
			}
		}));
		
	}
	
	//游戏是否结束
	public void isFinish(){
		//某一方未占领任何城市
		int self=0;
		int enemy=0;
		for(City city:this.cityList){
			if(city.getBelong()==1){
				self++;
			}else if(city.getBelong()==2){
				enemy++;
			}
		}
		if (self == 0 || enemy == 0) {
			final boolean isWin = self == 0?false:true;
			this.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					GameActivity.this.mDialog = new GameDialog(GameActivity.this,GameActivity.this.mEngine,GameActivity.this.mScene,GameActivity.this.mDialogBg);
					GameActivity.this.mScene.attachChild(GameActivity.this.mDialog);
					GameActivity.this.mDialog.show(isWin,GameActivity.this.time,GameActivity.this.level);
				}
			});
		}
	}
	
	@Override
	public void onResumeGame() {
		super.onResumeGame();
		if(!this.mMusic.isPlaying() && this.sound.isMusic()){
			this.mMusic.play();
		}
	
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();
		if(this.mMusic.isPlaying()){
			this.mMusic.pause();
		}
	}


}