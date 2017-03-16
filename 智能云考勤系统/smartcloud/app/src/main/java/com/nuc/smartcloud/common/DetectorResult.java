

package com.nuc.smartcloud.common;

import com.nuc.smartcloud.ResultPoint;


public final class DetectorResult {

  private final BitMatrix bits;
  private final ResultPoint[] points;

  public DetectorResult(BitMatrix bits, ResultPoint[] points) {
    this.bits = bits;
    this.points = points;
  }

  public BitMatrix getBits() {
    return bits;
  }

  public ResultPoint[] getPoints() {
    return points;
  }

}