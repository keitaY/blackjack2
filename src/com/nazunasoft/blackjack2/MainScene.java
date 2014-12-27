package com.nazunasoft.blackjack2;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import android.app.ActionBar.LayoutParams;
import android.graphics.Typeface;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class MainScene extends KeyListenScene implements IOnSceneTouchListener {
	private BaseGameActivity baseActivity;
	Sprite[] cards = new Sprite[54];
	Sprite[][][] girls = new Sprite[3][3][3];
	public MainScene(MultiSceneActivity baseActivity){
		super(baseActivity);
		init();
	}
	
	public void init(){
		prepareCards();
		prepareGirls();
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
		return true;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		return false;
	}
	
	
	public void prepareSoundAndMusic() {
		
	}
	
	public void prepareCards(){
		for(int i=0;i<=51;i++){
			cards[i] = getBaseActivity().getResourceUtil().getSprite("cards/"+i+".png");
		}
		cards[52] = getBaseActivity().getResourceUtil().getSprite("cards/ura3.png");
	}
	public void prepareGirls(){
		String name="";
		for(int i=0;i<3;i++){
			if(i==0){name = "sora";
			}else if(i==1){name = "yu";
			}else if(i==2){name = "rina";
			}
			for(int j=0;j<3;j++){
				for(int k=0;k<3;k++){
					girls[i][j][k] = getBaseActivity().getResourceUtil().getSprite("girls/"+name+j+"_"+k+".png");	
				}
			}
		}
	}
}
