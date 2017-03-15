package com.nuc.smartcloud.extra;





import com.nuc.smartcloud.R;
import com.nuc.smartcloud.main.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;


public class  SplashscreenActivity extends Activity {
	
	private boolean isInit = false;
	SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.view_spalshscreen);
		isInit  = true;		// 初始化完毕
		
		new Monitor().start();		// 开启监听线程
		
	}
	
	
	
	private Handler mHandler = new Handler() {
		
		public void handleMessage(Message msg) {
			startActivity(new Intent(SplashscreenActivity.this, LoginActivity.class));
			finish();
		}
	};
		
	private class Monitor extends Thread {
		
		@Override
		public void run() {
			boolean isRun = true;
			while(isRun) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (isInit) {		// 如果初始化完毕，开始进入游戏
					isRun = false;
					mHandler.sendMessage(new Message());
				}
			}
		}
	}
}
