

package com.nuc.smartcloud.qrcode.decoder;

import com.nuc.smartcloud.ChecksumException;
import com.nuc.smartcloud.FormatException;
import com.nuc.smartcloud.NotFoundException;
import com.nuc.smartcloud.common.BitMatrix;
import com.nuc.smartcloud.common.DecoderResult;
import com.nuc.smartcloud.common.reedsolomon.GF256;
import com.nuc.smartcloud.common.reedsolomon.ReedSolomonDecoder;
import com.nuc.smartcloud.common.reedsolomon.ReedSolomonException;

import java.util.Hashtable;


public final class Decoder {

  private final ReedSolomonDecoder rsDecoder;

  public Decoder() {
    rsDecoder = new ReedSolomonDecoder(GF256.QR_CODE_FIELD);
  }

  public DecoderResult decode(boolean[][] image)
      throws ChecksumException, FormatException, NotFoundException {
    return decode(image, null);
  }

 
  public DecoderResult decode(boolean[][] image, Hashtable hints)
      throws ChecksumException, FormatException, NotFoundException {
    int dimension = image.length;
    BitMatrix bits = new BitMatrix(dimension);
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (image[i][j]) {
          bits.set(j, i);
        }
      }
    }
    return decode(bits, hints);
  }

  public DecoderResult decode(BitMatrix bits) throws ChecksumException, FormatException, NotFoundException {
    return decode(bits, null);
  }

 
  public DecoderResult decode(BitMatrix bits, Hashtable hints) throws FormatException, ChecksumException {

    // Construct a parser and read version, error-correction level
    BitMatrixParser parser = new BitMatrixParser(bits);
    Version version = parser.readVersion();
    ErrorCorrectionLevel ecLevel = parser.readFormatInformation().getErrorCorrectionLevel();

    // Read codewords
    byte[] codewords = parser.readCodewords();
    // Separate into data blocks
    DataBlock[] dataBlocks = DataBlock.getDataBlocks(codewords, version, ecLevel);

    // Count total number of data bytes
    int totalBytes = 0;
    for (int i = 0; i < dataBlocks.length; i++) {
      totalBytes += dataBlocks[i].getNumDataCodewords();
    }
    byte[] resultBytes = new byte[totalBytes];
    int resultOffset = 0;

    // Error-correct and copy data blocks together into a stream of bytes
    for (int j = 0; j < dataBlocks.length; j++) {
      DataBlock dataBlock = dataBlocks[j];
      byte[] codewordBytes = dataBlock.getCodewords();
      int numDataCodewords = dataBlock.getNumDataCodewords();
      correctErrors(codewordBytes, numDataCodewords);
      for (int i = 0; i < numDataCodewords; i++) {
        resultBytes[resultOffset++] = codewordBytes[i];
      }
    }

    // Decode the contents of that stream of bytes
    return DecodedBitStreamParser.decode(resultBytes, version, ecLevel, hints);
  }

  
  private void correctErrors(byte[] codewordBytes, int numDataCodewords) throws ChecksumException {
    int numCodewords = codewordBytes.length;
    // First read into an array of ints
    int[] codewordsInts = new int[numCodewords];
    for (int i = 0; i < numCodewords; i++) {
      codewordsInts[i] = codewordBytes[i] & 0xFF;
    }
    int numECCodewords = codewordBytes.length - numDataCodewords;
    try {
      rsDecoder.decode(codewordsInts, numECCodewords);
    } catch (ReedSolomonException rse) {
      throw ChecksumException.getChecksumInstance();
    }
   
    for (int i = 0; i < numDataCodewords; i++) {
      codewordBytes[i] = (byte) codewordsInts[i];
    }
  }

}
