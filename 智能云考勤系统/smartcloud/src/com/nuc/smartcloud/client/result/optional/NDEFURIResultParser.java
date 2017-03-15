

package com.nuc.smartcloud.client.result.optional;

import com.nuc.smartcloud.Result;



final class NDEFURIResultParser extends AbstractNDEFResultParser {

  private static final String[] URI_PREFIXES = {
      null,
      "http://www.",
      "https://www.",
      "http://",
      "https://",
      "tel:",
      "mailto:",
      "ftp://anonymous:anonymous@",
      "ftp://ftp.",
      "ftps://",
      "sftp://",
      "smb://",
      "nfs://",
      "ftp://",
      "dav://",
      "news:",
      "telnet://",
      "imap:",
      "rtsp://",
      "urn:",
      "pop:",
      "sip:",
      "sips:",
      "tftp:",
      "btspp://",
      "btl2cap://",
      "btgoep://",
      "tcpobex://",
      "irdaobex://",
      "file://",
      "urn:epc:id:",
      "urn:epc:tag:",
      "urn:epc:pat:",
      "urn:epc:raw:",
      "urn:epc:",
      "urn:nfc:",
  };

  static String decodeURIPayload(byte[] payload) {
    int identifierCode = payload[0] & 0xFF;
    String prefix = null;
    if (identifierCode < URI_PREFIXES.length) {
      prefix = URI_PREFIXES[identifierCode];
    }
    String restOfURI = bytesToString(payload, 1, payload.length - 1, "UTF8");
    return prefix == null ? restOfURI : prefix + restOfURI;
  }

}