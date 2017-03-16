
package com.nuc.smartcloud.qrcode;

import com.nuc.smartcloud.BarcodeFormat;
import com.nuc.smartcloud.EncodeHintType;
import com.nuc.smartcloud.Writer;
import com.nuc.smartcloud.WriterException;
import com.nuc.smartcloud.common.BitMatrix;
import com.nuc.smartcloud.qrcode.decoder.ErrorCorrectionLevel;
import com.nuc.smartcloud.qrcode.encoder.ByteMatrix;
import com.nuc.smartcloud.qrcode.encoder.Encoder;
import com.nuc.smartcloud.qrcode.encoder.QRCode;

import java.util.Hashtable;

public final class QRCodeWriter implements Writer {

  private static final int QUIET_ZONE_SIZE = 4;

  public BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
      throws WriterException {

    return encode(contents, format, width, height, null);
  }

  public BitMatrix encode(String contents, BarcodeFormat format, int width, int height,
      Hashtable hints) throws WriterException {

    if (contents == null || contents.length() == 0) {
      throw new IllegalArgumentException("Found empty contents");
    }

    if (format != BarcodeFormat.QR_CODE) {
      throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
    }

    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' +
          height);
    }

    ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
    if (hints != null) {
      ErrorCorrectionLevel requestedECLevel = (ErrorCorrectionLevel) hints.get(EncodeHintType.ERROR_CORRECTION);
      if (requestedECLevel != null) {
        errorCorrectionLevel = requestedECLevel;
      }
    }

    QRCode code = new QRCode();
    Encoder.encode(contents, errorCorrectionLevel, hints, code);
    return renderResult(code, width, height);
  }

  // Note that the input matrix uses 0 == white, 1 == black, while the output matrix uses
  // 0 == black, 255 == white (i.e. an 8 bit greyscale bitmap).
  private static BitMatrix renderResult(QRCode code, int width, int height) {
    ByteMatrix input = code.getMatrix();
    int inputWidth = input.getWidth();
    int inputHeight = input.getHeight();
    int qrWidth = inputWidth + (QUIET_ZONE_SIZE << 1);
    int qrHeight = inputHeight + (QUIET_ZONE_SIZE << 1);
    int outputWidth = Math.max(width, qrWidth);
    int outputHeight = Math.max(height, qrHeight);

    int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
    // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
    // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
    // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
    // handle all the padding from 100x100 (the actual QR) up to 200x160.
    int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
    int topPadding = (outputHeight - (inputHeight * multiple)) / 2;

    BitMatrix output = new BitMatrix(outputWidth, outputHeight);

    for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
      // Write the contents of this row of the barcode
      for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
        if (input.get(inputX, inputY) == 1) {
          output.setRegion(outputX, outputY, multiple, multiple);
        }
      }
    }

    return output;
  }

}
