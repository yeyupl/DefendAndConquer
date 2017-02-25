package com.yc.DefendAndConquer.Entity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.yc.DefendAndConquer.GameActivity;
import com.yc.DefendAndConquer.MapActivity;
import com.yc.DefendAndConquer.Utils.Store;
import com.yc.DefendAndConquer.Utils.Utils;

public class GameDialog extends Sprite {
	
	private Scene mScene;
	private Context context;
	private BitmapTextureAtlas mBitmapTexture;
	private Engine mEngine;
	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private StrokeFont mStrokeFont;
	private BitmapTextureAtlas mStrokeFontTexture;
	private Store store;
	private TiledTextureRegion mBtn;



	public GameDialog(float pX, float pY, TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
	}
	
	public GameDialog(Context context,Engine mEngine,Scene mScene,TextureRegion pTextureRegion) {
		super(200, 40, pTextureRegion);
		this.context = context;
		this.mEngine = mEngine;
		this.mScene = mScene;
		this.loadResources();	
		this.store = new Store(this.context);
	}
	
	private void loadResources() {
		this.mBitmapTexture  = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBtn = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTexture, this.context, "gui/game_btn.png", 0, 0,1,2);
		
		this.mEngine.getTextureManager().loadTexture(this.mBitmapTexture);
		
		this.mFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this.context, "font/droid.ttf", 28, true, Color.WHITE);
		
		this.mStrokeFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mStrokeFont = FontFactory.createStrokeFromAsset(this.mStrokeFontTexture, this.context, "font/droid.ttf", 30, true, Color.BLACK, 2, Color.YELLOW,true);
		
		this.mEngine.getTextureManager().loadTextures(this.mFontTexture, this.mStrokeFontTexture);
		this.mEngine.getFontManager().loadFonts(this.mFont,this.mStrokeFont);
	}
	
	public void show(final boolean isWin,int time,int level){
		
		this.mScene.clearTouchAreas();
		this.mScene.clearUpdateHandlers();
		int topTime = this.store.getInt("time_"+level);
		if(isWin && (topTime<=0 || time <= topTime)){
			this.store.set("time_"+level, time);
			((GameActivity)GameDialog.this.context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(GameDialog.this.context, "你创造了新的记录！", Toast.LENGTH_LONG).show();						
				}
				
			});				
		}
		
		this.attachChild(new Text(140, 145, this.mFont, Utils.formatTime(time)));
		
		this.attachChild(new Text(150, 200, this.mStrokeFont, isWin?"胜利了！":"失败了！"));
		
		AnimatedSprite btnSprite = new AnimatedSprite(100,250,this.mBtn.clone()){
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionDown()){
						this.setScale(1.25f);
						try{ 
							Thread.sleep(300); 
						}catch(InterruptedException   e){ 
							e.printStackTrace(); 
						} 
						this.setScale(1.0f);
						((GameActivity)GameDialog.this.context).runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								Intent intent = new Intent(GameDialog.this.context,MapActivity.class);
					    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					    		GameDialog.this.context.startActivity(intent);
					    		GameDialog.this.detach();
					    		((Activity)GameDialog.this.context).finish();
							}
						});
					}
					return false;
				}
		};
		this.attachChild(btnSprite);
		this.mScene.registerTouchArea(btnSprite);
	}
	
	public void detach(){
		this.mScene.detachChild(this);
	}

	
	

}