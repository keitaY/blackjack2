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
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
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
	Sprite[] cards = new Sprite[54];
	Sprite[][][] girls = new Sprite[3][3][3];
	Sprite[] bg = new Sprite[3];
	private Sound drawsnd;
	int gamestate=0;//0...initial state 1...having cards(2 or more cards) 2...game over(ready to start)
	
	public MainScene(MultiSceneActivity baseActivity){
		super(baseActivity);
		init();
	}
	
	public void init(){
		prepareCards();
		prepareGirls();
		prepareBackgrounds();
		setOnSceneTouchListener(this);
		//---------------------
		setBackground(bg[0]);
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
		//setBackground(bg[1]);
		//drawcards(gamestate);
	    switch (pSceneTouchEvent.getAction()) {
	    case TouchEvent.ACTION_DOWN:
	        Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
	        drawsnd.play();
	        break;
	    case TouchEvent.ACTION_UP:
	        Log.d("TouchEvent", "getAction()" + "ACTION_UP");
	        break;
	    case TouchEvent.ACTION_MOVE:
	        Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
	        break;
	    case TouchEvent.ACTION_CANCEL:
	        Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
	        break;
	    }
		return true;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		return false;
	}
	
	public void drawcards(int state){
		int[] fieldcard = new int[5];
		if(state==0){
			fieldcard[0] = setcard(0);
			gamestate++;
		}else if(state==1){
			gamestate++;
		}else if(state==2){
			removecard(fieldcard[0]);
			gamestate=0;
		}
	}
	public int setcard(int fieldnum){
		int cardnum = (int)Math.random()*52;
		cards[cardnum].setPosition(20,20+50*fieldnum);
		attachChild(cards[cardnum]);
		return cardnum;
	}
	public void removecard(int cardnumber){
		if(cards[cardnumber].hasParent()){cards[cardnumber].detachSelf();}
	}
	//---------------------------------------------prepare----------------------------------------

	@Override
	public void prepareSoundAndMusic() {
		try {
			drawsnd = SoundFactory.createSoundFromAsset(getBaseActivity().getSoundManager(),getBaseActivity(),"mekuru.wav");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			for(int j=1;j<3;j++){
				for(int k=0;k<3;k++){
				girls[i][j][k] = getBaseActivity().getResourceUtil().getSprite("girls/"+name+j+"_"+k+".png");	
				}
			}
		}
	}
	public void prepareBackgrounds(){
		bg[0] = getBaseActivity().getResourceUtil().getSprite("bg/hanaya.jpg");
		bg[1] = getBaseActivity().getResourceUtil().getSprite("bg/kyositsu.jpg");
		bg[2] = getBaseActivity().getResourceUtil().getSprite("bg/casino.jpg");
	}
	public void setBackground(Sprite bg){
		bg.setPosition(0,0);
		attachChild(bg);
	}
	//-------------------------------------------------------------------------------------
}
