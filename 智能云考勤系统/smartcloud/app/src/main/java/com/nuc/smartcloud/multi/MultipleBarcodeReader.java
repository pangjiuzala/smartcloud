

package com.nuc.smartcloud.multi;

import com.nuc.smartcloud.BinaryBitmap;
import com.nuc.smartcloud.NotFoundException;
import com.nuc.smartcloud.Result;

import java.util.Hashtable;


public interface MultipleBarcodeReader {

  Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException;

  Result[] decodeMultiple(BinaryBitmap image, Hashtable hints) throws NotFoundException;

}
