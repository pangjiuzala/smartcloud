
package com.nuc.smartcloud.qrcode.decoder;


public final class ErrorCorrectionLevel {

 


  public static final ErrorCorrectionLevel L = new ErrorCorrectionLevel(0, 0x01, "L");
  /**
   * M = ~15% correction
   */
  public static final ErrorCorrectionLevel M = new ErrorCorrectionLevel(1, 0x00, "M");
  /**
   * Q = ~25% correction
   */
  public static final ErrorCorrectionLevel Q = new ErrorCorrectionLevel(2, 0x03, "Q");
  /**
   * H = ~30% correction
   */
  public static final ErrorCorrectionLevel H = new ErrorCorrectionLevel(3, 0x02, "H");

  private static final ErrorCorrectionLevel[] FOR_BITS = {M, L, H, Q};

  private final int ordinal;
  private final int bits;
  private final String name;

  private ErrorCorrectionLevel(int ordinal, int bits, String name) {
    this.ordinal = ordinal;
    this.bits = bits;
    this.name = name;
  }

  public int ordinal() {
    return ordinal;
  }

  public int getBits() {
    return bits;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }


  public static ErrorCorrectionLevel forBits(int bits) {
    if (bits < 0 || bits >= FOR_BITS.length) {
      throw new IllegalArgumentException();
    }
    return FOR_BITS[bits];
  }


}
