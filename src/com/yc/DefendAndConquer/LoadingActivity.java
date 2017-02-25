package com.yc.DefendAndConquer;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;


public class LoadingActivity extends BaseGameActivity {
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTexture;
	private TextureRegion mBackgroundTextureRegion;
	private Scene mScene;
	private TiledTextureRegion mLoadingBaRTextureRegion;


	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new FillResolutionPolicy(), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.DEFAULT);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTexture, this, "loader/bg.jpg", 0, 0);
		
		this.mLoadingBaRTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this, "loader/loading_bar.png", 0, 480, 1, 5);
		this.mEngine.getTextureManager().loadTextures(this.mBitmapTexture);
	}

	@Override
	public Scene onLoadScene() {
		//this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		
		this.mScene.setBackground(new SpriteBackground(new Sprite(0,0,this.mBackgroundTextureRegion)));
		
		final AnimatedSprite loadingBar = new AnimatedSprite(200, 400, this.mLoadingBaRTextureRegion);
		loadingBar.animate(500,false,new IAnimationListener(){
			@Override
			public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
				Intent intent = new Intent(LoadingActivity.this,MenuActivity.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}});
		this.mScene.attachChild(loadingBar);
		
		return this.mScene;
	}

	@Override
	public void onLoadComplete() {
		
	}

}