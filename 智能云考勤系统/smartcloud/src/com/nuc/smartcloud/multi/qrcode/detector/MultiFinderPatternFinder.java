
package com.nuc.smartcloud.multi.qrcode.detector;

import com.nuc.smartcloud.DecodeHintType;
import com.nuc.smartcloud.NotFoundException;
import com.nuc.smartcloud.ResultPoint;
import com.nuc.smartcloud.ResultPointCallback;
import com.nuc.smartcloud.common.BitMatrix;
import com.nuc.smartcloud.common.Collections;
import com.nuc.smartcloud.common.Comparator;
import com.nuc.smartcloud.qrcode.detector.FinderPattern;
import com.nuc.smartcloud.qrcode.detector.FinderPatternFinder;
import com.nuc.smartcloud.qrcode.detector.FinderPatternInfo;

import java.util.Hashtable;
import java.util.Vector;


final class MultiFinderPatternFinder extends FinderPatternFinder {

  private static final FinderPatternInfo[] EMPTY_RESULT_ARRAY = new FinderPatternInfo[0];

  private static final float MAX_MODULE_COUNT_PER_EDGE = 180;
  // min. legal count per modules per QR code edge (11)
  private static final float MIN_MODULE_COUNT_PER_EDGE = 9;

 
  private static final float DIFF_MODSIZE_CUTOFF_PERCENT = 0.05f;

 
  private static final float DIFF_MODSIZE_CUTOFF = 0.5f;


  
  private static class ModuleSizeComparator implements Comparator {
    public int compare(Object center1, Object center2) {
      float value = ((FinderPattern) center2).getEstimatedModuleSize() -
                    ((FinderPattern) center1).getEstimatedModuleSize();
      return value < 0.0 ? -1 : value > 0.0 ? 1 : 0;
    }
  }

 
  MultiFinderPatternFinder(BitMatrix image) {
    super(image);
  }

  MultiFinderPatternFinder(BitMatrix image, ResultPointCallback resultPointCallback) {
    super(image, resultPointCallback);
  }

  
  private FinderPattern[][] selectBestPatterns() throws NotFoundException {
    Vector possibleCenters = getPossibleCenters();
    int size = possibleCenters.size();

    if (size < 3) {
      // Couldn't find enough finder patterns
      throw NotFoundException.getNotFoundInstance();
    }

    /*
     * Begin HE modifications to safely detect multiple codes of equal size
     */
    if (size == 3) {
      return new FinderPattern[][]{
          new FinderPattern[]{
              (FinderPattern) possibleCenters.elementAt(0),
              (FinderPattern) possibleCenters.elementAt(1),
              (FinderPattern) possibleCenters.elementAt(2)
          }
      };
    }

    
    Collections.insertionSort(possibleCenters, new ModuleSizeComparator());

   

    Vector results = new Vector(); // holder for the results

    for (int i1 = 0; i1 < (size - 2); i1++) {
      FinderPattern p1 = (FinderPattern) possibleCenters.elementAt(i1);
      if (p1 == null) {
        continue;
      }

      for (int i2 = i1 + 1; i2 < (size - 1); i2++) {
        FinderPattern p2 = (FinderPattern) possibleCenters.elementAt(i2);
        if (p2 == null) {
          continue;
        }

        // Compare the expected module sizes; if they are really off, skip
        float vModSize12 = (p1.getEstimatedModuleSize() - p2.getEstimatedModuleSize()) /
            (Math.min(p1.getEstimatedModuleSize(), p2.getEstimatedModuleSize()));
        float vModSize12A = Math.abs(p1.getEstimatedModuleSize() - p2.getEstimatedModuleSize());
        if (vModSize12A > DIFF_MODSIZE_CUTOFF && vModSize12 >= DIFF_MODSIZE_CUTOFF_PERCENT) {
          // break, since elements are ordered by the module size deviation there cannot be
          // any more interesting elements for the given p1.
          break;
        }

        for (int i3 = i2 + 1; i3 < size; i3++) {
          FinderPattern p3 = (FinderPattern) possibleCenters.elementAt(i3);
          if (p3 == null) {
            continue;
          }

          // Compare the expected module sizes; if they are really off, skip
          float vModSize23 = (p2.getEstimatedModuleSize() - p3.getEstimatedModuleSize()) /
              (Math.min(p2.getEstimatedModuleSize(), p3.getEstimatedModuleSize()));
          float vModSize23A = Math.abs(p2.getEstimatedModuleSize() - p3.getEstimatedModuleSize());
          if (vModSize23A > DIFF_MODSIZE_CUTOFF && vModSize23 >= DIFF_MODSIZE_CUTOFF_PERCENT) {
            // break, since elements are ordered by the module size deviation there cannot be
            // any more interesting elements for the given p1.
            break;
          }

          FinderPattern[] test = {p1, p2, p3};
          ResultPoint.orderBestPatterns(test);

          // Calculate the distances: a = topleft-bottomleft, b=topleft-topright, c = diagonal
          FinderPatternInfo info = new FinderPatternInfo(test);
          float dA = ResultPoint.distance(info.getTopLeft(), info.getBottomLeft());
          float dC = ResultPoint.distance(info.getTopRight(), info.getBottomLeft());
          float dB = ResultPoint.distance(info.getTopLeft(), info.getTopRight());

          // Check the sizes
          float estimatedModuleCount = ((dA + dB) / p1.getEstimatedModuleSize()) / 2;
          if (estimatedModuleCount > MAX_MODULE_COUNT_PER_EDGE ||
              estimatedModuleCount < MIN_MODULE_COUNT_PER_EDGE) {
            continue;
          }

          // Calculate the difference of the edge lengths in percent
          float vABBC = Math.abs(((dA - dB) / Math.min(dA, dB)));
          if (vABBC >= 0.1f) {
            continue;
          }

          // Calculate the diagonal length by assuming a 90° angle at topleft
          float dCpy = (float) Math.sqrt(dA * dA + dB * dB);
          // Compare to the real distance in %
          float vPyC = Math.abs(((dC - dCpy) / Math.min(dC, dCpy)));

          if (vPyC >= 0.1f) {
            continue;
          }

          // All tests passed!
          results.addElement(test);
        } // end iterate p3
      } // end iterate p2
    } // end iterate p1

    if (!results.isEmpty()) {
      FinderPattern[][] resultArray = new FinderPattern[results.size()][];
      for (int i = 0; i < results.size(); i++) {
        resultArray[i] = (FinderPattern[]) results.elementAt(i);
      }
      return resultArray;
    }

    // Nothing found!
    throw NotFoundException.getNotFoundInstance();
  }

