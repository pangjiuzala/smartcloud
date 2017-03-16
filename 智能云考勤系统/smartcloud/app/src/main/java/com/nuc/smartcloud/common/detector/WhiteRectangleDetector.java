

package com.nuc.smartcloud.common.detector;

import com.nuc.smartcloud.NotFoundException;
import com.nuc.smartcloud.ResultPoint;
import com.nuc.smartcloud.common.BitMatrix;


public final class WhiteRectangleDetector {

  private static final int INIT_SIZE = 40;
  private static final int CORR = 1;

  private final BitMatrix image;
  private final int height;
  private final int width;

  public WhiteRectangleDetector(BitMatrix image) {
    this.image = image;
    height = image.getHeight();
    width = image.getWidth();
  }

 
  public ResultPoint[] detect() throws NotFoundException {

    int left = (width - INIT_SIZE) >> 1;
    int right = (width + INIT_SIZE) >> 1;
    int up = (height - INIT_SIZE) >> 1;
    int down = (height + INIT_SIZE) >> 1;
    boolean sizeExceeded = false;
    boolean aBlackPointFoundOnBorder = true;
    boolean atLeastOneBlackPointFoundOnBorder = false;

    while (aBlackPointFoundOnBorder) {

      aBlackPointFoundOnBorder = false;

      // .....
      // .   |
      // .....
      boolean rightBorderNotWhite = true;
      while (rightBorderNotWhite && right < width) {
        rightBorderNotWhite = containsBlackPoint(up, down, right, false);
        if (rightBorderNotWhite) {
          right++;
          aBlackPointFoundOnBorder = true;
        }
      }

      if (right >= width) {
        sizeExceeded = true;
        break;
      }

      // .....
      // .   .
      // .___.
      boolean bottomBorderNotWhite = true;
      while (bottomBorderNotWhite && down < height) {
        bottomBorderNotWhite = containsBlackPoint(left, right, down, true);
        if (bottomBorderNotWhite) {
          down++;
          aBlackPointFoundOnBorder = true;
        }
      }

      if (down >= height) {
        sizeExceeded = true;
        break;
      }

      // .....
      // |   .
      // .....
      boolean leftBorderNotWhite = true;
      while (leftBorderNotWhite && left >= 0) {
        leftBorderNotWhite = containsBlackPoint(up, down, left, false);
        if (leftBorderNotWhite) {
          left--;
          aBlackPointFoundOnBorder = true;
        }
      }

      if (left < 0) {
        sizeExceeded = true;
        break;
      }

      // .___.
      // .   .
      // .....
      boolean topBorderNotWhite = true;
      while (topBorderNotWhite && up >= 0) {
        topBorderNotWhite = containsBlackPoint(left, right, up, true);
        if (topBorderNotWhite) {
          up--;
          aBlackPointFoundOnBorder = true;
        }
      }

      if (up < 0) {
        sizeExceeded = true;
        break;
      }

      if (aBlackPointFoundOnBorder) {
        atLeastOneBlackPointFoundOnBorder = true;
      }

    }

    if (!sizeExceeded && atLeastOneBlackPointFoundOnBorder) {

      int maxSize = right - left;

      ResultPoint z = null;
      for (int i = 1; i < maxSize; i++) {
        z = getBlackPointOnSegment(left, down - i, left + i, down);
        if (z != null) {
          break;
        }
      }

      if (z == null) {
        throw NotFoundException.getNotFoundInstance();
      }

      ResultPoint t = null;
      //go down right
      for (int i = 1; i < maxSize; i++) {
        t = getBlackPointOnSegment(left, up + i, left + i, up);
        if (t != null) {
          break;
        }
      }

      if (t == null) {
        throw NotFoundException.getNotFoundInstance();
      }

      ResultPoint x = null;
      //go down left
      for (int i = 1; i < maxSize; i++) {
        x = getBlackPointOnSegment(right, up + i, right - i, up);
        if (x != null) {
          break;
        }
      }

      if (x == null) {
        throw NotFoundException.getNotFoundInstance();
      }

      ResultPoint y = null;
      //go up left
      for (int i = 1; i < maxSize; i++) {
        y = getBlackPointOnSegment(right, down - i, right - i, down);
        if (y != null) {
          break;
        }
      }

      if (y == null) {
        throw NotFoundException.getNotFoundInstance();
      }

      return centerEdges(y, z, x, t);

    } else {
      throw NotFoundException.getNotFoundInstance();
    }
  }

  /**
   * Ends up being a bit faster than Math.round(). This merely rounds its
   * argument to the nearest int, where x.5 rounds up.
   */
  private static int round(float d) {
    return (int) (d + 0.5f);
  }

  private ResultPoint getBlackPointOnSegment(float aX, float aY, float bX, float bY) {
    int dist = distanceL2(aX, aY, bX, bY);
    float xStep = (bX - aX) / dist;
    float yStep = (bY - aY) / dist;

    for (int i = 0; i < dist; i++) {
      int x = round(aX + i * xStep);
      int y = round(aY + i * yStep);
      if (image.get(x, y)) {
        return new ResultPoint(x, y);
      }
    }
    return null;
  }

  private static int distanceL2(float aX, float aY, float bX, float bY) {
    float xDiff = aX - bX;
    float yDiff = aY - bY;
    return round((float) Math.sqrt(xDiff * xDiff + yDiff * yDiff));
  }

  /**
   * recenters the points of a constant distance towards the center
   *
   * @param y bottom most point
   * @param z left most point
   * @param x right most point
   * @param t top most point
   * @return {@link ResultPoint}[] describing the corners of the rectangular
   *         region. The first and last points are opposed on the diagonal, as
   *         are the second and third. The first point will be the topmost
   *         point and the last, the bottommost. The second point will be
   *         leftmost and the third, the rightmost
   */
  private ResultPoint[] centerEdges(ResultPoint y, ResultPoint z,
                                    ResultPoint x, ResultPoint t) {

    //
    //       t            t
    //  z                      x
    //        x    OR    z
    //   y                    y
    //

    float yi = y.getX();
    float yj = y.getY();
    float zi = z.getX();
    float zj = z.getY();
    float xi = x.getX();
    float xj = x.getY();
    float ti = t.getX();
    float tj = t.getY();

    if (yi < width / 2) {
      return new ResultPoint[]{
          new ResultPoint(ti - CORR, tj + CORR),
          new ResultPoint(zi + CORR, zj + CORR),
          new ResultPoint(xi - CORR, xj - CORR),
          new ResultPoint(yi + CORR, yj - CORR)};
    } else {
      return new ResultPoint[]{
          new ResultPoint(ti + CORR, tj + CORR),
          new ResultPoint(zi + CORR, zj - CORR),
          new ResultPoint(xi - CORR, xj + CORR),
          new ResultPoint(yi - CORR, yj - CORR)};
    }
  }

 
  private boolean containsBlackPoint(int a, int b, int fixed, boolean horizontal) {

    if (horizontal) {
      for (int x = a; x <= b; x++) {
        if (image.get(x, fixed)) {
          return true;
        }
      }
    } else {
      for (int y = a; y <= b; y++) {
        if (image.get(fixed, y)) {
          return true;
        }
      }
    }

    return false;
  }

}