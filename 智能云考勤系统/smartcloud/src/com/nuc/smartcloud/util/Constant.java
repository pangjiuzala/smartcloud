package com.nuc.smartcloud.util;

import java.io.File;

import android.R.string;
import android.os.Environment;

public class Constant {
	public static final String ACTION_PAUSE_ALL = null;
	public static final String ACTION_STOP_SRC = null;
	public static final String ACTION_PLAY_ALL = null;
	public static final Object ACTION_INIT_BACKGROUND = null;
	public static final Object ACTION_PLAY_BACKGROUND = null;
	public static final Object ACTION_INIT_SRC = null;
	public static String path = "/sdcard/Rival/";
	public static final float appVersion = 0.6f; // ��ǰ��Ϸ�汾�ţ�ÿ����һ���µİ汾��ȥ��Ҫ�����ֵ
	public static final boolean CNZZ = false;

	public static final int lowScreenW = 450;
	public static final int highScreenW = 850;

	public static final int highScreenSize = 6;

	public static final String path1 = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "Vi"
			+ File.separator
			+ "ScreenShot"
			+ File.separator
			+ "FireControl";

	public static final int playerAllHP = 10; // ���ǵ���Ѫ��

	// ������Ϸ״̬����
	public static final byte GAME_PAUSE = 0; // ��Ϸ��ͣ
	public static final byte GAMEING = 1; // ��Ϸ��
	public static final byte GAME_WIN = 2; // ��Ϸʤ��
	public static final byte GAME_FAIL = 3; // ��Ϸʧ��
	public static final byte GAME_DRAMA = 4; // ����ģʽ
	public static final byte RECYCLE_DRAMA = 6;

	// ���� �����������Ͳ�Ҫ�ģ���������
	// ���� �����������Ͳ�Ҫ�ģ���������
	public static final short TYPE_BACKGRUND01_01 = 1;
	public static final short TYPE_BACKGRUND01_02 = 2;
	public static final short TYPE_BACKGRUND01_03 = 3;
	public static final short TYPE_BACKGRUND01_04 = 4; // ������

	public static final short TYPE_BACKGRUND02 = -7; // ������
	public static final short TYPE_DRAMA_LEVEL01_MARS = -6; // ��һ�ؿ��Ļ���
	public static final short TYPE_DRAMA_LEVEL01_EARTH = -5; // ��һ�ؿ��ĵ���
	public static final short TYPE_DRAMA_LEVEL01_SUN = -4; // ��һ�ؿ���̫��

	public static final short TYPE_PLAYER_ALL = -1; // ���ǵ�

	public static final short TYPE_PLAYER = 0; // ���ǵ�
	public static final short TYPE_FLY = 1; // ��Ӭ
	public static final short TYPE_DUCKL = 2; // Ѽ��(���������˶�)
	public static final short TYPE_DUCKR = 3; // Ѽ��(���������˶�)
	public static final short TYPE_BOSS = 4; // Boss��

	public static final short TYPE_PLAYER_BULLET = 5; // ���ǵ�ͼƬ
	public static final short TYPE_FLY_BULLET = 6; // ��Ӭ��ͼƬ
	public static final short TYPE_DUCK_BULLET = 7; // Ѽ�ӵ�ͼƬ
	public static final short TYPE_BOSS_BULLET = 8; // Boss��ͼƬ
	public static final short TYPE_BOSS_BULLET_CRAZY = 9; // Boss��ͼƬ
	public static final short TYPE_BOOM_DUCK = 10; // ��ը��ͼƬ
	public static final short TYPE_BOSSBOOM = 11; // BOSS��ը��ͼƬ

	public static final short TYPE_PowerUPs_GOLD01 = 12; // ��ҵ�PowerUPs
	public static final short TYPE_PowerUPs_HP = 13; // ��Ѫ��PowerUPs
	public static final short TYPE_PowerUPs_BULLET_01 = 14; // �ӵ�01��PowerUPs
	public static final short TYPE_PowerUPs_NUCLEAR_01 = 15; // ����01��PowerUPs
	public static final short TYPE_PowerUPs_NUCLEAR_01_LINE = 16; // ����01��PowerUPs
	public static final short TYPE_PowerUPs_DOUBLEBULLET = 17; // ˫���ӵ���PowerUPs

	public static final short TYPE_BOOM_FLY = 18; // FLY��ը��ͼƬ
	// public static final short TYPE_BOOM_BOSS_FINAL = 19; // BOSS_FINAL��ը��ͼƬ

	// �Ʒ�
	public static final short SCORE_FLY = 2; // ��Ӭ
	public static final short SCORE_DUCK = 4; // Ѽ��
	public static final short SCORE_BOSS = 8; // Ѽ��

	// 8������
	public static final byte DIR_UP = 1;
	public static final byte DIR_DOWN = 2;
	public static final byte DIR_LEFT = 3;
	public static final byte DIR_RIGHT = 4;
	public static final byte DIR_UP_LEFT = 5;
	public static final byte DIR_UP_RIGHT = 6;
	public static final byte DIR_DOWN_LEFT = 7;
	public static final byte DIR_DOWN_RIGHT = 8;

	// ս������ת���ֱ��
	public static final byte TURN_NORMAL = 0;
	public static final byte TURN_LEFT = 1;
	public static final byte TURN_RIGHT = 2;

	// ����Ĵ���Ĺ����Ǵ�������йز��ſ��Ƶ�Intent,��Ȼ���Ƕ���Ҫ��AndroidManifest.xml�н��ж��壨���ã�
	public static final int SOUNDPOOL_MENUSELECT = 0;
	public static final int SOUNDPOOL_EXPLOSION = 1;
	public static final int SOUNDPOOL_POWERUPS_COIN = 2;
	public static final int SOUNDPOOL_POWERUPS_NUCLEAR01_LINE = 3;

	public static final String SMARTCLOUD_FIRST = "username";
	public static final String SMARTCLOUD_SECOND = "password";

}
