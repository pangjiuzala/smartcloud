
package com.nuc.smartcloud.client.result.optional;

import com.nuc.smartcloud.client.result.ResultParser;

import java.io.UnsupportedEncodingException;


abstract class AbstractNDEFResultParser extends ResultParser {

  static String bytesToString(byte[] bytes, int offset, int length, String encoding) {
    try {
      return new String(bytes, offset, length, encoding);
    } catch (UnsupportedEncodingException uee) {
    
      throw new RuntimeException("Platform does not support required encoding: " + uee);
    }
  }

}