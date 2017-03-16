

package com.nuc.smartcloud.common;

import com.nuc.smartcloud.NotFoundException;

public abstract class GridSampler {

  private static GridSampler gridSampler = new DefaultGridSampler();

  public static void setGridSampler(GridSampler newGridSampler) {
    if (newGridSampler == null) {
      throw new IllegalArgumentException();
    }
    gridSampler = newGridSampler;
  }


  public static GridSampler getInstance() {
    return gridSampler;
  }

  
  public abstract BitMatrix sampleGrid(BitMatrix image,
                                       int dimension,
                                       float p1ToX, float p1ToY,
                                       float p2ToX, float p2ToY,
                                       float p3ToX, float p3ToY,
                                       float p4ToX, float p4ToY,
                                       float p1FromX, float p1FromY,
                                       float p2FromX, float p2FromY,
                                       float p3FromX, float p3FromY,
                                       float p4FromX, float p4FromY) throws NotFoundException;

  public BitMatrix sampleGrid(BitMatrix image,
                              int dimension,
                              PerspectiveTransform transform) throws NotFoundException {
    throw new IllegalStateException(); // Can't use UnsupportedOperationException
  }
  

  
  protected static void checkAndNudgePoints(BitMatrix image, float[] points)
      throws NotFoundException {
    int width = image.getWidth();
    int height = image.getHeight();
    // Check and nudge points from start until we see some that are OK:
    boolean nudged = true;
    for (int offset = 0; offset < points.length && nudged; offset += 2) {
      int x = (int) points[offset];
      int y = (int) points[offset + 1];
      if (x < -1 || x > width || y < -1 || y > height) {
        throw NotFoundException.getNotFoundInstance();
      }
      nudged = false;
      if (x == -1) {
        points[offset] = 0.0f;
        nudged = true;
      } else if (x == width) {
        points[offset] = width - 1;
        nudged = true;
      }
      if (y == -1) {
        points[offset + 1] = 0.0f;
        nudged = true;
      } else if (y == height) {
        points[offset + 1] = height - 1;
        nudged = true;
      }
    }
    // Check and nudge points from end:
    nudged = true;
    for (int offset = points.length - 2; offset >= 0 && nudged; offset -= 2) {
      int x = (int) points[offset];
      int y = (int) points[offset + 1];
      if (x < -1 || x > width || y < -1 || y > height) {
        throw NotFoundException.getNotFoundInstance();
      }
      nudged = false;
      if (x == -1) {
        points[offset] = 0.0f;
        nudged = true;
      } else if (x == width) {
        points[offset] = width - 1;
        nudged = true;
      }
      if (y == -1) {
        points[offset + 1] = 0.0f;
        nudged = true;
      } else if (y == height) {
        points[offset + 1] = height - 1;
        nudged = true;
      }
    }
  }

}
