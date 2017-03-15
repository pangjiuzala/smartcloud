
package com.nuc.smartcloud.client.android;
import com.nuc.smartcloud.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.Collection;

import com.nuc.smartcloud.main.QRcodeActivity;
import com.nuc.smartcloud.util.Assist;


public final class PreferencesActivity extends PreferenceActivity
    implements OnSharedPreferenceChangeListener {

  public static final String KEY_DECODE_1D = "preferences_decode_1D";
  public static final String KEY_DECODE_QR = "preferences_decode_QR";
  public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
  public static final String KEY_CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";

  public static final String KEY_PLAY_BEEP = "preferences_play_beep";
  public static final String KEY_VIBRATE = "preferences_vibrate";
  public static final String KEY_COPY_TO_CLIPBOARD = "preferences_copy_to_clipboard";
  public static final String KEY_FRONT_LIGHT = "preferences_front_light";
  public static final String KEY_BULK_MODE = "preferences_bulk_mode";

  public static final String KEY_HELP_VERSION_SHOWN = "preferences_help_version_shown";
  public static final String KEY_NOT_OUR_RESULTS_SHOWN = "preferences_not_out_results_shown";


  private CheckBoxPreference decodeQR;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
			Assist.transActivity(PreferencesActivity.this, CaptureActivity.class);

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
  @Override
  protected void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    addPreferencesFromResource(R.xml.preferences);

    PreferenceScreen preferences = getPreferenceScreen();
    preferences.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    decodeQR = (CheckBoxPreference) preferences.findPreference(KEY_DECODE_QR);
   
    disableLastCheckedPref();
  }

  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    disableLastCheckedPref();
  }

  private void disableLastCheckedPref() {
    Collection<CheckBoxPreference> checked = new ArrayList<CheckBoxPreference>(3);
   
    if (decodeQR.isChecked()) {
      checked.add(decodeQR);
    }
    
    boolean disable = checked.size() < 2;
    for (CheckBoxPreference pref : new CheckBoxPreference[] { decodeQR}) {
      pref.setEnabled(!(disable && checked.contains(pref)));
    }
  }

}
