

package com.nuc.smartcloud.pdf417;

import com.nuc.smartcloud.BarcodeFormat;
import com.nuc.smartcloud.BinaryBitmap;
import com.nuc.smartcloud.DecodeHintType;
import com.nuc.smartcloud.FormatException;
import com.nuc.smartcloud.NotFoundException;
import com.nuc.smartcloud.Reader;
import com.nuc.smartcloud.Result;
import com.nuc.smartcloud.ResultPoint;
import com.nuc.smartcloud.common.BitMatrix;
import com.nuc.smartcloud.common.DecoderResult;
import com.nuc.smartcloud.common.DetectorResult;
import com.nuc.smartcloud.pdf417.decoder.Decoder;
import com.nuc.smartcloud.pdf417.detector.Detector;
import com.nuc.smartcloud.qrcode.QRCodeReader;

import java.util.Hashtable;


public final class PDF417Reader implements Reader {

  private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

  private final Decoder decoder = new Decoder();

  public Result decode(BinaryBitmap image) throws NotFoundException, FormatException {
    return decode(image, null);
  }

  public Result decode(BinaryBitmap image, Hashtable hints)
      throws NotFoundException, FormatException {
    DecoderResult decoderResult;
    ResultPoint[] points;
    if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
      BitMatrix bits = QRCodeReader.extractPureBits(image.getBlackMatrix());
      decoderResult = decoder.decode(bits);
      points = NO_POINTS;
    } else {
      DetectorResult detectorResult = new Detector(image).detect();
      decoderResult = decoder.decode(detectorResult.getBits());
      points = detectorResult.getPoints();
    }
    return new Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
        BarcodeFormat.PDF417);
  }

  public void reset() {
    // do nothing
  }

}
