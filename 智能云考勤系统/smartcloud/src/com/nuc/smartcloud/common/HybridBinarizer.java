

package com.nuc.smartcloud.common;

import com.nuc.smartcloud.Binarizer;
import com.nuc.smartcloud.LuminanceSource;
import com.nuc.smartcloud.NotFoundException;


public final class HybridBinarizer extends GlobalHistogramBinarizer {

 
  private static final int MINIMUM_DIMENSION = 40;

  private BitMatrix matrix = null;

  public HybridBinarizer(LuminanceSource source) {
    super(source);
  }

  public BitMatrix getBlackMatrix() throws NotFoundException {
    binarizeEntireImage();
    return matrix;
  }

  public Binarizer createBinarizer(LuminanceSource source) {
    return new HybridBinarizer(source);
  }

  
  private void binarizeEntireImage() throws NotFoundException {
    if (matrix == null) {
      LuminanceSource source = getLuminanceSource();
      if (source.getWidth() >= MINIMUM_DIMENSION && source.getHeight() >= MINIMUM_DIMENSION) {
        byte[] luminances = source.getMatrix();
        int width = source.getWidth();
        int height = source.getHeight();
        int subWidth = width >> 3;
        if ((width & 0x07) != 0) {
          subWidth++;
        }
        int subHeight = height >> 3;
        if ((height & 0x07) != 0) {
          subHeight++;
        }
        int[][] blackPoints = calculateBlackPoints(luminances, subWidth, subHeight, width, height);

        matrix = new BitMatrix(width, height);
        calculateThresholdForBlock(luminances, subWidth, subHeight, width, height, blackPoints, matrix);
      } else {
        // If the image is too small, fall back to the global histogram approach.
        matrix = super.getBlackMatrix();
      }
    }
  }


  private static void calculateThresholdForBlock(byte[] luminances, int subWidth, int subHeight,
      int width, int height, int[][] blackPoints, BitMatrix matrix) {
    for (int y = 0; y < subHeight; y++) {
      int yoffset = y << 3;
      if ((yoffset + 8) >= height) {
        yoffset = height - 8;
      }
      for (int x = 0; x < subWidth; x++) {
        int xoffset = x << 3;
        if ((xoffset + 8) >= width) {
            xoffset = width - 8;
        }
        int left = (x > 1) ? x : 2;
        left = (left < subWidth - 2) ? left : subWidth - 3;
        int top = (y > 1) ? y : 2;
        top = (top < subHeight - 2) ? top : subHeight - 3;
        int sum = 0;
        for (int z = -2; z <= 2; z++) {
          int[] blackRow = blackPoints[top + z];
          sum += blackRow[left - 2];
          sum += blackRow[left - 1];
          sum += blackRow[left];
          sum += blackRow[left + 1];
          sum += blackRow[left + 2];
        }
        int average = sum / 25;
        threshold8x8Block(luminances, xoffset, yoffset, average, width, matrix);
      }
    }
  }

  // Applies a single threshold to an 8x8 block of pixels.
  private static void threshold8x8Block(byte[] luminances, int xoffset, int yoffset, int threshold,
      int stride, BitMatrix matrix) {
    for (int y = 0; y < 8; y++) {
      int offset = (yoffset + y) * stride + xoffset;
      for (int x = 0; x < 8; x++) {
        int pixel = luminances[offset + x] & 0xff;
        if (pixel < threshold) {
          matrix.set(xoffset + x, yoffset + y);
        }
      }
    }
  }

  // Calculates a single black point for each 8x8 block of pixels and saves it away.
  private static int[][] calculateBlackPoints(byte[] luminances, int subWidth, int subHeight,
      int width, int height) {
    int[][] blackPoints = new int[subHeight][subWidth];
    for (int y = 0; y < subHeight; y++) {
      int yoffset = y << 3;
      if ((yoffset + 8) >= height) {
        yoffset = height - 8;
      }
      for (int x = 0; x < subWidth; x++) {
        int xoffset = x << 3;
        if ((xoffset + 8) >= width) {
            xoffset = width - 8;
        }
        int sum = 0;
        int min = 255;
        int max = 0;
        for (int yy = 0; yy < 8; yy++) {
          int offset = (yoffset + yy) * width + xoffset;
          for (int xx = 0; xx < 8; xx++) {
            int pixel = luminances[offset + xx] & 0xff;
            sum += pixel;
            if (pixel < min) {
              min = pixel;
            }
            if (pixel > max) {
              max = pixel;
            }
          }
        }

    
        int average;
        if (max - min > 24) {
          average = sum >> 6;
        } else {
         
          average = max == 0 ? 1 : min >> 1;
        }
        blackPoints[y][x] = average;
      }
    }
    return blackPoints;
  }

}
