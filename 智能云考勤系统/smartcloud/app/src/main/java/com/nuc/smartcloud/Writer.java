
package com.nuc.smartcloud;

import com.nuc.smartcloud.common.BitMatrix;

import java.util.Hashtable;


public interface Writer {

 
  BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
      throws WriterException;

 
  BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Hashtable hints)
      throws WriterException;

}
