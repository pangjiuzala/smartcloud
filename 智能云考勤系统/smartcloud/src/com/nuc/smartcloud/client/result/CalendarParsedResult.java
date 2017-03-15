
package com.nuc.smartcloud.client.result;


public final class CalendarParsedResult extends ParsedResult {

  private final String summary;
  private final String start;
  private final String end;
  private final String location;
  private final String attendee;
  private final String description;

  public CalendarParsedResult(String summary,
                              String start,
                              String end,
                              String location,
                              String attendee,
                              String description) {
    super(ParsedResultType.CALENDAR);

    if (start == null) {
      throw new IllegalArgumentException();
    }
    validateDate(start);
    if (end == null) {
      end = start;
    } else {
      validateDate(end);
    }
    this.summary = summary;
    this.start = start;
    this.end = end;
    this.location = location;
    this.attendee = attendee;
    this.description = description;
  }

  public String getSummary() {
    return summary;
  }

  public String getStart() {
    return start;
  }

 
  public String getEnd() {
    return end;
  }

  public String getLocation() {
    return location;
  }

  public String getAttendee() {
    return attendee;
  }

  public String getDescription() {
    return description;
  }

  public String getDisplayResult() {
    StringBuffer result = new StringBuffer(100);
    maybeAppend(summary, result);
    maybeAppend(start, result);
    maybeAppend(end, result);
    maybeAppend(location, result);
    maybeAppend(attendee, result);
    maybeAppend(description, result);
    return result.toString();
  }


  private static void validateDate(String date) {
    if (date != null) {
      int length = date.length();
      if (length != 8 && length != 15 && length != 16) {
        throw new IllegalArgumentException();
      }
      for (int i = 0; i < 8; i++) {
        if (!Character.isDigit(date.charAt(i))) {
          throw new IllegalArgumentException();
        }
      }
      if (length > 8) {
        if (date.charAt(8) != 'T') {
          throw new IllegalArgumentException();
        }
        for (int i = 9; i < 15; i++) {
          if (!Character.isDigit(date.charAt(i))) {
            throw new IllegalArgumentException();
          }
        }
        if (length == 16 && date.charAt(15) != 'Z') {
          throw new IllegalArgumentException();
        }
      }
    }
  }

}
