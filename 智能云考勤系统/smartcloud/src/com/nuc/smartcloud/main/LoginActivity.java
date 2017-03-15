package com.nuc.smartcloud.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import com.nuc.smartcloud.client.android.CaptureActivity;
import com.nuc.smartcloud.extra.SplashscreenActivity;
import com.nuc.smartcloud.util.Assist;
import com.nuc.smartcloud.util.Constant;
import com.nuc.smartcloud.util.CustomUtil;
import com.nuc.smartcloud.util.DateUtils;
import com.nuc.smartcloud.util.FileUtils;
import com.nuc.smartcloud.util.MyActivityManager;
import com.nuc.smartcloud.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button login;
	private Button search;
	private Button banben;
	private EditText username_main;
	private EditText password_main;
	private CheckBox save_main;
	final int FONT_8 = 0x110;
	final int FONT_10 = 0x111;
	final int FONT_12 = 0x112;
	final int FONT_14 = 0x113;
	final int FONT_16 = 0x114;
	static final int NOTIFICATION_ID = 0x1123;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	String time = null;
	ProgressDialog progressDialog;
	private boolean flag = true;
	String jsonString = "null";;

	public void shareScreenshot() {
		time = "rival_" + DateUtils.getTimeNow();
		shot();
		new Thread(new Runnable() {
			public void run() {
				Intent intent = new Intent("android.intent.action.SEND");
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_TEXT,
						"我正在使用智能云考勤系统，二维码识别、云存储技术让考勤如此方便，赶快分享给大家吧!");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				LoginActivity.this.startActivity(Intent.createChooser(intent,
						"分享"));
			}

		}).start();
	}

	// 分享图标
	public void shareIcon() {

		time = "rival_" + DateUtils.getTimeNow();
		FileUtils.savePic(
				BitmapFactory.decodeResource(getResources(), R.drawable.icon),
				time);
		new Thread(new Runnable() {
			public void run() {
				Intent intent = new Intent("android.intent.action.SEND");
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_TEXT,
						"我正在使用智能云考勤系统，二维码识别、云存储技术让考勤如此方便，赶快分享给大家吧!");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				LoginActivity.this.startActivity(Intent.createChooser(intent,
						"分享"));
			}

		}).start();
	}

	/**
	 * 截屏方法
	 * 
	 * @return
	 */
	private Bitmap shot() {
		View view = getWindow().getDecorView();
		Display display = this.getWindowManager().getDefaultDisplay();
		view.layout(0, 0, display.getWidth(), display.getHeight());
		view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap

		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
		view.destroyDrawingCache();
		FileUtils.savePic(bmp, time);
		return bmp;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// -------------向menu中添加字体大小的子菜单-------------

		menu.add(0, FONT_14, 0, "分享");
		menu.add(0, FONT_16, 0, "退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	// 菜单项被单击后的回调方法
	public boolean onOptionsItemSelected(MenuItem mi) {
		// 判断单击的是哪个菜单项，并针对性的作出响应。
		switch (mi.getItemId()) {

		case FONT_14:
			Intent addIntent = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			String title = getResources().getString(R.string.app_name);
			// 加载快捷方式的图标
			Parcelable icon = Intent.ShortcutIconResource.fromContext(
					LoginActivity.this, R.drawable.icon);
			// 创建点击快捷方式后操作Intent,该处当点击创建的快捷方式后，再次启动该程序
			Intent myIntent = new Intent(LoginActivity.this,
					com.nuc.smartcloud.extra.SplashscreenActivity.class);
			// 设置快捷方式的标题
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
			// 设置快捷方式的图标
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			// 设置快捷方式对应的Intent
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
			// 发送广播添加快捷方式
			sendBroadcast(addIntent);
			shareIcon();
			break;

		case FONT_16:
			new AlertDialog.Builder(this)
					// .setTitle("Message Box")
					.setMessage("您确定要退出智能云考勤系统吗？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									MyActivityManager.exit();

								}
							}).show();

			break;

		}
		return true;
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	protected void onStop() {

		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			new AlertDialog.Builder(this)
					// .setTitle("Message Box")
					.setMessage("您确定要退出智能云考勤系统吗？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									MyActivityManager.exit();

								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public boolean userlogin() throws Exception {
		String username = username_main.getText().toString().trim();
		String password = password_main.getText().toString().trim();
		connection = CustomUtil.getConnection();
		Statement s = (Statement) connection.createStatement();

		ResultSet rs = s

		.executeQuery("select  *  from   tpl_login where username="
				+ username + " and password=" + password + ";");

		if (rs.next()) {
			return true;

		} else {
			return false;
		}
	}

	public boolean managerlogin() throws Exception {
		String username = username_main.getText().toString().trim();
		String password = password_main.getText().toString().trim();
		connection = CustomUtil.getConnection();
		Statement s = (Statement) connection.createStatement();

		ResultSet rs = s

		.executeQuery("select  *  from   tpl_super where username="
				+ username + " and password=" + password + ";");
		// connection.close();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	private static String Md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		}
	}

	private void login() {
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 0) {

				progressDialog.dismiss();

				Assist.transActivity(LoginActivity.this, WelcomeActivity.class);
				Toast.makeText(getApplicationContext(), "登录成功",
						Toast.LENGTH_SHORT).show();
				MyActivityManager.exit();

			} else if (msg.arg1 == 1) {
				progressDialog.dismiss();

				username_main.getText().clear();
				password_main.getText().clear();
				Toast.makeText(getApplicationContext(), "用户名或密码错误",
						Toast.LENGTH_SHORT).show();

			} else if (msg.arg1 == 2) {
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "登陆失败,请检查输入或网络设置",
						Toast.LENGTH_SHORT).show();
				username_main.getText().clear();
				password_main.getText().clear();
			}

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		MyActivityManager.addActivity(LoginActivity.this);

		username_main = (EditText) findViewById(R.id.user);
		password_main = (EditText) findViewById(R.id.password);
		CustomUtil.isNetworkAvailable(getApplicationContext());
		if (CustomUtil.isNetworkAvailable(getApplicationContext()) == false) {
			Toast.makeText(getApplicationContext(), "当前网络不可用,请检查网络设置",
					Toast.LENGTH_SHORT).show();

		}
		login = (Button) findViewById(R.id.login_btn);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				progressDialog = ProgressDialog.show(LoginActivity.this,
						"正在访问云端信息", "请您稍后...", false, true);
				new Thread(new Runnable() {

					public void run() {
						String username = username_main.getText().toString()
								.trim();
						String password = password_main.getText().toString()
								.trim();

						try {
							Looper.prepare();
							managerlogin();
							userlogin();

							if (managerlogin() || userlogin()) {

								Message msg = new Message();
								msg.arg1 = 0;
								handler.sendMessage(msg);

							}

							else {

								Message msg = new Message();
								msg.arg1 = 1;
								handler.sendMessage(msg);

							}
							connection.close();
						} catch (Exception e) {
							Message msg = new Message();
							msg.arg1 = 2;
							handler.sendMessage(msg);

						} finally {
							progressDialog.dismiss();

						}

					}

				}).start();

				// 输入后影藏键盘
			}
		});

	}
}
