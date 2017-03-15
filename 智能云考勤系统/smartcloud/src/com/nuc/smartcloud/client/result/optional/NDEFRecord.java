

package com.nuc.smartcloud.client.result.optional;


final class NDEFRecord {

  private static final int SUPPORTED_HEADER_MASK = 0x3F; // 0 0 1 1 1 111 (the bottom 6 bits matter)
  private static final int SUPPORTED_HEADER = 0x11;      // 0 0 0 1 0 001

  public static final String TEXT_WELL_KNOWN_TYPE = "T";
  public static final String URI_WELL_KNOWN_TYPE = "U";
  public static final String SMART_POSTER_WELL_KNOWN_TYPE = "Sp";
  public static final String ACTION_WELL_KNOWN_TYPE = "act";

  private final int header;
  private final String type;
  private final byte[] payload;
  private final int totalRecordLength;

  private NDEFRecord(int header, String type, byte[] payload, int totalRecordLength) {
    this.header = header;
    this.type = type;
    this.payload = payload;
    this.totalRecordLength = totalRecordLength;
  }

  static NDEFRecord readRecord(byte[] bytes, int offset) {
    int header = bytes[offset] & 0xFF;
   
    if (((header ^ SUPPORTED_HEADER) & SUPPORTED_HEADER_MASK) != 0) {
      return null;
    }
    int typeLength = bytes[offset + 1] & 0xFF;

    int payloadLength = bytes[offset + 2] & 0xFF;

    String type = AbstractNDEFResultParser.bytesToString(bytes, offset + 3, typeLength, "US-ASCII");

    byte[] payload = new byte[payloadLength];
    System.arraycopy(bytes, offset + 3 + typeLength, payload, 0, payloadLength);

    return new NDEFRecord(header, type, payload, 3 + typeLength + payloadLength);
  }

  boolean isMessageBegin() {
    return (header & 0x80) != 0;
  }

  boolean isMessageEnd() {
    return (header & 0x40) != 0;
  }

  String getType() {
    return type;
  }

  byte[] getPayload() {
    return payload;
  }

  int getTotalRecordLength() {
    return totalRecordLength;
  }

}