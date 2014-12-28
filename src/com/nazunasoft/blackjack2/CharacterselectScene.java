package com.nazunasoft.blackjack2;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;

public class CharacterselectScene extends KeyListenScene implements IOnSceneTouchListener  {

	Sprite[] girls = new Sprite[3];
	Sprite[] bg = new Sprite[4];
	Sprite layer;
	int who =0;
	private Text nametxt;
	private Text titletxt;
	private Text scoretxt;
	private Sound selectsnd;
	private Sound oksnd;
	public CharacterselectScene(MultiSceneActivity baseActivity) {
		super(baseActivity);
		init();
	}
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
	    switch (pSceneTouchEvent.getAction()) {
	    case TouchEvent.ACTION_DOWN:
	        Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
	        oksnd.play();
			layer.registerEntityModifier(new FadeOutModifier(0.12f));
			layer.registerEntityModifier(new MoveYModifier(
					0.12f, layer.getY(), layer.getY()+40));
			nametxt.registerEntityModifier(new FadeOutModifier(0.12f));
			if(who==1){who=0;
			}else if(who==2){who=1;
			}else if(who==0){who=2;}
			girls[who].registerEntityModifier(new FadeOutModifier(0.12f));
			girls[who].registerEntityModifier(new MoveXModifier(
					0.12f, girls[who].getX(), girls[who].getX()-20));
			MainActivity.whoisselected = who;
	        break;
	    case TouchEvent.ACTION_UP:
	        Log.d("TouchEvent", "getAction()" + "ACTION_UP");
	        ResourceUtil.getInstance(getBaseActivity()).resetAllTexture();
	        KeyListenScene scene = new MainScene(getBaseActivity());
	        getBaseActivity().getEngine().setScene(scene);
	        getBaseActivity().appendScene(scene);
	        break;
	    case TouchEvent.ACTION_MOVE:
	        Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
	        break;
	    case TouchEvent.ACTION_CANCEL:
	        Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
	        break;
	    }
		return false;
	}

	@Override
	public void init() {
		prepareGirls();
		prepareBackgrounds();
		preparebuttonsprite();
		prepareText();
		showgirl(who);
		sortChildren();
		setOnSceneTouchListener(this);
	}

	@Override
	public void prepareSoundAndMusic() {
		try {
			selectsnd = SoundFactory.createSoundFromAsset(getBaseActivity().getSoundManager(),getBaseActivity(),"kinzoku2.wav");
			oksnd = SoundFactory.createSoundFromAsset(getBaseActivity().getSoundManager(),getBaseActivity(),"okay.wav");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	
	public void preparebuttonsprite(){
		 final Sprite button = new Sprite(400, 220, (TextureRegion) getBaseActivity().getResourceUtil().getSprite("icons/point.png").getTextureRegion(), this.getBaseActivity().getVertexBufferObjectManager()){
			   @Override
			   public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			    switch (pSceneTouchEvent.getAction()) {
			    case TouchEvent.ACTION_DOWN:
			        Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
			        selectsnd.play();
			        showgirl(who);
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
			  attachChild(button);
			  button.setZIndex(4);
			  registerTouchArea(button);
	}
	
	public void showgirl(int wh){
		removegirl();
		setBackgrounds(wh);
		attachChild(girls[wh]);
		girls[wh].registerEntityModifier(new FadeInModifier(0.12f));
		girls[wh].registerEntityModifier(new MoveXModifier(
				0.12f, girls[wh].getX()-20, girls[wh].getX()));
		if(wh==0){
			nametxt.setText("Sora Atarasi");
		}else if(wh==1){
			nametxt.setText("Yu Inami");
		}else if(wh==2){
			nametxt.setText("Rina Umino");
		}
		layer.registerEntityModifier(new FadeInModifier(0.12f));
		layer.registerEntityModifier(new MoveYModifier(
				0.12f, layer.getY()+40, layer.getY()));
        sortChildren();
        who++;
        if(who==3){who=0;}
	}
	
	public void removegirl(){
		for(int i=0;i<3;i++){
			if(girls[i].hasParent()){girls[i].detachSelf();}
		}
	}
	
	public void prepareGirls(){
		String name="";
		for(int i=0;i<3;i++){
			if(i==0){name = "sora";
			}else if(i==1){name = "yu";
			}else if(i==2){name = "rina";
			}
		girls[i] = getBaseActivity().getResourceUtil().getSprite("girls/"+name+(1)+"_"+0+".png");
		if(i==0||i==1){girls[i].setPosition(0,50);
		}else{girls[i].setPosition(0,0);}
		girls[i].setZIndex(1);
		}
	}
	public void prepareText(){
		Texture texture = new BitmapTextureAtlas(getBaseActivity().getTextureManager(), 480, 800,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		Typeface tegaki = Typeface.createFromAsset(getBaseActivity().getAssets(), "font/851tegaki.ttf");
		Font font = new Font(getBaseActivity().getFontManager(), texture, tegaki, 38, true, Color.BLACK);
		getBaseActivity().getTextureManager().loadTexture(texture);
		getBaseActivity().getFontManager().loadFont(font);
		nametxt = new Text(220, 680, font, "Sora Atarasi", 50,
				new TextOptions(HorizontalAlign.LEFT), getBaseActivity()
						.getVertexBufferObjectManager());
		nametxt.setZIndex(3);
		attachChild(nametxt);
		titletxt = new Text(90, 30, font, "Character Select", 50,
				new TextOptions(HorizontalAlign.LEFT), getBaseActivity()
						.getVertexBufferObjectManager());
		titletxt.setZIndex(3);
		attachChild(titletxt);
		
		scoretxt = new Text(10, 700, font, "", 50,
				new TextOptions(HorizontalAlign.LEFT), getBaseActivity()
						.getVertexBufferObjectManager());
		scoretxt.setZIndex(3);
		attachChild(scoretxt);
	}
	public void setBackgrounds(int w){
		removeBackgrounds();
		attachChild(bg[w]);
		bg[w].registerEntityModifier(new FadeInModifier(0.22f));
	}
	public void removeBackgrounds(){
		for(int i=0;i<3;i++){
			if(bg[i].hasParent()){bg[i].detachSelf();}
		}
	}
	
	public void prepareBackgrounds(){
		for(int i=0;i<3;i++){
		bg[i] = getBaseActivity().getResourceUtil().getSprite("bg/bgr"+i+".jpg");
		bg[i].setZIndex(-1);
		bg[i].setPosition(0,0);
		}
		bg[3] = getBaseActivity().getResourceUtil().getSprite("bg/white.jpg");
		bg[3].setZIndex(-2);
		bg[3].setPosition(0,0);
		attachChild(bg[3]);
		layer = getBaseActivity().getResourceUtil().getSprite("bg/layer.png");
		layer.setZIndex(2);
		layer.setPosition(0,0);
		attachChild(layer);
	}
	

}
