

package com.nuc.smartcloud.qrcode.detector;

import com.nuc.smartcloud.ResultPoint;


public final class AlignmentPattern extends ResultPoint {

  private final float estimatedModuleSize;

  AlignmentPattern(float posX, float posY, float estimatedModuleSize) {
    super(posX, posY);
    this.estimatedModuleSize = estimatedModuleSize;
  }

 
  boolean aboutEquals(float moduleSize, float i, float j) {
    if (Math.abs(i - getY()) <= moduleSize && Math.abs(j - getX()) <= moduleSize) {
      float moduleSizeDiff = Math.abs(moduleSize - estimatedModuleSize);
      return moduleSizeDiff <= 1.0f || moduleSizeDiff / estimatedModuleSize <= 1.0f;
    }
    return false;
  }

}