  public FinderPatternInfo[] findMulti(Hashtable hints) throws NotFoundException {
    boolean tryHarder = hints != null && hints.containsKey(DecodeHintType.TRY_HARDER);
    BitMatrix image = getImage();
    int maxI = image.getHeight();
    int maxJ = image.getWidth();
    // We are looking for black/white/black/white/black modules in
    // 1:1:3:1:1 ratio; this tracks the number of such modules seen so far

    // Let's assume that the maximum version QR Code we support takes up 1/4 the height of the
    // image, and then account for the center being 3 modules in size. This gives the smallest
    // number of pixels the center could be, so skip this often. When trying harder, look for all
    // QR versions regardless of how dense they are.
    int iSkip = (int) (maxI / (MAX_MODULES * 4.0f) * 3);
    if (iSkip < MIN_SKIP || tryHarder) {
      iSkip = MIN_SKIP;
    }

    int[] stateCount = new int[5];
    for (int i = iSkip - 1; i < maxI; i += iSkip) {
      // Get a row of black/white values
      stateCount[0] = 0;
      stateCount[1] = 0;
      stateCount[2] = 0;
      stateCount[3] = 0;
      stateCount[4] = 0;
      int currentState = 0;
      for (int j = 0; j < maxJ; j++) {
        if (image.get(j, i)) {
          // Black pixel
          if ((currentState & 1) == 1) { // Counting white pixels
            currentState++;
          }
          stateCount[currentState]++;
        } else { // White pixel
          if ((currentState & 1) == 0) { // Counting black pixels
            if (currentState == 4) { // A winner?
              if (foundPatternCross(stateCount)) { // Yes
                boolean confirmed = handlePossibleCenter(stateCount, i, j);
                if (!confirmed) {
                  do { // Advance to next black pixel
                    j++;
                  } while (j < maxJ && !image.get(j, i));
                  j--; // back up to that last white pixel
                }
                // Clear state to start looking again
                currentState = 0;
                stateCount[0] = 0;
                stateCount[1] = 0;
                stateCount[2] = 0;
                stateCount[3] = 0;
                stateCount[4] = 0;
              } else { // No, shift counts back by two
                stateCount[0] = stateCount[2];
                stateCount[1] = stateCount[3];
                stateCount[2] = stateCount[4];
                stateCount[3] = 1;
                stateCount[4] = 0;
                currentState = 3;
              }
            } else {
              stateCount[++currentState]++;
            }
          } else { // Counting white pixels
            stateCount[currentState]++;
          }
        }
      } // for j=...

      if (foundPatternCross(stateCount)) {
        handlePossibleCenter(stateCount, i, maxJ);
      } // end if foundPatternCross
    } // for i=iSkip-1 ...
    FinderPattern[][] patternInfo = selectBestPatterns();
    Vector result = new Vector();
    for (int i = 0; i < patternInfo.length; i++) {
      FinderPattern[] pattern = patternInfo[i];
      ResultPoint.orderBestPatterns(pattern);
      result.addElement(new FinderPatternInfo(pattern));
    }

    if (result.isEmpty()) {
      return EMPTY_RESULT_ARRAY;
    } else {
      FinderPatternInfo[] resultArray = new FinderPatternInfo[result.size()];
      for (int i = 0; i < result.size(); i++) {
        resultArray[i] = (FinderPatternInfo) result.elementAt(i);
      }
      return resultArray;
    }
  }

}
