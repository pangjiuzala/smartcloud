

package com.nuc.smartcloud;

public final class FormatException extends ReaderException {

  private static final FormatException instance = new FormatException();

  private FormatException() {
    // do nothing
  }

  public static FormatException getFormatInstance() {
    return instance;
  }

}