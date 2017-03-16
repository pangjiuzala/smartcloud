package com.nuc.smartcloud.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.nuc.smartcloud.client.android.CaptureActivity;
import com.nuc.smartcloud.util.Assist;
import com.nuc.smartcloud.util.CustomUtil;
import com.nuc.smartcloud.util.MyActivityManager;
import com.nuc.smartcloud.R;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;

public class QRcodeActivity extends Activity {
	String jsonString = "null";;
	static final int NOTIFICATION_ID = 0x1123;
	private Button login;
	private Button search;
	private EditText username_main;
	private EditText password_main;
	private CheckBox save_main;
	final int FONT_8 = 0x110;
	final int FONT_10 = 0x111;
	final int FONT_12 = 0x112;
	final int FONT_14 = 0x113;
	final int FONT_16 = 0x114;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyActivityManager.exit();
			Assist.transActivity(QRcodeActivity.this, WelcomeActivity.class);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}


	@Override
	public void onPause() {
		super.onPause();
		MyActivityManager.exit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_qrcode);
		MyActivityManager.addActivity(QRcodeActivity.this);
		CustomUtil.isNetworkAvailable(getApplicationContext());

		if (CustomUtil.isNetworkAvailable(getApplicationContext()) == false) {
			Toast.makeText(getApplicationContext(), "当前网络不可用,请检查网络设置",
					Toast.LENGTH_SHORT).show();


		}
		login = (Button) findViewById(R.id.erwei);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyActivityManager.exit();
				Assist.transActivity(QRcodeActivity.this, CaptureActivity.class);

			}
		});

	}
}
