

package com.nuc.smartcloud;


import com.nuc.smartcloud.common.BitMatrix;
import com.nuc.smartcloud.qrcode.QRCodeWriter;

import java.util.Hashtable;


public final class MultiFormatWriter implements Writer {

  public BitMatrix encode(String contents, BarcodeFormat format, int width,
      int height) throws WriterException {

    return encode(contents, format, width, height, null);
  }

  public BitMatrix encode(String contents, BarcodeFormat format, int width, int height,
      Hashtable hints) throws WriterException {

    Writer writer;
    if (format == BarcodeFormat.QR_CODE) {
      writer = new QRCodeWriter();
  
    } else {
      throw new IllegalArgumentException("No encoder available for format " + format);
    }
    return writer.encode(contents, format, width, height, hints);
  }

}
