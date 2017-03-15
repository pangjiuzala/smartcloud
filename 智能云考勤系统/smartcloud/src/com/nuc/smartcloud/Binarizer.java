

package com.nuc.smartcloud;

import com.nuc.smartcloud.common.BitArray;
import com.nuc.smartcloud.common.BitMatrix;


public abstract class Binarizer {

  private final LuminanceSource source;

  protected Binarizer(LuminanceSource source) {
    if (source == null) {
      throw new IllegalArgumentException("Source must be non-null.");
    }
    this.source = source;
  }

  public LuminanceSource getLuminanceSource() {
    return source;
  }

  
  public abstract BitArray getBlackRow(int y, BitArray row) throws NotFoundException;

 
  public abstract BitMatrix getBlackMatrix() throws NotFoundException;

  public abstract Binarizer createBinarizer(LuminanceSource source);

}
