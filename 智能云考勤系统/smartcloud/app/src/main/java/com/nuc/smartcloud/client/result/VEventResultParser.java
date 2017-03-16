

package com.nuc.smartcloud.client.result;

import com.nuc.smartcloud.Result;


final class VEventResultParser extends ResultParser {

  private VEventResultParser() {
  }

  public static CalendarParsedResult parse(Result result) {
    String rawText = result.getText();
    if (rawText == null) {
      return null;
    }
    int vEventStart = rawText.indexOf("BEGIN:VEVENT");
    if (vEventStart < 0) {
      return null;
    }

    String summary = VCardResultParser.matchSingleVCardPrefixedField("SUMMARY", rawText, true);
    String start = VCardResultParser.matchSingleVCardPrefixedField("DTSTART", rawText, true);
    String end = VCardResultParser.matchSingleVCardPrefixedField("DTEND", rawText, true);
    String location = VCardResultParser.matchSingleVCardPrefixedField("LOCATION", rawText, true);
    String description = VCardResultParser.matchSingleVCardPrefixedField("DESCRIPTION", rawText, true);
    try {
      return new CalendarParsedResult(summary, start, end, location, null, description);
    } catch (IllegalArgumentException iae) {
      return null;
    }
  }

}