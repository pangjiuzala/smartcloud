package com.nuc.smartcloud.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import com.nuc.smartcloud.R;
import com.nuc.smartcloud.client.android.CaptureActivity;
import com.nuc.smartcloud.util.Assist;
import com.nuc.smartcloud.util.CustomUtil;
import com.nuc.smartcloud.util.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordQueryActivity extends ListActivity {
	String jsonString = "null";;
	private Connection connection;
	private Statement statement;
	private Button bn;
	String time = null;
	public static String Nums;

	private List<String> myMusicList = new ArrayList<String>();

	private int currentListItem = 0; // 当前播放歌曲的索引

	ProgressDialog progressDialog;

	// 绑定音乐

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			MyActivityManager.exit();
			Assist.transActivity(RecordQueryActivity.this,
					WelcomeActivity.class);

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	

	void musicList() throws Exception {

		ArrayAdapter<String> musicList = new ArrayAdapter<String>(this,
				R.layout.music_text, myMusicList);

		setListAdapter(musicList);

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		currentListItem = position;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_query);
		MyActivityManager.addActivity(RecordQueryActivity.this);
		CustomUtil.isNetworkAvailable(getApplicationContext());

		if (CustomUtil.isNetworkAvailable(getApplicationContext()) == false) {
			Toast.makeText(getApplicationContext(), "当前网络不可用,请检查网络设置",
					Toast.LENGTH_SHORT).show();

		}

		final Button bn = (Button) findViewById(R.id.chaxun);
		bn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				EditText Name = (EditText) findViewById(R.id.num);
				String nums = Name.getText().toString().trim();
				Name.getText().clear();
				Name.setVisibility(View.GONE);
				bn.setVisibility(View.GONE);
				try {

					connection = CustomUtil.getConnection();
					Statement s = (Statement) connection.createStatement();

					ResultSet rs = s

							.executeQuery("select  *  from  tpl_members where member_id ="
									+ nums
									+ " or id="
									+ nums
									+ " or member_phone=" + nums);
					;

					if (rs.next()) {
						Name.setVisibility(View.VISIBLE);
						bn.setVisibility(View.VISIBLE);
						Toast.makeText(getApplicationContext(), "查询成功!",
								Toast.LENGTH_SHORT).show();
						myMusicList.add("姓   名："
								+ rs.getObject("member_name").toString());
						myMusicList.add("员工号:"
								+ rs.getObject("member_id").toString());
						myMusicList.add("考勤号:" + rs.getObject("id").toString());
						myMusicList.add("手机号:"
								+ rs.getObject("member_phone").toString());
						myMusicList.add("员工号:"
								+ rs.getObject("member_id").toString());
						myMusicList.add("考勤情况(1:是   0:否)");
						myMusicList
								.add("是否考勤:"
										+ rs.getObject("member_exam_record")
												.toString());
						myMusicList.add("正常:"
								+ rs.getObject("member_ontime").toString());
						myMusicList.add("迟到:"
								+ rs.getObject("member_late").toString());
						myMusicList.add("旷课:"
								+ rs.getObject("member_cut_class").toString());
						musicList();
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
						connection.close();

					} else {
						Name.setVisibility(View.VISIBLE);
						bn.setVisibility(View.VISIBLE);
						InputMethodManager imm2 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm2.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);

						Toast.makeText(getApplicationContext(), "该员工尚未注册!",
								Toast.LENGTH_SHORT).show();
						connection.close();
					}

				} catch (Exception e) {
					Name.setVisibility(View.VISIBLE);
					bn.setVisibility(View.VISIBLE);
					InputMethodManager im3 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					im3.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					Toast.makeText(getApplicationContext(), "查询失败,请检查输入或网络设置",
							Toast.LENGTH_SHORT).show();

				}
			}
		});

	}
}