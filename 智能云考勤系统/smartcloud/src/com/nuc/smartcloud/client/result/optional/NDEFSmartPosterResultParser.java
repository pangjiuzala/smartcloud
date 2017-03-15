

package com.nuc.smartcloud.client.result.optional;

import com.nuc.smartcloud.Result;


final class NDEFSmartPosterResultParser extends AbstractNDEFResultParser {

  public static NDEFSmartPosterParsedResult parse(Result result) {
    byte[] bytes = result.getRawBytes();
    if (bytes == null) {
      return null;
    }
    NDEFRecord headerRecord = NDEFRecord.readRecord(bytes, 0);
    // Yes, header record starts and ends a message
    if (headerRecord == null || !headerRecord.isMessageBegin() || !headerRecord.isMessageEnd()) {
      return null;
    }
    if (!headerRecord.getType().equals(NDEFRecord.SMART_POSTER_WELL_KNOWN_TYPE)) {
      return null;
    }

    int offset = 0;
    int recordNumber = 0;
    NDEFRecord ndefRecord = null;
    byte[] payload = headerRecord.getPayload();
    int action = NDEFSmartPosterParsedResult.ACTION_UNSPECIFIED;
    String title = null;
    String uri = null;

    while (offset < payload.length && (ndefRecord = NDEFRecord.readRecord(payload, offset)) != null) {
      if (recordNumber == 0 && !ndefRecord.isMessageBegin()) {
        return null;
      }

      String type = ndefRecord.getType();
      if (NDEFRecord.TEXT_WELL_KNOWN_TYPE.equals(type)) {
        String[] languageText = NDEFTextResultParser.decodeTextPayload(ndefRecord.getPayload());
        title = languageText[1];
      } else if (NDEFRecord.URI_WELL_KNOWN_TYPE.equals(type)) {
        uri = NDEFURIResultParser.decodeURIPayload(ndefRecord.getPayload());
      } else if (NDEFRecord.ACTION_WELL_KNOWN_TYPE.equals(type)) {
        action = ndefRecord.getPayload()[0];
      }
      recordNumber++;
      offset += ndefRecord.getTotalRecordLength();
    }

    if (recordNumber == 0 || (ndefRecord != null && !ndefRecord.isMessageEnd())) {
      return null;
    }

    return new NDEFSmartPosterParsedResult(action, uri, title);
  }

}