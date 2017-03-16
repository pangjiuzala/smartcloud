

package com.nuc.smartcloud;


import com.nuc.smartcloud.pdf417.PDF417Reader;
import com.nuc.smartcloud.qrcode.QRCodeReader;

import java.util.Hashtable;
import java.util.Vector;


public final class MultiFormatReader implements Reader {

  private Hashtable hints;
  private Vector readers;

  public Result decode(BinaryBitmap image) throws NotFoundException {
    setHints(null);
    return decodeInternal(image);
  }

 
  public Result decode(BinaryBitmap image, Hashtable hints) throws NotFoundException {
    setHints(hints);
    return decodeInternal(image);
  }

 
  public Result decodeWithState(BinaryBitmap image) throws NotFoundException {
    // Make sure to set up the default state so we don't crash
    if (readers == null) {
      setHints(null);
    }
    return decodeInternal(image);
  }

  public void setHints(Hashtable hints) {
    this.hints = hints;

    boolean tryHarder = hints != null && hints.containsKey(DecodeHintType.TRY_HARDER);
    Vector formats = hints == null ? null : (Vector) hints.get(DecodeHintType.POSSIBLE_FORMATS);
    readers = new Vector();
    if (formats != null) {
      boolean addOneDReader =
          formats.contains(BarcodeFormat.UPC_A) ||
              formats.contains(BarcodeFormat.UPC_E) ||
              formats.contains(BarcodeFormat.EAN_13) ||
              formats.contains(BarcodeFormat.EAN_8) ||
              //formats.contains(BarcodeFormat.CODABAR) ||
              formats.contains(BarcodeFormat.CODE_39) ||
              formats.contains(BarcodeFormat.CODE_93) ||
              formats.contains(BarcodeFormat.CODE_128) ||
              formats.contains(BarcodeFormat.ITF) ||
              formats.contains(BarcodeFormat.RSS14) ||
              formats.contains(BarcodeFormat.RSS_EXPANDED);
      // Put 1D readers upfront in "normal" mode
    
      if (formats.contains(BarcodeFormat.QR_CODE)) {
        readers.addElement(new QRCodeReader());
      }
      
      if (formats.contains(BarcodeFormat.PDF417)) {
         readers.addElement(new PDF417Reader());
       }
    
      
    }
  }
    
   

  



  public void reset() {
    int size = readers.size();
    for (int i = 0; i < size; i++) {
      Reader reader = (Reader) readers.elementAt(i);
      reader.reset();
    }
  }

  private Result decodeInternal(BinaryBitmap image) throws NotFoundException {
    int size = readers.size();
    for (int i = 0; i < size; i++) {
      Reader reader = (Reader) readers.elementAt(i);
      try {
        return reader.decode(image, hints);
      } catch (ReaderException re) {
  
      }
    }

    throw NotFoundException.getNotFoundInstance();
  }

}
