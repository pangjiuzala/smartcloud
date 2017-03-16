package com.nuc.smartcloud.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.nuc.smartcloud.R;
import com.nuc.smartcloud.util.Assist;
import com.nuc.smartcloud.util.MyActivityManager;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;

public class AboutActivity extends Activity {
	String jsonString = "null";;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			MyActivityManager.exit();

			Assist.transActivity(this, WelcomeActivity.class);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_about);
		MyActivityManager.addActivity(AboutActivity.this);

	}

}