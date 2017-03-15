package com.nuc.smartcloud.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.R.string;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.nuc.smartcloud.Result;

public class CustomUtil {
	private static Connection connection;
	private static Statement statement;
	private static ResultSet resultSet;

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {

						return true;
					}
				}
			}
		}
		return false;
	}

	public static void updatezhengchang(Result result) throws SQLException {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str_date = fmt.format(rightNow.getTime());
		connection = getConnection();

		Statement s = (Statement) connection.createStatement();
		ResultSet rs1 = s

		.executeQuery("select  *  from    tpl_members  where id=" + result);
		if (rs1.next()) {
			int m = rs1.getInt("member_ontime");

			m++;

			s.executeUpdate("update tpl_members set  member_ontime='" + m
					+ "' where id=" + result);

			connection.close();

		}
		connection.close();
	}

	private static String tel() throws Exception {
		connection = (Connection) getConnection();
		Statement s2 = (Statement) connection.createStatement();

		ResultSet rs2 = s2

		.executeQuery("select  *  from   tpl_super ");
		rs2.next();
		return rs2.getObject("tel").toString();
	}

	public static void updatechidao(Result result) throws SQLException {

		connection = getConnection();

		Statement s = (Statement) connection.createStatement();
		ResultSet rs1 = s

		.executeQuery("select  *  from    tpl_members  where id=" + result);
		if (rs1.next()) {
			int m = rs1.getInt("member_late");

			m++;

			s.executeUpdate("update tpl_members set  member_late='" + m
					+ "' where id=" + result);

		}
		connection.close();
	}

	public static void updatekuangke(Result result) throws SQLException {

		connection = getConnection();

		Statement s = (Statement) connection.createStatement();
		ResultSet rs1 = s

		.executeQuery("select  *  from    tpl_members  where id=" + result);
		if (rs1.next()) {
			int m = rs1.getInt("member_cut_class");

			m++;

			s.executeUpdate("update tpl_members set  member_cut_class='" + m
					+ "' where id=" + result);

		}
		connection.close();
	}

	public static void updateone(Result result) throws SQLException {

		connection = getConnection();

		Statement s = (Statement) connection.createStatement();

		s.executeUpdate("update   tpl_members  set member_exam_record='1' where id="
				+ result);

		connection.close();
	}

	public static Connection getConnection() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://your Server/your database "; // 10.28.52.193
																	// localhost

		// Md5(Constant.SMARTCLOUD_SECOND);
		try {
			Class.forName(driver);
			connection = (Connection) DriverManager.getConnection(url,
					Constant.SMARTCLOUD_FIRST, Constant.SMARTCLOUD_SECOND);
			if (!connection.isClosed()) {

			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
}
