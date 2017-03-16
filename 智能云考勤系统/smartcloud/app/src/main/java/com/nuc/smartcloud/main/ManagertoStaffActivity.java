package com.nuc.smartcloud.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.jar.Attributes.Name;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.nuc.smartcloud.client.android.CaptureActivity;
import com.nuc.smartcloud.util.Assist;
import com.nuc.smartcloud.util.CustomUtil;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ManagertoStaffActivity extends Activity {
	String jsonString = "null";;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	EditText Username;
	EditText Password;
	String time = null;
	ProgressDialog progressDialog;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			MyActivityManager.exit();
			Assist.transActivity(ManagertoStaffActivity.this,
					WelcomeActivity.class);

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}



	Handler myhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 0) {

				progressDialog.dismiss();
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				Toast.makeText(getApplicationContext(), "验证成功!",
						Toast.LENGTH_SHORT).show();
				Assist.transActivity(ManagertoStaffActivity.this,
						StaffDeleteActivity.class);
			} else if (msg.arg1 == 1) {

				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				Assist.transActivity(ManagertoStaffActivity.this,
						ManagertoStaffActivity.class);
				Toast.makeText(getApplicationContext(), "验证失败!",
						Toast.LENGTH_SHORT).show();
			} else if (msg.arg1 == 2) {

				progressDialog.dismiss();
				Assist.transActivity(ManagertoStaffActivity.this,
						ManagertoStaffActivity.class);
				Toast.makeText(getApplicationContext(), "验证失败,请检查输入或网络设置",
						Toast.LENGTH_SHORT).show();
			}

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_show);
		MyActivityManager.addActivity(ManagertoStaffActivity.this);
		CustomUtil.isNetworkAvailable(getApplicationContext());

		if (CustomUtil.isNetworkAvailable(getApplicationContext()) == false) {
			Toast.makeText(getApplicationContext(), "当前网络不可用,请检查网络设置",
					Toast.LENGTH_SHORT).show();

		}
		final Builder builder = new AlertDialog.Builder(this);
		// 为按钮绑定事件监听器

		// 设置对话框的图标
		builder.setIcon(R.drawable.yun);
		// 设置对话框的标题
		builder.setTitle("请您验证管理员信息");
		// 装载/res/layout/login.xml界面布局

		final TableLayout loginForm = (TableLayout) getLayoutInflater()
				.inflate(R.layout.activity_super, null);

		// 设置对话框显示的View对象
		builder.setView(loginForm);
		// 为对话框设置一个“确定”按钮
		builder.setPositiveButton("验证"
				// 为按钮设置监听器
				, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);

						progressDialog = ProgressDialog.show(
								ManagertoStaffActivity.this, "验证中", "请您稍后...",
								false, true);
						new Thread(new Runnable() {

							public void run() {

								try {
									Looper.prepare();
									EditText Username = (EditText) loginForm
											.findViewById(R.id.username);
									EditText Password = (EditText) loginForm
											.findViewById(R.id.password);
									String username = Username.getText()
											.toString().trim();
									String password = Password.getText()
											.toString().trim();
									connection = CustomUtil.getConnection();
									Statement s = (Statement) connection
											.createStatement();
									ResultSet rs = s

											.executeQuery("select  *  from   tpl_super where username="
													+ username
													+ " and password="
													+ password + ";");
									if (rs.next())

									{
										Message msg = new Message();
										msg.arg1 = 0;
										myhandler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = 1;
										myhandler.sendMessage(msg);
									}
									connection.close();
								} catch (Exception e) {
									Message msg = new Message();
									msg.arg1 = 2;
									myhandler.sendMessage(msg);

								} finally {
									progressDialog.dismiss();

								}

							}

						}).start();

					}
				});
		// 为对话框设置一个“取消”按钮
		builder.setNegativeButton("返回", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				Assist.transActivity(ManagertoStaffActivity.this,
						WelcomeActivity.class);
			}
		});
		// 创建、并显示对话框
		builder.create().show();
	}

}