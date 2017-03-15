package com.nuc.smartcloud.client.android;

import com.nuc.smartcloud.BarcodeFormat;
import com.nuc.smartcloud.R;
import com.nuc.smartcloud.Result;
import com.nuc.smartcloud.ResultMetadataType;
import com.nuc.smartcloud.ResultPoint;
import com.nuc.smartcloud.client.android.camera.CameraManager;

import com.nuc.smartcloud.client.android.result.ResultButtonListener;
import com.nuc.smartcloud.client.android.result.ResultHandler;
import com.nuc.smartcloud.client.android.result.ResultHandlerFactory;

import com.nuc.smartcloud.main.QRcodeActivity;
import com.nuc.smartcloud.main.WelcomeActivity;
import com.nuc.smartcloud.main.WelcomeActivity;
import com.nuc.smartcloud.util.Assist;
import com.nuc.smartcloud.util.Constant;
import com.nuc.smartcloud.util.CustomUtil;
import com.nuc.smartcloud.util.MyActivityManager;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.UpdatableResultSet;

import android.R.bool;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.interfaces.RSAKey;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public final class CaptureActivity extends Activity implements
		SurfaceHolder.Callback {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	// 建立连接conn()

	ProgressDialog progressDialog;
	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final int SHARE_ID = Menu.FIRST;
	private static final int HISTORY_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	private static final int HELP_ID = Menu.FIRST + 3;
	private static final int ABOUT_ID = Menu.FIRST + 4;

	private static final long INTENT_RESULT_DURATION = 1500L;
	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;
	private static final float BEEP_VOLUME = 0.10f;
	private static final long VIBRATE_DURATION = 200L;

	private static final String PACKAGE_NAME = "com.nuc.zxing.client.android";
	private static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";
	private static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";
	private static final String ZXING_URL = "http://zxing.appspot.com/scan";
	private static final String RETURN_CODE_PLACEHOLDER = "{CODE}";
	private static final String RETURN_URL_PARAM = "ret";

	private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES;
	static {
		DISPLAYABLE_METADATA_TYPES = new HashSet<ResultMetadataType>(5);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.ISSUE_NUMBER);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.SUGGESTED_PRICE);
		DISPLAYABLE_METADATA_TYPES
				.add(ResultMetadataType.ERROR_CORRECTION_LEVEL);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.POSSIBLE_COUNTRY);
	}

	private enum Source {
		NATIVE_APP_INTENT, PRODUCT_SEARCH_LINK, ZXING_LINK, NONE
	}

	private CaptureActivityHandler handler;
	static final int NOTIFICATION_ID = 0x1123;
	private ViewfinderView viewfinderView;
	private TextView statusView;
	private View resultView;
	private MediaPlayer mediaPlayer;
	private Result lastResult;
	private boolean hasSurface;
	private boolean playBeep;
	private boolean vibrate;
	private boolean copyToClipboard;
	private Source source;
	private String sourceUrl;
	private String returnUrlTemplate;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private String versionName;

	private InactivityTimer inactivityTimer;

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	public void panduanzhengchang(Result result) throws Exception {
		{
			// URL url = new URL("http://www.bjtime.cn");
			// // 生成连接对象
			// URLConnection uc = url.openConnection();
			// // 发出连接
			// uc.connect();
			// long time = uc.getDat;
			Date date = new Date();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str_date = fmt.format(date);
			long time = date.getTime() / 1000;

			Statement s = (Statement) connection.createStatement();
			ResultSet rs = s

					.executeQuery("select  *  from    tpl_members  where  id="
							+ result);

			if (rs.next()) {

				if (rs.getInt("member_exam_record") == 1) {
					MyActivityManager.exit();
					Assist.transActivity(CaptureActivity.this,
							QRcodeActivity.class);
					AssetManager am = getAssets();
					AssetFileDescriptor afd = null;
					try {
						afd = am.openFd("click6.mp3");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MediaPlayer mplayer = new MediaPlayer();
					try {
						mplayer.setDataSource(afd.getFileDescriptor(),
								afd.getStartOffset(), afd.getLength());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						mplayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;
					mplayer.start();
					Toast toast = Toast.makeText(CaptureActivity.this, " \n "
							+ "重复签到!" + " \n "

							+ "姓名:" + rs.getObject("member_name").toString()
							+ " \n " + "考勤号:" + rs.getObject("id").toString()
							+ "\n " + "工号:"
							+ rs.getObject("member_id").toString() + " \n ",

					Toast.LENGTH_LONG);
					toast.show();
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(20000);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(20000);

					connection.close();

				}

				else if (rs.getInt("member_exam_record") == 0) {
					MyActivityManager.exit();
					Assist.transActivity(CaptureActivity.this,
							QRcodeActivity.class);

					AssetManager am = getAssets();
					AssetFileDescriptor afd = null;
					try {
						afd = am.openFd("zhengchang.mp3");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MediaPlayer mplayer = new MediaPlayer();
					try {
						mplayer.setDataSource(afd.getFileDescriptor(),
								afd.getStartOffset(), afd.getLength());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// // TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						mplayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;
					mplayer.start();
					CustomUtil.updateone(result);
					CustomUtil.updatezhengchang(result);

					Intent intent = new Intent(CaptureActivity.this,
							WelcomeActivity.class);
					PendingIntent pi = PendingIntent.getActivity(
							CaptureActivity.this, 0, intent, 0);
					// 创建一个Notification
					Notification notify = new Notification();
					// 为Notification设置图标，该图标显示在状态栏
					notify.icon = R.drawable.yun;
					// 为Notification设置文本内容，该文本会显示在状态栏
					notify.tickerText = "签到成功!";
					// 为Notification设置发送签到时间:

					// 为Notification设置声音
					notify.defaults = Notification.DEFAULT_SOUND;
					// 为Notification设置默认声音、默认振动、默认闪光灯
					notify.defaults = Notification.DEFAULT_ALL;
					// 设置事件信息
					notify.setLatestEventInfo(CaptureActivity.this, "姓名:"
							+ rs.getObject("member_name").toString() + ","
							+ "考勤号:" + rs.getObject("id").toString(),

					"签到状态:正常,时间:" + str_date, pi);
					// 获取系统的NotificationManager服务
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					// 发送通知
					notificationManager.notify(NOTIFICATION_ID, notify);
					connection.close();
				}
			}

		}
	}

	public void panduanchidao(Result result) throws Exception {
		{
			Date date = new Date();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str_date = fmt.format(date);
			long time = date.getTime() / 1000;

			Statement s = (Statement) connection.createStatement();
			ResultSet rs = s

					.executeQuery("select  *  from    tpl_members  where  id="
							+ result);

			if (rs.next()) {

				if (rs.getInt("member_exam_record") == 1) {
					MyActivityManager.exit();
					Assist.transActivity(CaptureActivity.this,
							QRcodeActivity.class);
					AssetManager am = getAssets();
					AssetFileDescriptor afd = null;
					try {
						afd = am.openFd("click6.mp3");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MediaPlayer mplayer = new MediaPlayer();
					try {
						mplayer.setDataSource(afd.getFileDescriptor(),
								afd.getStartOffset(), afd.getLength());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						mplayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;
					mplayer.start();

					Toast toast = Toast.makeText(CaptureActivity.this, " \n "
							+ "重复签到!" + " \n "

							+ "姓名:" + rs.getObject("member_name").toString()
							+ " \n " + "考勤号:" + rs.getObject("id").toString()
							+ "\n " + "工号:"
							+ rs.getObject("member_id").toString() + " \n ",

					Toast.LENGTH_LONG);
					toast.show();
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(20000);

					connection.close();

				}

				else if (rs.getInt("member_exam_record") == 0) {
					MyActivityManager.exit();
					Assist.transActivity(CaptureActivity.this,
							QRcodeActivity.class);
					AssetManager am = getAssets();
					AssetFileDescriptor afd = null;
					try {
						afd = am.openFd("chidao.mp3");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MediaPlayer mplayer = new MediaPlayer();
					try {
						mplayer.setDataSource(afd.getFileDescriptor(),
								afd.getStartOffset(), afd.getLength());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// // TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						mplayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;
					mplayer.start();
					CustomUtil.updateone(result);
					CustomUtil.updatechidao(result);
					Date date2 = new Date(time);
					SimpleDateFormat fmt2 = new SimpleDateFormat("hh:mm:ss");
					String str_date2 = fmt.format(date2);
					Intent intent = new Intent(CaptureActivity.this,
							WelcomeActivity.class);
					PendingIntent pi = PendingIntent.getActivity(
							CaptureActivity.this, 0, intent, 0);
					// 创建一个Notification
					Notification notify = new Notification();
					// 为Notification设置图标，该图标显示在状态栏
					notify.icon = R.drawable.yun;
					// 为Notification设置文本内容，该文本会显示在状态栏
					notify.tickerText = " 签到成功!";
					// 为Notification设置发送签到时间:

					// 为Notification设置声音
					notify.defaults = Notification.DEFAULT_SOUND;
					// 为Notification设置默认声音、默认振动、默认闪光灯
					notify.defaults = Notification.DEFAULT_ALL;
					// 设置事件信息
					notify.setLatestEventInfo(CaptureActivity.this, "姓名:"
							+ rs.getObject("member_name").toString() + ","
							+ "考勤号:" + rs.getObject("id").toString(),

					"签到状态:迟到,时间:" + str_date, pi);
					// 获取系统的NotificationManager服务
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					// 发送通知
					notificationManager.notify(NOTIFICATION_ID, notify);
				}
				connection.close();
			}

		}
	}

	public void panduankuangke(Result result) throws Exception {
		{
			// URL url = new URL("http://www.bjtime.cn");
			// // 生成连接对象
			// URLConnection uc = url.openConnection();
			// // 发出连接
			// uc.connect();
			// long time = uc.getDate();

			Date date = new Date();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str_date = fmt.format(date);
			long time = date.getTime() / 1000;
			Statement s = (Statement) connection.createStatement();
			ResultSet rs = s

					.executeQuery("select  *  from    tpl_members  where  id="
							+ result);

			if (rs.next()) {

				if (rs.getInt("member_exam_record") == 1) {
					MyActivityManager.exit();
					Assist.transActivity(CaptureActivity.this,
							QRcodeActivity.class);
					AssetManager am = getAssets();
					AssetFileDescriptor afd = null;
					try {
						afd = am.openFd("click6.mp3");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MediaPlayer mplayer = new MediaPlayer();
					try {
						mplayer.setDataSource(afd.getFileDescriptor(),
								afd.getStartOffset(), afd.getLength());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						mplayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;
					mplayer.start();

					Toast toast = Toast.makeText(CaptureActivity.this, " \n "
							+ "重复签到!" + " \n "

							+ "姓名:" + rs.getObject("member_name").toString()
							+ " \n " + "考勤号:" + rs.getObject("id").toString()
							+ "\n " + "工号:"
							+ rs.getObject("member_id").toString() + " \n ",

					Toast.LENGTH_LONG);
					toast.show();
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(20000);

				}

				else if (rs.getInt("member_exam_record") == 0) {
					MyActivityManager.exit();
					Assist.transActivity(CaptureActivity.this,
							QRcodeActivity.class);

					AssetManager am = getAssets();
					AssetFileDescriptor afd = null;
					try {
						afd = am.openFd("kuangke.mp3");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MediaPlayer mplayer = new MediaPlayer();
					try {
						mplayer.setDataSource(afd.getFileDescriptor(),
								afd.getStartOffset(), afd.getLength());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// // / // TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						mplayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;
					mplayer.start();
					CustomUtil.updateone(result);
					CustomUtil.updatekuangke(result);
					MyActivityManager.exit();

					// s.executeUpdate("update   tpl_members  set member_ip="
					// + getIpAddress().toString() + " where id=" + result);
					Intent intent = new Intent(CaptureActivity.this,
							WelcomeActivity.class);
					PendingIntent pi = PendingIntent.getActivity(
							CaptureActivity.this, 0, intent, 0);
					// 创建一个Notification
					Notification notify = new Notification();
					// 为Notification设置图标，该图标显示在状态栏
					notify.icon = R.drawable.yun;
					// 为Notification设置文本内容，该文本会显示在状态栏
					notify.tickerText = "签到成功!";
					// 为Notification设置发送签到时间:

					// 为Notification设置声音
					notify.defaults = Notification.DEFAULT_SOUND;
					// 为Notification设置默认声音、默认振动、默认闪光灯
					notify.defaults = Notification.DEFAULT_ALL;
					// 设置事件信息
					notify.setLatestEventInfo(CaptureActivity.this, "姓名:"
							+ rs.getObject("member_name").toString() + ","
							+ "考勤号:" + rs.getObject("id").toString(),

					"签到状态:旷课,时间:" + str_date, pi);
					// 获取系统的NotificationManager服务
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					// 发送通知
					notificationManager.notify(NOTIFICATION_ID, notify);

				}
			}
			connection.close();

		}

	}

	public void panduantime(Result result) throws Exception {
		{

			Date date = new Date();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str_date = fmt.format(date);
			long time = date.getTime() / 1000;

			Statement s = (Statement) connection.createStatement();
			ResultSet rs = s

			.executeQuery("select  *  from   tpl_examrecord");

			if (rs.next()) {
				// 正常判断
				if ((time)
						- Integer.valueOf(rs
								.getString("exam_record_ontime_start_time")) <= 0) {
					panduanzhengchang(result);

				}
				// 迟到判断
				else if ((time
						- Integer.valueOf(rs
								.getString("exam_record_ontime_start_time")) > 0 && (time
						- Integer.valueOf(rs
								.getString("exam_record_late_end_time")) <= 0))) {

					panduanchidao(result);

				} else if (time
						- Integer.valueOf(rs
								.getString("exam_record_late_end_time")) > 0) {
					panduankuangke(result);

				}

			}
		}
	}

	public void chaxun(Result result) throws SQLException {

		connection = CustomUtil.getConnection();

		Statement s = (Statement) connection.createStatement();

		ResultSet rs = s

		.executeQuery("select  *  from    tpl_members  where id=" + result);
		if (!rs.next()) {

			AssetManager am = getAssets();
			AssetFileDescriptor afd = null;
			try {
				afd = am.openFd("click5.mp3");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MediaPlayer mplayer = new MediaPlayer();
			try {
				mplayer.setDataSource(afd.getFileDescriptor(),
						afd.getStartOffset(), afd.getLength());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				mplayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
			mplayer.start();
			MyActivityManager.exit();
			Assist.transActivity(CaptureActivity.this, QRcodeActivity.class);

			Toast toast = Toast.makeText(CaptureActivity.this, "系统中无此人信息!",

			Toast.LENGTH_SHORT);
			toast.show();
			connection.close();
		}

	}

	// 这是单元测试里的
	private final DialogInterface.OnClickListener aboutListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialogInterface, int i) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(getString(R.string.zxing_url)));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			startActivity(intent);
		}
	};

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_capture);
		MyActivityManager.addActivity(CaptureActivity.this);

		CustomUtil.isNetworkAvailable(getApplicationContext());

		if (CustomUtil.isNetworkAvailable(getApplicationContext()) == false) {
			MyActivityManager.exit();
			Toast.makeText(getApplicationContext(), "当前网络不可用，请检查网络设置",
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(
					android.provider.Settings.ACTION_SETTINGS); // 系统设置
			startActivityForResult(intent, 0);

		}
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		resultView = findViewById(R.id.result_view);
		statusView = (TextView) findViewById(R.id.status_view);
		handler = null;
		lastResult = null;
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		// showHelpOnFirstLaunch();
	}

	@Override
	protected void onResume() {
		super.onResume();
		resetStatusView();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);

		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		Intent intent = getIntent();
		String action = intent == null ? null : intent.getAction();
		String dataString = intent == null ? null : intent.getDataString();
		if (intent != null && action != null) {
			if (action.equals(Intents.Scan.ACTION)) {
				// Scan the formats the intent requested, and return the result
				// to the calling activity.
				source = Source.NATIVE_APP_INTENT;
				decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
			} else if (dataString != null
					&& dataString.contains(PRODUCT_SEARCH_URL_PREFIX)
					&& dataString.contains(PRODUCT_SEARCH_URL_SUFFIX)) {

				source = Source.PRODUCT_SEARCH_LINK;
				sourceUrl = dataString;
				decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;
			} else if (dataString != null && dataString.startsWith(ZXING_URL)) {

				source = Source.ZXING_LINK;
				sourceUrl = dataString;
				Uri inputUri = Uri.parse(sourceUrl);
				returnUrlTemplate = inputUri
						.getQueryParameter(RETURN_URL_PARAM);
				decodeFormats = DecodeFormatManager
						.parseDecodeFormats(inputUri);
			} else {

				source = Source.NONE;
				decodeFormats = null;
			}
			characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
		} else {
			source = Source.NONE;
			decodeFormats = null;
			characterSet = null;
		}

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		playBeep = prefs.getBoolean(PreferencesActivity.KEY_PLAY_BEEP, true);
		if (playBeep) {

			AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
			if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
				playBeep = false;
			}
		}
		vibrate = prefs.getBoolean(PreferencesActivity.KEY_VIBRATE, false);
		copyToClipboard = prefs.getBoolean(
				PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true);
		initBeepSound();
	}

	@Override
	protected void onPause() {
		MyActivityManager.exit();
		Assist.transActivity(CaptureActivity.this, QRcodeActivity.class);
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			MyActivityManager.exit();
			Assist.transActivity(CaptureActivity.this, QRcodeActivity.class);

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	// Don't display the share menu item if the result overlay is showing.

	@Override
	public void onConfigurationChanged(Configuration config) {
		// Do nothing, this is to prevent the activity from being restarted when
		// the keyboard opens.
		super.onConfigurationChanged(config);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void handleDecode(Result rawResult, Bitmap barcode) throws Exception {

		lastResult = rawResult;
		MyActivityManager.exit();
		Assist.transActivity(CaptureActivity.this, QRcodeActivity.class);
		chaxun(rawResult);
		panduantime(rawResult);

		if (barcode == null) {
			// This is from history -- no saved barcode

		} else {

		}

		switch (source) {
		case NATIVE_APP_INTENT:
		case PRODUCT_SEARCH_LINK:
			handleDecodeExternally(rawResult, barcode);
			break;
		case ZXING_LINK:
			if (returnUrlTemplate == null) {
				handleDecodeInternally(rawResult, barcode);
			} else {
				handleDecodeExternally(rawResult, barcode);
			}
			break;
		case NONE:
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			if (prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
				Toast.makeText(this, R.string.msg_bulk_mode_scanned,
						Toast.LENGTH_SHORT).show();
				// Wait a moment or else it will scan the same barcode
				// continuously about 3 times
				if (handler != null) {
					handler.sendEmptyMessageDelayed(R.id.restart_preview,
							BULK_MODE_SCAN_DELAY_MS);
				}
				resetStatusView();
			} else {
				handleDecodeInternally(rawResult, barcode);
			}
			break;
		}
	}

	private void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_image_border));
			paint.setStrokeWidth(3.0f);
			paint.setStyle(Paint.Style.STROKE);
			Rect border = new Rect(2, 2, barcode.getWidth() - 2,
					barcode.getHeight() - 2);
			canvas.drawRect(border, paint);

			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat()
							.equals(BarcodeFormat.UPC_A))
					|| (rawResult.getBarcodeFormat()
							.equals(BarcodeFormat.EAN_13))) {

				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	private void handleDecodeInternally(Result rawResult, Bitmap barcode) {
		statusView.setVisibility(View.GONE);
		viewfinderView.setVisibility(View.GONE);
		resultView.setVisibility(View.VISIBLE);

		ImageView barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view);
		if (barcode == null) {
			barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.icon));
		} else {
			barcodeImageView.setImageBitmap(barcode);
		}

		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
				this, rawResult);

		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
		String formattedTime = formatter.format(new Date(rawResult
				.getTimestamp()));
		TextView timeTextView = (TextView) findViewById(R.id.time_text_view);
		timeTextView.setText(formattedTime);

		TextView metaTextView = (TextView) findViewById(R.id.meta_text_view);
		View metaTextViewLabel = findViewById(R.id.meta_text_view_label);
		metaTextView.setVisibility(View.GONE);
		metaTextViewLabel.setVisibility(View.GONE);
		Map<ResultMetadataType, Object> metadata = (Map<ResultMetadataType, Object>) rawResult
				.getResultMetadata();
		if (metadata != null) {
			StringBuilder metadataText = new StringBuilder(20);
			for (Map.Entry<ResultMetadataType, Object> entry : metadata
					.entrySet()) {
				if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
					metadataText.append(entry.getValue()).append('\n');
				}
			}
			if (metadataText.length() > 0) {
				metadataText.setLength(metadataText.length() - 1);
				metaTextView.setText(metadataText);
				metaTextView.setVisibility(View.VISIBLE);
				metaTextViewLabel.setVisibility(View.VISIBLE);
			}
		}

		TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
		CharSequence displayContents = resultHandler.getDisplayContents();
		contentsTextView.setText(displayContents);
		// Crudely scale betweeen 22 and 32 -- bigger font for shorter text
		int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
		contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

		int buttonCount = resultHandler.getButtonCount();
		ViewGroup buttonView = (ViewGroup) findViewById(R.id.result_button_view);
		buttonView.requestFocus();
		for (int x = 0; x < ResultHandler.MAX_BUTTON_COUNT; x++) {
			TextView button = (TextView) buttonView.getChildAt(x);
			if (x < buttonCount) {
				button.setVisibility(View.VISIBLE);
				button.setText(resultHandler.getButtonText(x));
				button.setOnClickListener(new ResultButtonListener(
						resultHandler, x));
			} else {
				button.setVisibility(View.GONE);
			}
		}

		if (copyToClipboard) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(displayContents);
		}
	}

	private void handleDecodeExternally(Result rawResult, Bitmap barcode) {
		viewfinderView.drawResultBitmap(barcode);

		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
				this, rawResult);
		statusView.setText(getString(resultHandler.getDisplayTitle()));

		if (copyToClipboard) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(resultHandler.getDisplayContents());
		}

		if (source == Source.NATIVE_APP_INTENT) {

			Intent intent = new Intent(getIntent().getAction());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
			intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult
					.getBarcodeFormat().toString());
			Message message = Message.obtain(handler, R.id.return_scan_result);
			message.obj = intent;
			handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
		} else if (source == Source.PRODUCT_SEARCH_LINK) {

			Message message = Message
					.obtain(handler, R.id.launch_product_query);
			int end = sourceUrl.lastIndexOf("/scan");
			message.obj = sourceUrl.substring(0, end) + "?q="
					+ resultHandler.getDisplayContents().toString()
					+ "&source=zxing";
			handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
		} else if (source == Source.ZXING_LINK) {

			Message message = Message
					.obtain(handler, R.id.launch_product_query);
			message.obj = returnUrlTemplate.replace(RETURN_CODE_PLACEHOLDER,
					resultHandler.getDisplayContents().toString());
			handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {

			setVolumeControlStream(AudioManager.STREAM_MUSIC);

			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();

		}
		if (vibrate) {

			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
			return;
		} catch (RuntimeException e) {

			Log.w(TAG, "Unexpected error initializating camera", e);
			displayFrameworkBugMessageAndExit();
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	private void resetStatusView() {
		resultView.setVisibility(View.GONE);
		statusView.setText(R.string.msg_default_status);
		statusView.setVisibility(View.VISIBLE);
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

}
