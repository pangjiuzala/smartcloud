
package com.nuc.smartcloud.common;

import java.util.Hashtable;

import com.nuc.smartcloud.DecodeHintType;


public final class StringUtils {

  private static final String PLATFORM_DEFAULT_ENCODING =
      System.getProperty("file.encoding");
  public static final String SHIFT_JIS = "SJIS";
  private static final String EUC_JP = "EUC_JP";
  private static final String UTF8 = "UTF8";
  private static final String ISO88591 = "ISO8859_1";
  private static final boolean ASSUME_SHIFT_JIS =
      SHIFT_JIS.equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING) ||
      EUC_JP.equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING);

  private StringUtils() {}


  public static String guessEncoding(byte[] bytes, Hashtable hints) {
    if (hints != null) {
      String characterSet = (String) hints.get(DecodeHintType.CHARACTER_SET);
      if (characterSet != null) {
        return characterSet;
      }
    }
    // Does it start with the UTF-8 byte order mark? then guess it's UTF-8
    if (bytes.length > 3 &&
        bytes[0] == (byte) 0xEF &&
        bytes[1] == (byte) 0xBB &&
        bytes[2] == (byte) 0xBF) {
      return UTF8;
    }
 
    int length = bytes.length;
    boolean canBeISO88591 = true;
    boolean canBeShiftJIS = true;
    boolean canBeUTF8 = true;
    int utf8BytesLeft = 0;
    int maybeDoubleByteCount = 0;
    int maybeSingleByteKatakanaCount = 0;
    boolean sawLatin1Supplement = false;
    boolean sawUTF8Start = false;
    boolean lastWasPossibleDoubleByteStart = false;

    for (int i = 0;
         i < length && (canBeISO88591 || canBeShiftJIS || canBeUTF8);
         i++) {

      int value = bytes[i] & 0xFF;

 
      if (value >= 0x80 && value <= 0xBF) {
        if (utf8BytesLeft > 0) {
          utf8BytesLeft--;
        }
      } else {
        if (utf8BytesLeft > 0) {
          canBeUTF8 = false;
        }
        if (value >= 0xC0 && value <= 0xFD) {
          sawUTF8Start = true;
          int valueCopy = value;
          while ((valueCopy & 0x40) != 0) {
            utf8BytesLeft++;
            valueCopy <<= 1;
          }
        }
      }


      if ((value == 0xC2 || value == 0xC3) && i < length - 1) {
      
      
        int nextValue = bytes[i + 1] & 0xFF;
        if (nextValue <= 0xBF &&
            ((value == 0xC2 && nextValue >= 0xA0) || (value == 0xC3 && nextValue >= 0x80))) {
          sawLatin1Supplement = true;
        }
      }
      if (value >= 0x7F && value <= 0x9F) {
        canBeISO88591 = false;
      }

      if (value >= 0xA1 && value <= 0xDF) {
       
        if (!lastWasPossibleDoubleByteStart) {
          maybeSingleByteKatakanaCount++;
        }
      }
      if (!lastWasPossibleDoubleByteStart &&
          ((value >= 0xF0 && value <= 0xFF) || value == 0x80 || value == 0xA0)) {
        canBeShiftJIS = false;
      }
      if (((value >= 0x81 && value <= 0x9F) || (value >= 0xE0 && value <= 0xEF))) {
      
        if (lastWasPossibleDoubleByteStart) {
         
          lastWasPossibleDoubleByteStart = false;
        } else {
         
          lastWasPossibleDoubleByteStart = true;
          if (i >= bytes.length - 1) {
            canBeShiftJIS = false;
          } else {
            int nextValue = bytes[i + 1] & 0xFF;
            if (nextValue < 0x40 || nextValue > 0xFC) {
              canBeShiftJIS = false;
            } else {
              maybeDoubleByteCount++;
            }
          
          }
        }
      } else {
        lastWasPossibleDoubleByteStart = false;
      }
    }
    if (utf8BytesLeft > 0) {
      canBeUTF8 = false;
    }

    
    if (canBeShiftJIS && ASSUME_SHIFT_JIS) {
      return SHIFT_JIS;
    }
    if (canBeUTF8 && sawUTF8Start) {
      return UTF8;
    }
   
    if (canBeShiftJIS && (maybeDoubleByteCount >= 3 || 20 * maybeSingleByteKatakanaCount > length)) {
      return SHIFT_JIS;
    } // Otherwise, we default to ISO-8859-1 unless we know it can't be
    if (!sawLatin1Supplement && canBeISO88591) {
      return ISO88591;
    }

    return PLATFORM_DEFAULT_ENCODING;
  }

}
