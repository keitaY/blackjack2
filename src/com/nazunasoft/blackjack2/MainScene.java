package com.nazunasoft.blackjack2;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
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
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

import android.graphics.Typeface;

import android.util.Log;
import android.view.KeyEvent;

public class MainScene extends KeyListenScene implements IOnSceneTouchListener {
	Sprite[] cards = new Sprite[54];
	Sprite[][][] girls = new Sprite[3][3][3];
	Sprite[] bg = new Sprite[3];
	private Sound drawsnd;
	int gamestate=0;//0...initial state 1...having cards(2 or more cards) 2...game over(ready to start)
	int havecards=0;
	int rensyou=0;
	int who=MainActivity.whoisselected;//0...sora 1...yu 2...rina
	int nugedo=0;//0...nugetenai 1...itimai nugeteru
	int dealerY=340;//Y of dealer cards row
	int guestY=570;//Y of guest cards row
	private Text guestscoretxt;
	private Text dealerscoretxt;
	private Text rensyoutxt;
	int[] field = new int[5];
	int[] dealerfield = new int[5];
	
	public MainScene(MultiSceneActivity baseActivity){
		super(baseActivity);
		init();
	}
	
	public void init(){
		prepareCards();
		prepareGirls();
		prepareBackgrounds();
		prepareText();
		preparebuttonsprite();
		setOnSceneTouchListener(this);
		//---------------------
		setBackground(bg[who]);
		setgirl(girls[who][nugedo][0]);
		sortChildren();
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
	    switch (pSceneTouchEvent.getAction()) {
	    case TouchEvent.ACTION_DOWN:
	        Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
	        drawsnd.play();
			drawcards(gamestate);
			if(gamestate==2){gameover();}
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
		if(state==0||state==2){
			removeAllcards();
			setgirl(girls[who][nugedo][0]);
			setdealercards();
			field[0] = setcard(0,0);
			field[1] = setcard(1,0);
			gamestate=1;
			havecards=2;
			guestscoretxt.setText(pointsolve(field)+"");
			dealerscoretxt.setText("?");
			sortChildren();
			if(isVast(pointsolve(field))==1){gamestate=2;}
		}else if(state==1){
			field[havecards] = setcard(havecards,0);
			havecards++;
			guestscoretxt.setText(pointsolve(field)+"");
			if(isVast(pointsolve(field))==1){
				gamestate=2;
			}else if(isVast(pointsolve(field))==0){
				gamestate=1;
			}
			if(havecards==5){gamestate=2;}
		}
	}
	
	public int pointsolve(int cardnum[]){
		int sum=0;
		int ace=0;
		int[] point = new int[5];
		for(int i=0;i<5;i++){
			if(cardnum[i]>=0){
				point[i] = (int)((cardnum[i])/4)+1;
				if(point[i]>=11){point[i]=10;}
				if(point[i]==1&&ace==0){ace=1;point[i]=11;}
			}else if(cardnum[i]==-1){
				point[i]=0;
			}
			sum = sum + point[i];
		}
		if(sum>=22&&ace==1){sum = sum-10;}
		return sum;
	}
	
	public int isVast(int n){ 
		//0...not vast  1...vast or blackjack
		if(n>=21){return 1;}
		return 0;
	}
	
	public int setcard(int fieldnum, int isdealer){
		int fieldX = 10+90*fieldnum;
		int cardnum = (int)(Math.random()*52);
		while(cards[cardnum].hasParent()){
			cardnum = (int)(Math.random()*52);//kabuttetara hikinaosi
			}
		if(!cards[cardnum].hasParent()){
		attachChild(cards[cardnum]);
		if(isdealer==0){
			cards[cardnum].setPosition(fieldX,guestY);
		}else{
			cards[cardnum].setPosition(fieldX,dealerY);
		}
		cards[cardnum].registerEntityModifier(new FadeInModifier(0.2f));
		cards[cardnum].registerEntityModifier(new MoveYModifier(
				0.2f+(0.1f*fieldnum), cards[cardnum].getY()-20-(20*fieldnum), cards[cardnum].getY()));
		}
		return cardnum;
	}
	
	public int setdealercards(){
		dealerfield[0] = setcard(0,1);
		dealerfield[1] = setcard(1,1);
		reversecard();
		if(pointsolve(dealerfield)==21){
			return 21;}
		if(pointsolve(dealerfield)>=17){
			return pointsolve(dealerfield);}
		if(pointsolve(dealerfield)<17&&pointsolve(dealerfield)>=15){
			if((int)(Math.random()*100)>60){
				return pointsolve(dealerfield);
			}
		}
		//---------------
		dealerfield[2] = setcard(2,1);
		if(pointsolve(dealerfield)>=17){
			return pointsolve(dealerfield);}
		if(pointsolve(dealerfield)<17&&pointsolve(dealerfield)>=15){
			if((int)(Math.random()*100)>40){
				return pointsolve(dealerfield);
			}
		}
		//-------------------
		dealerfield[3] = setcard(3,1);
		if(pointsolve(dealerfield)>=16){
			return pointsolve(dealerfield);}
		if(pointsolve(dealerfield)<17&&pointsolve(dealerfield)>=15){
			if((int)(Math.random()*100)>20){
				return pointsolve(dealerfield);
			}
		}
		//-------------------------
		dealerfield[4] = setcard(4,1);
		return pointsolve(dealerfield);
	}
	
	public void reversecard(){
		cards[dealerfield[1]].setVisible(false);
		if(!cards[52].hasParent()){
		attachChild(cards[52]);
		cards[52].setPosition(100,dealerY);
		cards[52].registerEntityModifier(new FadeInModifier(0.2f));
		cards[52].registerEntityModifier(new MoveYModifier(
				0.2f+(0.1f*1), cards[52].getY()-20-(20*1), cards[52].getY()));
		}
	}
	
	public void gameover(){
		if(cards[52].hasParent()){cards[52].detachSelf();}
		cards[dealerfield[1]].setVisible(true);
		cards[dealerfield[1]].registerEntityModifier(new FadeInModifier(0.12f));
		cards[dealerfield[1]].registerEntityModifier(new MoveXModifier(
				0.12f, cards[dealerfield[1]].getX()-20-(20*1), cards[dealerfield[1]].getX()));
		dealerscoretxt.setText(pointsolve(dealerfield)+"");
		
		int dealerscore = pointsolve(dealerfield);
		if(dealerscore>21){dealerscore=0;}
		int playerscore = pointsolve(field);
		if(playerscore>21){playerscore=0;}
		
		if(dealerscore>playerscore){//---player lose
			rensyou=0;
			nugedo=0;
			rensyoutxt.setText("");
			guestscoretxt.setText(pointsolve(field)+" you lose...");
			setgirl(girls[who][nugedo][1]);
			sortChildren();
		}else if(dealerscore<playerscore){//player win
			rensyou++;
			if(rensyou>3){nugedo=1;}
			guestscoretxt.setText(pointsolve(field)+" you win!!");
			rensyoutxt.setText(rensyou+"èüñ⁄ÅI");
			setgirl(girls[who][nugedo][2]);
			sortChildren();
		}else{//push
			guestscoretxt.setText(pointsolve(field)+" push");
		}
		
	}
	
	//------------------------set-and-remove-----------------------------------------------------
	
	public void setgirl(Sprite girl){
		removegirl();
		if(!girl.hasParent()){
			attachChild(girl);}
	}
	public void removegirl(){
		for(int i=0;i<3;i++){
			for(int j=0;j<2;j++){
				for(int k=0;k<3;k++){
					if(girls[i][j][k].hasParent()){girls[i][j][k].detachSelf();}			
				}
			}
		}
	}
	public void removeAllcards(){
		for(int i=0;i<=52;i++){
			if(cards[i].hasParent()){
			//	cards[i].registerEntityModifier(new FadeOutModifier(0.4f));
			//	cards[i].registerEntityModifier(new MoveXModifier(0.4f, cards[i].getX(), 500));
				cards[i].detachSelf();
			}
		}
		resetfield();
	}
	public void resetfield(){
		for(int i=0;i<5;i++){
			field[i] = -1;
			dealerfield[i] = -1;
		}
	}

	public void setBackground(Sprite bg){
		bg.setPosition(0,0);
		attachChild(bg);
	}
	//---------------------------------------------prepare-assets----------------------------------------

	@Override
	public void prepareSoundAndMusic() {
		try {
			drawsnd = SoundFactory.createSoundFromAsset(getBaseActivity().getSoundManager(),getBaseActivity(),"mekuru.wav");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void prepareText(){
		Texture texture = new BitmapTextureAtlas(getBaseActivity().getTextureManager(), 480, 800,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		Typeface tegaki = Typeface.createFromAsset(getBaseActivity().getAssets(), "font/851tegaki.ttf");
		Font font = new Font(getBaseActivity().getFontManager(), texture, tegaki, 22, true, Color.BLACK);
		getBaseActivity().getTextureManager().loadTexture(texture);
		getBaseActivity().getFontManager().loadFont(font);
		guestscoretxt = new Text(35, guestY-25, font, "touch to start!", 50,
				new TextOptions(HorizontalAlign.LEFT), getBaseActivity()
						.getVertexBufferObjectManager());
		guestscoretxt.setZIndex(2);
		dealerscoretxt = new Text(35, dealerY+175, font, "", 50,
				new TextOptions(HorizontalAlign.LEFT), getBaseActivity()
						.getVertexBufferObjectManager());
		dealerscoretxt.setZIndex(2);
		rensyoutxt = new Text(35, dealerY-25, font, "", 50,
				new TextOptions(HorizontalAlign.LEFT), getBaseActivity()
						.getVertexBufferObjectManager());
		rensyoutxt.setZIndex(2);

		attachChild(guestscoretxt);
		attachChild(dealerscoretxt);
		attachChild(rensyoutxt);
	}
	
	public void prepareCards(){
		for(int i=0;i<=51;i++){
			cards[i] = getBaseActivity().getResourceUtil().getSprite("cards/"+i+".png");
			cards[i].setZIndex(1);
		}
		cards[52] = getBaseActivity().getResourceUtil().getSprite("cards/ura3.png");
		cards[52].setZIndex(1);
	}
	public void prepareGirls(){
		String name="";
		for(int i=0;i<3;i++){
			if(i==0){name = "sora";
			}else if(i==1){name = "yu";
			}else if(i==2){name = "rina";
			}
			for(int j=0;j<2;j++){
				for(int k=0;k<3;k++){
				girls[i][j][k] = getBaseActivity().getResourceUtil().getSprite("girls/"+name+(j+1)+"_"+k+".png");	
				girls[i][j][k].setZIndex(0);
				if(i==2){girls[i][j][k].setPosition(0,-80);}
				}
			}
		}
	}
	
	
	public void preparebuttonsprite(){
		 final Sprite button = new Sprite(145, 745, (TextureRegion) getBaseActivity().getResourceUtil().getSprite("icons/hold.png").getTextureRegion(), this.getBaseActivity().getVertexBufferObjectManager()){
			   @Override
			   public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			    switch (pSceneTouchEvent.getAction()) {
			    case TouchEvent.ACTION_DOWN:
			        Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
			        if(gamestate==1){
				    gamestate=2;
				    gameover();
				    drawsnd.play();
			        }
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
			  };
			  button.setZIndex(0);
			  attachChild(button);
			  registerTouchArea(button);
	}
	
	public void prepareBackgrounds(){
		bg[0] = getBaseActivity().getResourceUtil().getSprite("bg/hanaya.jpg");
		bg[1] = getBaseActivity().getResourceUtil().getSprite("bg/kyositsu.jpg");
		bg[2] = getBaseActivity().getResourceUtil().getSprite("bg/casino.jpg");
		bg[0].setZIndex(-1);
		bg[1].setZIndex(-1);
		bg[2].setZIndex(-1);
	}
	//-------------------------------------------------------------------------------------
}
