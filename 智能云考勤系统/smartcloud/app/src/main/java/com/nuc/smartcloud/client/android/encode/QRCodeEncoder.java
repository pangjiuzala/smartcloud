

package com.nuc.smartcloud.client.android.encode;

import com.nuc.smartcloud.BarcodeFormat;
import com.nuc.smartcloud.EncodeHintType;
import com.nuc.smartcloud.MultiFormatWriter;
import com.nuc.smartcloud.Result;
import com.nuc.smartcloud.WriterException;
import com.nuc.smartcloud.client.android.Contents;
import com.nuc.smartcloud.client.android.Intents;
import com.nuc.smartcloud.client.result.ParsedResult;
import com.nuc.smartcloud.client.result.ResultParser;
import com.nuc.smartcloud.common.BitMatrix;
import com.nuc.smartcloud.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;


final class QRCodeEncoder {

	private static final String TAG = QRCodeEncoder.class.getSimpleName();

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	private final Activity activity;
	private String contents;
	private String displayContents;
	private String title;
	private BarcodeFormat format;

	QRCodeEncoder(Activity activity, Intent intent) {
		this.activity = activity;
		if (intent == null) {
			throw new IllegalArgumentException("No valid data to encode.");
		}

		String action = intent.getAction();
		if (action.equals(Intents.Encode.ACTION)) {
			if (!encodeContentsFromZXingIntent(intent)) {
				throw new IllegalArgumentException("No valid data to encode.");
			}
		} else if (action.equals(Intent.ACTION_SEND)) {
			if (!encodeContentsFromShareIntent(intent)) {
				throw new IllegalArgumentException("No valid data to encode.");
			}
		}
	}

	public void requestBarcode(Handler handler, int pixelResolution) {
		Thread encodeThread = new EncodeThread(contents, handler,
				pixelResolution, format);
		encodeThread.start();
	}

	public String getContents() {
		return contents;
	}

	public String getDisplayContents() {
		return displayContents;
	}

	public String getTitle() {
		return title;
	}

	public String getFormat() {
		return format.toString();
	}

	
	private boolean encodeContentsFromZXingIntent(Intent intent) {
	
		String formatString = intent.getStringExtra(Intents.Encode.FORMAT);
		try {
			format = BarcodeFormat.valueOf(formatString);
		} catch (IllegalArgumentException iae) {
			// Ignore it then
			format = null;
		}
		if (format == null || BarcodeFormat.QR_CODE.equals(format)) {
			String type = intent.getStringExtra(Intents.Encode.TYPE);
			if (type == null || type.length() == 0) {
				return false;
			}
			this.format = BarcodeFormat.QR_CODE;
			encodeQRCodeContents(intent, type);
		} else {
			String data = intent.getStringExtra(Intents.Encode.DATA);
			if (data != null && data.length() > 0) {
				contents = data;
				displayContents = data;
				title = activity.getString(R.string.contents_text);
			}
		}
		return contents != null && contents.length() > 0;
	}

	
	private boolean encodeContentsFromShareIntent(Intent intent) {
		format = BarcodeFormat.QR_CODE;
		try {
			Uri uri = (Uri) intent.getExtras().getParcelable(
					Intent.EXTRA_STREAM);
			InputStream stream = activity.getContentResolver().openInputStream(
					uri);
			int length = stream.available();
			if (length <= 0) {
				Log.w(TAG, "Content stream is empty");
				return false;
			}
			byte[] vcard = new byte[length];
			int bytesRead = stream.read(vcard, 0, length);
			if (bytesRead < length) {
				Log.w(TAG,
						"Unable to fully read available bytes from content stream");
				return false;
			}
			String vcardString = new String(vcard, 0, bytesRead, "UTF-8");
			Log.d(TAG, "Encoding share intent content:");
			Log.d(TAG, vcardString);
			Result result = new Result(vcardString, vcard, null,
					BarcodeFormat.QR_CODE);
			ParsedResult parsedResult = ResultParser.parseResult(result);

		} catch (IOException e) {
			Log.w(TAG, e);
			return false;
		} catch (NullPointerException e) {
			Log.w(TAG, e);
			// In case the uri was not found in the Intent.
			return false;
		}
		return contents != null && contents.length() > 0;
	}

