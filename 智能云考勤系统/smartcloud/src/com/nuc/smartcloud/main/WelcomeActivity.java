package com.nuc.smartcloud.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.nuc.smartcloud.client.android.CaptureActivity;
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
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
	String time = null;
	final int SINGLE_DIALOG = 0x113;
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
				WelcomeActivity.this.startActivity(Intent.createChooser(intent,
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
				WelcomeActivity.this.startActivity(Intent.createChooser(intent,
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
									MyActivityManager.exit();
									Assist.transActivity(WelcomeActivity.this,
											WelcomeActivity.class);
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainactivity);

		MyActivityManager.addActivity(WelcomeActivity.this);
		CustomUtil.isNetworkAvailable(getApplicationContext());

		if (CustomUtil.isNetworkAvailable(getApplicationContext()) == false) {
			Toast.makeText(getApplicationContext(), "��ǰ���粻����,������������",
					Toast.LENGTH_SHORT).show();

		}
		Button button1 = (Button) findViewById(R.id.button1);// Ա��ע��

		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.lovewll.com/register/index.php/index/index.html");
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
			}
		});
		Button button2 = (Button) findViewById(R.id.button2);// Ա��ע��

		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyActivityManager.exit();
				Assist.transActivity(WelcomeActivity.this,
						ManagertoStaffActivity.class);
			}
		});
		Button button3 = (Button) findViewById(R.id.button3);// �û�ע��

		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyActivityManager.exit();
				Assist.transActivity(WelcomeActivity.this,
						ManagertoUserActivity.class);
			}
		});
		Button button4 = (Button) findViewById(R.id.button4);// �û�ע��

		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Assist.transActivity(WelcomeActivity.this,
						UserDeleteActivity.class);
			}
		});
		Button button5 = (Button) findViewById(R.id.button5);// ��¼��ѯ

		button5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyActivityManager.exit();
				Assist.transActivity(WelcomeActivity.this,
						RecordQueryActivity.class);
			}
		});
		Button button6 = (Button) findViewById(R.id.button6);// ��¼���

		button6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Assist.transActivity(WelcomeActivity.this,
						ManagertoClearActivity.class);
			}
		});
		Button button7 = (Button) findViewById(R.id.button7);// �汾��Ϣ

		button7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyActivityManager.exit();
				Assist.transActivity(WelcomeActivity.this,
						ManagertoIPActivity.class);
			}
		});
		Button button8= (Button) findViewById(R.id.button8);// �汾��Ϣ

		button8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyActivityManager.exit();
				Assist.transActivity(WelcomeActivity.this,
						ManagertoClearIpActivity.class);
			}
		});

		Button button9= (Button) findViewById(R.id.button9);// ��ʼ����

		button9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyActivityManager.exit();
				Assist.transActivity(WelcomeActivity.this, QRcodeActivity.class);
			}
		});
		Button button10 = (Button) findViewById(R.id.button10);// �汾��Ϣ

		button10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyActivityManager.exit();
				Assist.transActivity(WelcomeActivity.this, AboutActivity.class);
			}
		});
	
	}
}