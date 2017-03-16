
package com.nuc.smartcloud;

import java.util.Hashtable;


public interface Reader {


  Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException;

 
  Result decode(BinaryBitmap image, Hashtable hints) throws NotFoundException, ChecksumException, FormatException;

  void reset();

}