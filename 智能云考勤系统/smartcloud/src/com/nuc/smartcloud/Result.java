

package com.nuc.smartcloud;

import java.util.Enumeration;
import java.util.Hashtable;

public final class Result {

	private final String text;
	private final byte[] rawBytes;
	private ResultPoint[] resultPoints;
	private final BarcodeFormat format;
	private Hashtable resultMetadata;
	private final long timestamp;

	public Result(String text, byte[] rawBytes, ResultPoint[] resultPoints,
			BarcodeFormat format) {
		this(text, rawBytes, resultPoints, format, System.currentTimeMillis());
	}

	public Result(String text, byte[] rawBytes, ResultPoint[] resultPoints,
			BarcodeFormat format, long timestamp) {
		if (text == null && rawBytes == null) {
			throw new IllegalArgumentException("Text and bytes are null");
		}
		this.text = text;
		this.rawBytes = rawBytes;
		this.resultPoints = resultPoints;
		this.format = format;
		this.resultMetadata = null;
		this.timestamp = timestamp;
	}

	public String getText() {
		return text;
	}

	public byte[] getRawBytes() {
		return rawBytes;
	}

	public ResultPoint[] getResultPoints() {
		return resultPoints;
	}

	public BarcodeFormat getBarcodeFormat() {
		return format;
	}

	public Hashtable getResultMetadata() {
		return resultMetadata;
	}

	public void putMetadata(ResultMetadataType type, Object value) {
		if (resultMetadata == null) {
			resultMetadata = new Hashtable(3);
		}
		resultMetadata.put(type, value);
	}

	public void putAllMetadata(Hashtable metadata) {
		if (metadata != null) {
			if (resultMetadata == null) {
				resultMetadata = metadata;
			} else {
				Enumeration e = metadata.keys();
				while (e.hasMoreElements()) {
					ResultMetadataType key = (ResultMetadataType) e
							.nextElement();
					Object value = metadata.get(key);
					resultMetadata.put(key, value);
				}
			}
		}
	}

	public void addResultPoints(ResultPoint[] newPoints) {
		if (resultPoints == null) {
			resultPoints = newPoints;
		} else if (newPoints != null && newPoints.length > 0) {
			ResultPoint[] allPoints = new ResultPoint[resultPoints.length
					+ newPoints.length];
			System.arraycopy(resultPoints, 0, allPoints, 0, resultPoints.length);
			System.arraycopy(newPoints, 0, allPoints, resultPoints.length,
					newPoints.length);
			resultPoints = allPoints;
		}
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String toString() {
		if (text == null) {
			return "[" + rawBytes.length + " bytes]";
		} else {
			return text;
		}
	}

}
