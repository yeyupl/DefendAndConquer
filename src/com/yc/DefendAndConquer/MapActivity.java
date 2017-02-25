package com.yc.DefendAndConquer;

import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.font.StrokeFont;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Toast;

import com.yc.DefendAndConquer.Data.Levels;
import com.yc.DefendAndConquer.Entity.MapLevel;
import com.yc.DefendAndConquer.Utils.SoundUtil;
import com.yc.DefendAndConquer.Utils.Utils;


public class MapActivity extends BaseActivity {
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTexture;
	private TextureRegion mBackground,mMap,mInfo;
	private Scene mScene;
	private TiledTextureRegion mFire,mLevel,mBtn;
	
	private Music mMusic;
	private Sound mSound;
	
	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private BitmapTextureAtlas mStrokeFontTexture;
	private StrokeFont mStrokeFont;
	private AnimatedSprite btnSprite;
	private MapLevel mapLevel;
	private ChangeableText mapNameText;
	private ChangeableText mapTopText;
	private BitmapTextureAtlas mNameStrokeFontTexture;
	private StrokeFont mNameStrokeFont;
	private TextureRegion mGoldBg;
	private ChangeableText goldText;
	private ChangeableText needGoldText;
	private BitmapTextureAtlas mGoldFontTexture;
	private Font mGoldFont;
	private MapLevel mLevelSprite_1;
	private MapLevel mLevelSprite_2;
	private MapLevel mLevelSprite_3;
	private MapLevel mLevelSprite_4;
	private MapLevel mLevelSprite_5;
	private MapLevel mLevelSprite_6;
	private SoundUtil sound;
	private Text getGoldText;


	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new FillResolutionPolicy(), this.mCamera).setNeedsSound(true).setNeedsMusic(true));
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.DEFAULT);
		this.mBackground= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "map/bg.jpg", 0, 0);
		this.mMap = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "map/1.png", 0, 480);
		this.mInfo = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "map/info.png", 400, 480);
		
		this.mFire = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "map/fire.png", 0, 880, 11, 1);
		this.mLevel = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "map/level.png", 500, 630, 1, 2);
		this.mBtn = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "map/btn.png", 500, 800, 1, 2);
		this.mGoldBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "map/gold_bg.png", 700, 630);
		
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "music/main_theme.ogg");
			this.mMusic.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
		
		try {
			this.mSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/menu_select.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}
		this.mFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 18, true, Color.YELLOW);
		
		this.mStrokeFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mStrokeFont = FontFactory.createStrokeFromAsset(this.mStrokeFontTexture, this, "font/droid.ttf", 14, true, Color.BLACK, 1, Color.RED,true);
		
		this.mNameStrokeFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mNameStrokeFont = FontFactory.createStrokeFromAsset(this.mNameStrokeFontTexture, this, "font/droid.ttf", 25, true, Color.RED, 1.5f, Color.WHITE,true);
		
		this.mGoldFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mGoldFont = new Font(this.mGoldFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 20, true, Color.RED);
		
		
		this.mEngine.getTextureManager().loadTextures(this.mBitmapTexture,this.mFontTexture,this.mStrokeFontTexture,this.mNameStrokeFontTexture,this.mGoldFontTexture);
		this.mEngine.getFontManager().loadFonts(this.mFont,this.mStrokeFont,this.mNameStrokeFont,this.mGoldFont);
	}

	@Override
	public Scene onLoadScene() {
		//this.mEngine.registerUpdateHandler(new FPSLogger());
		
		this.sound = new SoundUtil(this);
		
		this.mScene = new Scene();
		
		this.mScene.setBackground(new SpriteBackground(new Sprite(0,0,this.mBackground)));
		
		this.mScene.attachChild(new Sprite(640, 0, this.mGoldBg));
		this.goldText = new ChangeableText(720,15,this.mGoldFont, this.gold+"", 4);
		this.mScene.attachChild(this.goldText);
		
		this.getGoldText = new Text(600,80,this.mGoldFont,"点击免费获取金币"){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
				if(pSceneTouchEvent.isActionDown()) {
					MapActivity.this.getGoldText.setScale(1.2f);
				}else{
					MapActivity.this.getGoldText.setScale(1.0f);					
				}
				return true;
			}
		};
		this.getGoldText.setColor(200, 211, 37);
		
		this.mScene.registerTouchArea(this.getGoldText);
		
		final Sprite mMapSprite = new Sprite(150, 20, this.mMap);
		mMapSprite.setRotation(-8.0f);
		this.mScene.attachChild(mMapSprite);
		
		final Sprite mInfoSprite = new Sprite(350, 280, this.mInfo);
		mInfoSprite.setRotation(-5.0f);
		this.mScene.attachChild(mInfoSprite);
		
		final AnimatedSprite mFireSprite = new AnimatedSprite(690, 190, this.mFire);
		mFireSprite.animate(100,true);
		this.mScene.attachChild(mFireSprite);
		

		this.mLevelSprite_1 = new MapLevel(this,320, 55, 1,this.mLevel.clone(),this.mStrokeFont);
		this.mScene.registerTouchArea(this.mLevelSprite_1);
		this.mScene.attachChild(this.mLevelSprite_1);
		
		this.mLevelSprite_2 =  new MapLevel(this,400, 110, 2,this.mLevel.clone(),this.mStrokeFont);
		this.mScene.registerTouchArea(this.mLevelSprite_2);
		this.mScene.attachChild(this.mLevelSprite_2);
		
		this.mLevelSprite_3 =  new MapLevel(this,170, 85, 3,this.mLevel.clone(),this.mStrokeFont);
		this.mScene.registerTouchArea(this.mLevelSprite_3);
		this.mScene.attachChild(this.mLevelSprite_3);
		
		this.mLevelSprite_4 =  new MapLevel(this,280, 160, 4,this.mLevel.clone(),this.mStrokeFont);
		this.mScene.registerTouchArea(this.mLevelSprite_4);
		this.mScene.attachChild(this.mLevelSprite_4);
		
		this.mLevelSprite_5 =  new MapLevel(this,350, 200, 5,this.mLevel.clone(),this.mStrokeFont);
		this.mScene.registerTouchArea(this.mLevelSprite_5);
		this.mScene.attachChild(this.mLevelSprite_5);
		
		this.mLevelSprite_6 =  new MapLevel(this,450, 150, 6,this.mLevel.clone(),this.mStrokeFont);
		this.mScene.registerTouchArea(this.mLevelSprite_6);
		this.mScene.attachChild(this.mLevelSprite_6);
		
		this.mapNameText = new ChangeableText(100,10,this.mNameStrokeFont, " ", 10);
		mInfoSprite.attachChild(this.mapNameText);
		
		this.mapTopText = new ChangeableText(80,50,this.mFont, "最高记录：??:??:??", 20);
		mInfoSprite.attachChild(this.mapTopText);
		
		this.btnSprite = new AnimatedSprite(50,80,this.mBtn){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
				if(pSceneTouchEvent.isActionDown()) {
					if(MapActivity.this.sound.isSound()){
						MapActivity.this.mSound.play();
					}
					this.setScale(1.25f);
					try{ 
						Thread.sleep(300); 
					}catch(InterruptedException   e){ 
						e.printStackTrace(); 
					} 
					this.setScale(1.0f);
					if(MapActivity.this.mapLevel!=null){
						if(!MapActivity.this.mapLevel.isUnLocked()){
							//激活解锁
							
						}
						if(MapActivity.this.mapLevel.isActive()){
							Intent intent = new Intent(MapActivity.this,GameActivity.class);
							intent.putExtra("level", MapActivity.this.mapLevel.getLevel());
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							MapActivity.this.startActivity(intent);
						}else{
							MapActivity.this.runOnUiThread(new Runnable(){
								@Override
								public void run() {
									Toast.makeText(MapActivity.this, "请先通过前面的关卡！", Toast.LENGTH_LONG).show();
								}
								
							});	
						}
					}
				}	
				return true;
			}
		};
		this.mScene.registerTouchArea(this.btnSprite);
		mInfoSprite.attachChild(this.btnSprite);
		
		this.showInfo(this.mLevelSprite_1);
		
		return this.mScene;
	}
	
	public void showInfo(MapLevel mapLevel){
		this.mapLevel = mapLevel;
		this.mapNameText.setText(Levels.getName(this.mapLevel.getLevel()));
		int topTime = this.store.getInt("time_"+this.mapLevel.getLevel());
		this.mapTopText.setText("最高记录："+(topTime>0?Utils.formatTime(topTime):"??:??:??"));
		if(this.mapLevel.isUnLocked()){
			this.btnSprite.stopAnimation(0);
		}else{
			this.btnSprite.stopAnimation(0);
		}
	}
	
	@Override
	public void onLoadComplete() {
		super.onLoadComplete();
	}
	

	
	@Override
	public void onResumeGame() {
		if(this.sound.isMusic()){
			this.mMusic.play();
		}
		this.mLevelSprite_1.flush();
		this.mLevelSprite_2.flush();
		this.mLevelSprite_3.flush();
		this.mLevelSprite_4.flush();
		this.mLevelSprite_5.flush();
		this.mLevelSprite_6.flush();
		
	}

	@Override
	public void onPauseGame() {
		if(this.mMusic.isPlaying()){
			this.mMusic.pause();
		}
	}


}