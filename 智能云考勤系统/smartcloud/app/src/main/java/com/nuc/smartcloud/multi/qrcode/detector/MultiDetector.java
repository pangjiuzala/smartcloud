

package com.nuc.smartcloud.multi.qrcode.detector;

import com.nuc.smartcloud.NotFoundException;
import com.nuc.smartcloud.ReaderException;
import com.nuc.smartcloud.common.BitMatrix;
import com.nuc.smartcloud.common.DetectorResult;
import com.nuc.smartcloud.qrcode.detector.Detector;
import com.nuc.smartcloud.qrcode.detector.FinderPatternInfo;

import java.util.Hashtable;
import java.util.Vector;


public final class MultiDetector extends Detector {

  private static final DetectorResult[] EMPTY_DETECTOR_RESULTS = new DetectorResult[0];

  public MultiDetector(BitMatrix image) {
    super(image);
  }

  public DetectorResult[] detectMulti(Hashtable hints) throws NotFoundException {
    BitMatrix image = getImage();
    MultiFinderPatternFinder finder = new MultiFinderPatternFinder(image);
    FinderPatternInfo[] info = finder.findMulti(hints);

    if (info == null || info.length == 0) {
      throw NotFoundException.getNotFoundInstance();
    }

    Vector result = new Vector();
    for (int i = 0; i < info.length; i++) {
      try {
        result.addElement(processFinderPatternInfo(info[i]));
      } catch (ReaderException e) {
        // ignore
      }
    }
    if (result.isEmpty()) {
      return EMPTY_DETECTOR_RESULTS;
    } else {
      DetectorResult[] resultArray = new DetectorResult[result.size()];
      for (int i = 0; i < result.size(); i++) {
        resultArray[i] = (DetectorResult) result.elementAt(i);
      }
      return resultArray;
    }
  }

}
