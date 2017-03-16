
package com.nuc.smartcloud.multi.qrcode;

import com.nuc.smartcloud.BarcodeFormat;
import com.nuc.smartcloud.BinaryBitmap;
import com.nuc.smartcloud.NotFoundException;
import com.nuc.smartcloud.ReaderException;
import com.nuc.smartcloud.Result;
import com.nuc.smartcloud.ResultMetadataType;
import com.nuc.smartcloud.ResultPoint;
import com.nuc.smartcloud.common.DecoderResult;
import com.nuc.smartcloud.common.DetectorResult;
import com.nuc.smartcloud.multi.MultipleBarcodeReader;
import com.nuc.smartcloud.multi.qrcode.detector.MultiDetector;
import com.nuc.smartcloud.qrcode.QRCodeReader;

import java.util.Hashtable;
import java.util.Vector;

public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader {

  private static final Result[] EMPTY_RESULT_ARRAY = new Result[0];

  public Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException {
    return decodeMultiple(image, null);
  }

  public Result[] decodeMultiple(BinaryBitmap image, Hashtable hints) throws NotFoundException {
    Vector results = new Vector();
    DetectorResult[] detectorResult = new MultiDetector(image.getBlackMatrix()).detectMulti(hints);
    for (int i = 0; i < detectorResult.length; i++) {
      try {
        DecoderResult decoderResult = getDecoder().decode(detectorResult[i].getBits());
        ResultPoint[] points = detectorResult[i].getPoints();
        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
            BarcodeFormat.QR_CODE);
        if (decoderResult.getByteSegments() != null) {
          result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, decoderResult.getByteSegments());
        }
        if (decoderResult.getECLevel() != null) {
          result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, decoderResult.getECLevel().toString());
        }
        results.addElement(result);
      } catch (ReaderException re) {
        // ignore and continue 
      }
    }
    if (results.isEmpty()) {
      return EMPTY_RESULT_ARRAY;
    } else {
      Result[] resultArray = new Result[results.size()];
      for (int i = 0; i < results.size(); i++) {
        resultArray[i] = (Result) results.elementAt(i);
      }
      return resultArray;
    }
  }

}
