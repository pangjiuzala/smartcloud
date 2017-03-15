package com.nuc.smartcloud.util;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * �ֻ��𶯹�����
 * 
 * @author Administrator
 * 
 */
public class VibratorUtils {

	private static boolean isVibrate;

	public static final void initIsVibrate(boolean isVibrate) {
		VibratorUtils.isVibrate = isVibrate;
	}

	/**
	 * final Activity activity �����ø÷�����Activityʵ�� long milliseconds ���𶯵�ʱ������λ�Ǻ���
	 * long[] pattern ���Զ�����ģʽ �����������ֵĺ���������[��ֹʱ������ʱ������ֹʱ������ʱ��������]ʱ���ĵ�λ�Ǻ���
	 */
	public static final void Vibrate(final Activity activity, long milliseconds) {
		if (isVibrate) {
			Vibrator vib = (Vibrator) activity
					.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(milliseconds);
		}
	}
	/*
	 * public static void Vibrate(final Activity activity, long[] pattern) { if
	 * (PrefsActivity.getVibrator(activity)) { Vibrator vib = (Vibrator)
	 * activity.getSystemService(Service.VIBRATOR_SERVICE); vib.vibrate(pattern,
	 * -1); } }
	 */
}
