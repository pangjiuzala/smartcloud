

package com.nuc.smartcloud;

public abstract class LuminanceSource {

  private final int width;
  private final int height;

  protected LuminanceSource(int width, int height) {
    this.width = width;
    this.height = height;
  }

  
  public abstract byte[] getRow(int y, byte[] row);

   public abstract byte[] getMatrix();

 
  public final int getWidth() {
    return width;
  }

  
  public final int getHeight() {
    return height;
  }

  
  public boolean isCropSupported() {
    return false;
  }

 
  public LuminanceSource crop(int left, int top, int width, int height) {
    throw new RuntimeException("This luminance source does not support cropping.");
  }

  /**
   * @return Whether this subclass supports counter-clockwise rotation.
   */
  public boolean isRotateSupported() {
    return false;
  }

  /**
   * Returns a new object with rotated image data. Only callable if isRotateSupported() is true.
   *
   * @return A rotated version of this object.
   */
  public LuminanceSource rotateCounterClockwise() {
    throw new RuntimeException("This luminance source does not support rotation.");
  }

}
