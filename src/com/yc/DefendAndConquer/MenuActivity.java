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
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;

import android.content.Intent;

import com.yc.DefendAndConquer.Utils.SoundUtil;


public class MenuActivity extends BaseActivity {
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTexture;
	private TextureRegion mBackgroundTextureRegion;
	private Scene mScene;
	private TiledTextureRegion mStartTextureRegion,mExitTextureRegion,mOptionsTextureRegion;
	
	private Music mMusic;
	private Sound mSound;
	private TextureRegion mOptionBg;
	private TiledTextureRegion mOptionsSelect;
	private TiledTextureRegion mOptionsBtn;
	private SoundUtil sound;
	private Sprite mOption;
	private AnimatedSprite mMusicSlect;
	private AnimatedSprite mBtn;
	private AnimatedSprite mSoundSlect;


	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new FillResolutionPolicy(), this.mCamera).setNeedsSound(true).setNeedsMusic(true));
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.DEFAULT);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "menu/bg.jpg", 0, 0);
		
		this.mStartTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "menu/start.png", 0, 480, 1, 2);
		this.mExitTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "menu/exit.png", 256, 480, 1, 2);
		this.mOptionsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "menu/options.png", 512, 480, 1, 2);
		this.mOptionBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "gui/option_bg.png", 0, 680);
		this.mOptionsSelect = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "gui/select.png",400, 680, 2, 1);
		this.mOptionsBtn = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "gui/game_btn.png",800, 680, 1, 2);

		
		try {
			this.mSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sound/menu_select.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}
		
		this.mEngine.getTextureManager().loadTextures(this.mBitmapTexture);
	}

	@Override
	public Scene onLoadScene() {
		//this.mEngine.registerUpdateHandler(new FPSLogger());
		
		
		this.sound = new SoundUtil(this);
		
		this.mScene = new Scene();
		
		this.mScene.setBackground(new SpriteBackground(new Sprite(0,0,this.mBackgroundTextureRegion)));
		
		final AnimatedSprite mStart = new AnimatedSprite(95, 184, this.mStartTextureRegion);
		this.mScene.attachChild(mStart);
		
		final AnimatedSprite mExit = new AnimatedSprite(75, 282, this.mExitTextureRegion);
		this.mScene.attachChild(mExit);
		
		final AnimatedSprite mOptions = new AnimatedSprite(587, 60, this.mOptionsTextureRegion);
		this.mScene.attachChild(mOptions);
		
		this.mScene.registerTouchArea(mStart);
		this.mScene.registerTouchArea(mExit);
		this.mScene.registerTouchArea(mOptions);
		this.mScene.setOnAreaTouchListener(new IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pTouchArea.equals(mStart)){
					if(pSceneTouchEvent.isActionDown()) {
						if(MenuActivity.this.sound.isSound()){
							MenuActivity.this.mSound.play();
						}
						mStart.stopAnimation(1);
					}
					if(pSceneTouchEvent.isActionUp()){
						mStart.stopAnimation(0);
						if(MenuActivity.this.mMusic.isPlaying()){
							MenuActivity.this.mMusic.stop();
						}
						Intent intent = new Intent(MenuActivity.this,MapActivity.class);
			    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				}else if(pTouchArea.equals(mExit)){
					if(pSceneTouchEvent.isActionDown()) {
						if(MenuActivity.this.sound.isSound()){
							MenuActivity.this.mSound.play();
						}
						mExit.stopAnimation(1);
					}
					if(pSceneTouchEvent.isActionUp()){
						mExit.stopAnimation(0);
						finish();
					}
				}else if(pTouchArea.equals(mOptions)){
					if(pSceneTouchEvent.isActionDown()) {
						if(MenuActivity.this.sound.isSound()){
							MenuActivity.this.mSound.play();
						}
						mOptions.stopAnimation(1);
					}
					if(pSceneTouchEvent.isActionUp()){
						mOptions.stopAnimation(0);
						MenuActivity.this.onOption();
					}
				}
				return true;
			}
		});		
		
		this.mOption = new Sprite(217,0, this.mOptionBg);
		this.mOption.setVisible(false);
		this.mBtn = new AnimatedSprite(90, 185, this.mOptionsBtn){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if(MenuActivity.this.sound.isSound()){
						MenuActivity.this.mSound.play();
					}
					MenuActivity.this.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							MenuActivity.this.mOption.setVisible(false);
							MenuActivity.this.mScene.unregisterTouchArea(MenuActivity.this.mBtn);
							MenuActivity.this.mScene.unregisterTouchArea(MenuActivity.this.mMusicSlect);
							MenuActivity.this.mScene.unregisterTouchArea(MenuActivity.this.mSoundSlect);
						}
					});
				}
				return true;
			}
		};
		this.mBtn.setScale(0.8f);
		this.mOption.attachChild(this.mBtn);
		this.mMusicSlect = new AnimatedSprite(230, 90, this.mOptionsSelect.clone()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if(MenuActivity.this.sound.isSound()){
						MenuActivity.this.mSound.play();
					}
					if(MenuActivity.this.sound.isMusic()){
						MenuActivity.this.sound.setMusicOn(false);
						this.stopAnimation(1);
						if(MenuActivity.this.mMusic.isPlaying()){
							MenuActivity.this.mMusic.pause();
						}
					}else{
						MenuActivity.this.sound.setMusicOn(true);
						this.stopAnimation(0);
						if(!MenuActivity.this.mMusic.isPlaying()){
							MenuActivity.this.mMusic.play();
						}
					}
				}
				return true;
			}
		};
		if(!this.sound.isMusic()){
			this.mMusicSlect.stopAnimation(1);
		}
		this.mOption.attachChild(this.mMusicSlect);
		this.mSoundSlect = new AnimatedSprite(230, 140, this.mOptionsSelect.clone()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if(MenuActivity.this.sound.isSound()){
						MenuActivity.this.mSound.play();
					}
					if(MenuActivity.this.sound.isSound()){
						MenuActivity.this.sound.setSoundOn(false);
						this.stopAnimation(1);
					}else{
						MenuActivity.this.sound.setSoundOn(true);
						this.stopAnimation(0);
					}
				}
				return true;
			}
		};
		if(!this.sound.isSound()){
			this.mSoundSlect.stopAnimation(1);
		}
		this.mOption.attachChild(this.mSoundSlect);
		this.mScene.attachChild(this.mOption);
		

		return this.mScene;
	}
	
	private void onOption(){
		this.mOption.setVisible(true);
		this.mScene.registerTouchArea(this.mBtn);
		this.mScene.registerTouchArea(this.mMusicSlect);
		this.mScene.registerTouchArea(this.mSoundSlect);
	}
	
	@Override
	public void onLoadComplete() {
		super.onLoadComplete();
	}
	

	
	@Override
	public void onResumeGame() {
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "music/main_theme.ogg");
			this.mMusic.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
		if(this.sound.isMusic()){
			this.mMusic.play();
		}
	}

	@Override
	public void onPauseGame() {
		if(this.mMusic.isPlaying()){
			this.mMusic.pause();
		}
	}

}