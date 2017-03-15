

package com.nuc.smartcloud.common;


public final class BitSource {

  private final byte[] bytes;
  private int byteOffset;
  private int bitOffset;

 
  public BitSource(byte[] bytes) {
    this.bytes = bytes;
  }

  
  public int readBits(int numBits) {
    if (numBits < 1 || numBits > 32) {
      throw new IllegalArgumentException();
    }

    int result = 0;

    
    if (bitOffset > 0) {
      int bitsLeft = 8 - bitOffset;
      int toRead = numBits < bitsLeft ? numBits : bitsLeft;
      int bitsToNotRead = bitsLeft - toRead;
      int mask = (0xFF >> (8 - toRead)) << bitsToNotRead;
      result = (bytes[byteOffset] & mask) >> bitsToNotRead;
      numBits -= toRead;
      bitOffset += toRead;
      if (bitOffset == 8) {
        bitOffset = 0;
        byteOffset++;
      }
    }

    // Next read whole bytes
    if (numBits > 0) {
      while (numBits >= 8) {
        result = (result << 8) | (bytes[byteOffset] & 0xFF);
        byteOffset++;
        numBits -= 8;
      }

      // Finally read a partial byte
      if (numBits > 0) {
        int bitsToNotRead = 8 - numBits;
        int mask = (0xFF >> bitsToNotRead) << bitsToNotRead;
        result = (result << numBits) | ((bytes[byteOffset] & mask) >> bitsToNotRead);
        bitOffset += numBits;
      }
    }

    return result;
  }


  public int available() {
    return 8 * (bytes.length - byteOffset) - bitOffset;
  }

}
