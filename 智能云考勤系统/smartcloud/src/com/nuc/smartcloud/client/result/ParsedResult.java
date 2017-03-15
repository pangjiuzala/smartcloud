
package com.nuc.smartcloud.client.result;

import com.nuc.smartcloud.Result;


public abstract class ParsedResult {

  private final ParsedResultType type;

  protected ParsedResult(ParsedResultType type) {
    this.type = type;
  }

  public ParsedResultType getType() {
    return type;
  }

  public abstract String getDisplayResult();

  public String toString() {
    return getDisplayResult();
  }

  public static void maybeAppend(String value, StringBuffer result) {
    if (value != null && value.length() > 0) {
     
      if (result.length() > 0) {
        result.append('\n');
      }
      result.append(value);
    }
  }

  public static void maybeAppend(String[] value, StringBuffer result) {
    if (value != null) {
      for (int i = 0; i < value.length; i++) {
        if (value[i] != null && value[i].length() > 0) {
          if (result.length() > 0) {
            result.append('\n');
          }
          result.append(value[i]);
        }
      }
    }
  }

}
