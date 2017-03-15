

package com.nuc.smartcloud;


public final class EncodeHintType {

  /**
   * Specifies what degree of error correction to use, for example in QR Codes (type Integer).
   */
  public static final EncodeHintType ERROR_CORRECTION = new EncodeHintType();

  /**
   * Specifies what character encoding to use where applicable (type String)
   */
  public static final EncodeHintType CHARACTER_SET = new EncodeHintType();

  private EncodeHintType() {
  }

}
