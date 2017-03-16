
package com.nuc.smartcloud.qrcode.decoder;

import com.nuc.smartcloud.common.BitMatrix;


abstract class DataMask {

  /**
   * See ISO 18004:2006 6.8.1
   */
  private static final DataMask[] DATA_MASKS = {
      new DataMask000(),
      new DataMask001(),
      new DataMask010(),
      new DataMask011(),
      new DataMask100(),
      new DataMask101(),
      new DataMask110(),
      new DataMask111(),
  };

  private DataMask() {
  }

 
  final void unmaskBitMatrix(BitMatrix bits, int dimension) {
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (isMasked(i, j)) {
          bits.flip(j, i);
        }
      }
    }
  }

  abstract boolean isMasked(int i, int j);


  static DataMask forReference(int reference) {
    if (reference < 0 || reference > 7) {
      throw new IllegalArgumentException();
    }
    return DATA_MASKS[reference];
  }


  private static class DataMask000 extends DataMask {
    boolean isMasked(int i, int j) {
      return ((i + j) & 0x01) == 0;
    }
  }


  private static class DataMask001 extends DataMask {
    boolean isMasked(int i, int j) {
      return (i & 0x01) == 0;
    }
  }

  
  private static class DataMask010 extends DataMask {
    boolean isMasked(int i, int j) {
      return j % 3 == 0;
    }
  }

  
  private static class DataMask011 extends DataMask {
    boolean isMasked(int i, int j) {
      return (i + j) % 3 == 0;
    }
  }

 
  private static class DataMask100 extends DataMask {
    boolean isMasked(int i, int j) {
      return (((i >>> 1) + (j /3)) & 0x01) == 0;
    }
  }

 
  private static class DataMask101 extends DataMask {
    boolean isMasked(int i, int j) {
      int temp = i * j;
      return (temp & 0x01) + (temp % 3) == 0;
    }
  }

  
  private static class DataMask110 extends DataMask {
    boolean isMasked(int i, int j) {
      int temp = i * j;
      return (((temp & 0x01) + (temp % 3)) & 0x01) == 0;
    }
  }

 
  private static class DataMask111 extends DataMask {
    boolean isMasked(int i, int j) {
      return ((((i + j) & 0x01) + ((i * j) % 3)) & 0x01) == 0;
    }
  }
}
