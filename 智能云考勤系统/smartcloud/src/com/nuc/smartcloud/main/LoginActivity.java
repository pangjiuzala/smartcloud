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
				intent.putExtra(Intent.EXTRA_SUBJECT, "����");
				intent.putExtra(Intent.EXTRA_TEXT,
						"������ʹ�������ƿ���ϵͳ����ά��ʶ���ƴ洢�����ÿ�����˷��㣬�Ͽ�������Ұ�!");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				LoginActivity.this.startActivity(Intent.createChooser(intent,
						"����"));
			}

		}).start();
	}

	// ����ͼ��
	public void shareIcon() {

		time = "rival_" + DateUtils.getTimeNow();
		FileUtils.savePic(
				BitmapFactory.decodeResource(getResources(), R.drawable.icon),
				time);
		new Thread(new Runnable() {
			public void run() {
				Intent intent = new Intent("android.intent.action.SEND");
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "����");
				intent.putExtra(Intent.EXTRA_TEXT,
						"������ʹ�������ƿ���ϵͳ����ά��ʶ���ƴ洢�����ÿ�����˷��㣬�Ͽ�������Ұ�!");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(Constant.path + time + ".png")));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				LoginActivity.this.startActivity(Intent.createChooser(intent,
						"����"));
			}

		}).start();
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	private Bitmap shot() {
		View view = getWindow().getDecorView();
		Display display = this.getWindowManager().getDefaultDisplay();
		view.layout(0, 0, display.getWidth(), display.getHeight());
		view.setDrawingCacheEnabled(true);// ����ǰ���ڱ��滺����Ϣ������getDrawingCache()�����Ż᷵��һ��Bitmap

		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
		view.destroyDrawingCache();
		FileUtils.savePic(bmp, time);
		return bmp;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// -------------��menu����������С���Ӳ˵�-------------

		menu.add(0, FONT_14, 0, "����");
		menu.add(0, FONT_16, 0, "�˳�");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	// �˵��������Ļص�����
	public boolean onOptionsItemSelected(MenuItem mi) {
		// �жϵ��������ĸ��˵��������Ե�������Ӧ��
		switch (mi.getItemId()) {

		case FONT_14:
			Intent addIntent = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			String title = getResources().getString(R.string.app_name);
			// ���ؿ�ݷ�ʽ��ͼ��
			Parcelable icon = Intent.ShortcutIconResource.fromContext(
					LoginActivity.this, R.drawable.icon);
			// ���������ݷ�ʽ�����Intent,�ô�����������Ŀ�ݷ�ʽ���ٴ������ó���
			Intent myIntent = new Intent(LoginActivity.this,
					com.nuc.smartcloud.extra.SplashscreenActivity.class);
			// ���ÿ�ݷ�ʽ�ı���
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
			// ���ÿ�ݷ�ʽ��ͼ��
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			// ���ÿ�ݷ�ʽ��Ӧ��Intent
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
			// ���͹㲥��ӿ�ݷ�ʽ
			sendBroadcast(addIntent);
			shareIcon();
			break;

		case FONT_16:
			new AlertDialog.Builder(this)
					// .setTitle("Message Box")
					.setMessage("��ȷ��Ҫ�˳������ƿ���ϵͳ��")
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setPositiveButton("ȷ��",
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
		// ���¼����Ϸ��ذ�ť
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			new AlertDialog.Builder(this)
					// .setTitle("Message Box")
					.setMessage("��ȷ��Ҫ�˳������ƿ���ϵͳ��")
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setPositiveButton("ȷ��",
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
				Toast.makeText(getApplicationContext(), "��¼�ɹ�",
						Toast.LENGTH_SHORT).show();
				MyActivityManager.exit();

			} else if (msg.arg1 == 1) {
				progressDialog.dismiss();

				username_main.getText().clear();
				password_main.getText().clear();
				Toast.makeText(getApplicationContext(), "�û������������",
						Toast.LENGTH_SHORT).show();

			} else if (msg.arg1 == 2) {
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "��½ʧ��,�����������������",
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
			Toast.makeText(getApplicationContext(), "��ǰ���粻����,������������",
					Toast.LENGTH_SHORT).show();

		}
		login = (Button) findViewById(R.id.login_btn);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				progressDialog = ProgressDialog.show(LoginActivity.this,
						"���ڷ����ƶ���Ϣ", "�����Ժ�...", false, true);
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

				// �����Ӱ�ؼ���
			}
		});

	}
}