	private void encodeQRCodeContents(Intent intent, String type) {
		if (type.equals(Contents.Type.TEXT)) {
			String data = intent.getStringExtra(Intents.Encode.DATA);
			if (data != null && data.length() > 0) {
				contents = data;
				displayContents = data;
				title = activity.getString(R.string.contents_text);
			}
		} else if (type.equals(Contents.Type.EMAIL)) {
			String data = trim(intent.getStringExtra(Intents.Encode.DATA));
			if (data != null) {
				contents = "mailto:" + data;
				displayContents = data;
				title = activity.getString(R.string.contents_email);
			}
		} else if (type.equals(Contents.Type.PHONE)) {
			String data = trim(intent.getStringExtra(Intents.Encode.DATA));
			if (data != null) {
				contents = "tel:" + data;
				displayContents = PhoneNumberUtils.formatNumber(data);
				title = activity.getString(R.string.contents_phone);
			}
		} else if (type.equals(Contents.Type.SMS)) {
			String data = trim(intent.getStringExtra(Intents.Encode.DATA));
			if (data != null) {
				contents = "sms:" + data;
				displayContents = PhoneNumberUtils.formatNumber(data);
				title = activity.getString(R.string.contents_sms);
			}
		} else if (type.equals(Contents.Type.CONTACT)) {
			Bundle bundle = intent.getBundleExtra(Intents.Encode.DATA);
			if (bundle != null) {
				StringBuilder newContents = new StringBuilder(100);
				StringBuilder newDisplayContents = new StringBuilder(100);
				newContents.append("MECARD:");
				String name = trim(bundle
						.getString(Contacts.Intents.Insert.NAME));
				if (name != null) {
					newContents.append("N:").append(escapeMECARD(name))
							.append(';');
					newDisplayContents.append(name);
				}
				String address = trim(bundle
						.getString(Contacts.Intents.Insert.POSTAL));
				if (address != null) {
					newContents.append("ADR:").append(escapeMECARD(address))
							.append(';');
					newDisplayContents.append('\n').append(address);
				}
				for (int x = 0; x < Contents.PHONE_KEYS.length; x++) {
					String phone = trim(bundle
							.getString(Contents.PHONE_KEYS[x]));
					if (phone != null) {
						newContents.append("TEL:").append(escapeMECARD(phone))
								.append(';');
						newDisplayContents.append('\n').append(
								PhoneNumberUtils.formatNumber(phone));
					}
				}
				for (int x = 0; x < Contents.EMAIL_KEYS.length; x++) {
					String email = trim(bundle
							.getString(Contents.EMAIL_KEYS[x]));
					if (email != null) {
						newContents.append("EMAIL:")
								.append(escapeMECARD(email)).append(';');
						newDisplayContents.append('\n').append(email);
					}
				}
				// Make sure we've encoded at least one field.
				if (newDisplayContents.length() > 0) {
					newContents.append(';');
					contents = newContents.toString();
					displayContents = newDisplayContents.toString();
					title = activity.getString(R.string.contents_contact);
				} else {
					contents = null;
					displayContents = null;
				}
			}
		} else if (type.equals(Contents.Type.LOCATION)) {
			Bundle bundle = intent.getBundleExtra(Intents.Encode.DATA);
			if (bundle != null) {
				// These must use Bundle.getFloat(), not getDouble(), it's part
				// of the API.
				float latitude = bundle.getFloat("LAT", Float.MAX_VALUE);
				float longitude = bundle.getFloat("LONG", Float.MAX_VALUE);
				if (latitude != Float.MAX_VALUE && longitude != Float.MAX_VALUE) {
					contents = "geo:" + latitude + ',' + longitude;
					displayContents = latitude + "," + longitude;
					title = activity.getString(R.string.contents_location);
				}
			}
		}
	}

	static Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
			int desiredWidth, int desiredHeight) throws WriterException {
		Hashtable<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contents);
		if (encoding != null) {
			hints = new Hashtable<EncodeHintType, Object>(2);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = writer.encode(contents, format, desiredWidth,
				desiredHeight, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	private static String trim(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		return s.length() == 0 ? null : s;
	}

	private static String escapeMECARD(String input) {
		if (input == null || (input.indexOf(':') < 0 && input.indexOf(';') < 0)) {
			return input;
		}
		int length = input.length();
		StringBuilder result = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			char c = input.charAt(i);
			if (c == ':' || c == ';') {
				result.append('\\');
			}
			result.append(c);
		}
		return result.toString();
	}

}
