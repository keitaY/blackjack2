package com.nazunasoft.blackjack2;

import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class MainActivity extends MultiSceneActivity {
	// 画面のサイズ。
	private int CAMERA_WIDTH = 480;
	private int CAMERA_HEIGHT = 800;
	static SharedPreferences sp ;
	public EngineOptions onCreateEngineOptions() {
		// サイズを指定し描画範囲をインスタンス化
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		// ゲームのエンジンを初期化。
		// 第1引数 タイトルバーを表示しないモード
		// 第2引数 画面は縦向き（幅480、高さ800）
		// 第3引数 解像度の縦横比を保ったまま最大まで拡大する
		// 第4引数 描画範囲
		EngineOptions eo = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		eo.getAudioOptions().setNeedsSound(true);
		eo.getAudioOptions().setNeedsMusic(true);
		return eo;
	}
	
	
	@Override
	protected Scene onCreateScene() {
		// MainSceneをインスタンス化し、エンジンにセット
		SoundFactory.setAssetBasePath("mfx/");
		MusicFactory.setAssetBasePath("mfx/");
		MainScene mainScene = new MainScene(this);
		sp =  getSharedPreferences("myprefs",Context.MODE_PRIVATE);
		return mainScene;
	}

	@Override
	protected int getLayoutID() {
		// ActivityのレイアウトのIDを返す
		return R.layout.activity_main;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		// SceneがセットされるViewのIDを返す
		return R.id.renderview;
	}
	
	
	@Override
	public void appendScene(KeyListenScene scene) {
		
	}

	@Override
	public void backToInitial() {
		
	}
	
	@Override
	public void refreshRunningScene(KeyListenScene scene) {
	}
	
	@Override
	public void onGameDestroyed(){
		System.exit(0);
	}
	


}