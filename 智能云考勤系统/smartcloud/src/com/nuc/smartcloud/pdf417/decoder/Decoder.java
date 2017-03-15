
package com.nuc.smartcloud.pdf417.decoder;


import com.nuc.smartcloud.ChecksumException;
import com.nuc.smartcloud.FormatException;
import com.nuc.smartcloud.NotFoundException;
import com.nuc.smartcloud.common.BitMatrix;
import com.nuc.smartcloud.common.DecoderResult;


public final class Decoder {

  private static final int MAX_ERRORS = 3;
  private static final int MAX_EC_CODEWORDS = 512;
  //private final ReedSolomonDecoder rsDecoder;

  public Decoder() {
    // TODO MGMG
    //rsDecoder = new ReedSolomonDecoder();
  }

 
  public DecoderResult decode(boolean[][] image) throws FormatException {
    int dimension = image.length;
    BitMatrix bits = new BitMatrix(dimension);
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (image[j][i]) {
          bits.set(j, i);
        }
      }
    }
    return decode(bits);
  }


  public DecoderResult decode(BitMatrix bits) throws FormatException {
    // Construct a parser to read the data codewords and error-correction level
    BitMatrixParser parser = new BitMatrixParser(bits);
    int[] codewords = parser.readCodewords();
    if (codewords == null || codewords.length == 0) {
      throw FormatException.getFormatInstance();
    }

    int ecLevel = parser.getECLevel();
    int numECCodewords = 1 << (ecLevel + 1);
    int[] erasures = parser.getErasures();

    correctErrors(codewords, erasures, numECCodewords);
    verifyCodewordCount(codewords, numECCodewords);

    // Decode the codewords
    return DecodedBitStreamParser.decode(codewords);
  }

  
  private static void verifyCodewordCount(int[] codewords, int numECCodewords) throws FormatException {
    if (codewords.length < 4) {
      // Codeword array size should be at least 4 allowing for
      // Count CW, At least one Data CW, Error Correction CW, Error Correction CW
      throw FormatException.getFormatInstance();
    }
    // The first codeword, the Symbol Length Descriptor, shall always encode the total number of data
    // codewords in the symbol, including the Symbol Length Descriptor itself, data codewords and pad
    // codewords, but excluding the number of error correction codewords.
    int numberOfCodewords = codewords[0];
    if (numberOfCodewords > codewords.length) {
      throw FormatException.getFormatInstance();
    }
    if (numberOfCodewords == 0) {
      // Reset to the length of the array - 8 (Allow for at least level 3 Error Correction (8 Error Codewords)
      if (numECCodewords < codewords.length) {
        codewords[0] = codewords.length - numECCodewords;
      } else {
        throw FormatException.getFormatInstance();
      }
    }
  }

 
  private static int correctErrors(int[] codewords, int[] erasures, int numECCodewords) throws FormatException {
    if ((erasures != null && erasures.length > numECCodewords / 2 + MAX_ERRORS) ||
        (numECCodewords < 0 || numECCodewords > MAX_EC_CODEWORDS)) {
      // Too many errors or EC Codewords is corrupted
      throw FormatException.getFormatInstance();
    }
    // Try to correct the errors
    // TODO enable error correction
    int result = 0; // rsDecoder.correctErrors(codewords, numECCodewords);
    if (erasures != null) {
      int numErasures = erasures.length;
      if (result > 0) {
        numErasures -= result;
      }
      if (numErasures > MAX_ERRORS) {
        // Still too many errors
        throw FormatException.getFormatInstance();
      }
    }
    return result;
  }

}
