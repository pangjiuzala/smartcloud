package com.nuc.smartcloud.util;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtils {

	/**
	 * ��ȡsrcĿ¼�µ�ͼƬ
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static final Bitmap getSrcBitmap(Context context, String path) {
		try {
			// ��ȡͼƬ��InputStream
			InputStream is = context.getClassLoader().getResourceAsStream(path);
			// InputStreamת����Bitmap
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();
			return bitmap;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * ����������ͼƬ
	 * 
	 * @param bitmap
	 * @param scaleWidth
	 * @param scaleHeight
	 * @return
	 */
	public static final Bitmap setScaleBitmap(Bitmap bitmap, float scaleWidth,
			float scaleHeight) {
		Matrix matrix = new Matrix();
		matrix.setScale(scaleWidth, scaleHeight); // �������ź��Bitmap����
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	public static final Bitmap setBackgroundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		float scale = (float) ((Assist.screenW + 200.0) / width);
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale); // �������ź��Bitmap����
		return Bitmap.createBitmap(bitmap, 0, 0, width, bitmap.getHeight(),
				matrix, true);
	}

	/*
	 * public static Bitmap createBitmap (Bitmap source, int x, int y, int
	 * width, int height, Matrix m, boolean filter)
	 * ���Ĳ���filter���������˲����ã�ͼ�������֪ʶ������һ��ͼ�������Ա任��ƽ�ơ���ת�����ţ�ʱ��
	 * ʵ�ʾ��Ǵ�ԭͼ����������ֵ�õ���ͼ���������ֵ�Ĺ��̣����е���ת�����ű任�õ�����ͼ���и��������
	 * ֵͨ���Ǿ������ֲ�ֵ��õģ�����ڸ���ͼ���������а�����Ҳ�����˲��������ԣ�android�ĵ��е�˵����
	 * �ǣ������еĲ�ֻ��ƽ�Ʊ任ʱ��filter����Ϊtrue���Խ����˲����������ڸ�����ͼ��������
	 */

	public static final Bitmap setRotateBitmap(Bitmap bitmap, float degree) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree); // ���÷�ת�ĽǶ�
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}
}
