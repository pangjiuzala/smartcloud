
package com.nuc.smartcloud.client.result;

import com.nuc.smartcloud.Result;

import java.util.Hashtable;
import java.util.Vector;


public abstract class ResultParser {

  public static ParsedResult parseResult(Result theResult) {
   
    ParsedResult result;
    if ((result = VEventResultParser.parse(theResult)) != null) {
      return result;
    
    
    
    } else if ((result = ExpandedProductResultParser.parse(theResult)) != null) {
      return result;
    }
    return new TextParsedResult(theResult.getText(), null);
  }

  protected static void maybeAppend(String value, StringBuffer result) {
    if (value != null) {
      result.append('\n');
      result.append(value);
    }
  }

  protected static void maybeAppend(String[] value, StringBuffer result) {
    if (value != null) {
      for (int i = 0; i < value.length; i++) {
        result.append('\n');
        result.append(value[i]);
      }
    }
  }

  protected static String[] maybeWrap(String value) {
    return value == null ? null : new String[] { value };
  }

  protected static String unescapeBackslash(String escaped) {
    if (escaped != null) {
      int backslash = escaped.indexOf((int) '\\');
      if (backslash >= 0) {
        int max = escaped.length();
        StringBuffer unescaped = new StringBuffer(max - 1);
        unescaped.append(escaped.toCharArray(), 0, backslash);
        boolean nextIsEscaped = false;
        for (int i = backslash; i < max; i++) {
          char c = escaped.charAt(i);
          if (nextIsEscaped || c != '\\') {
            unescaped.append(c);
            nextIsEscaped = false;
          } else {
            nextIsEscaped = true;
          }
        }
        return unescaped.toString();
      }
    }
    return escaped;
  }

  private static String urlDecode(String escaped) {

    // No we can't use java.net.URLDecoder here. JavaME doesn't have it.
    if (escaped == null) {
      return null;
    }
    char[] escapedArray = escaped.toCharArray();

    int first = findFirstEscape(escapedArray);
    if (first < 0) {
      return escaped;
    }

    int max = escapedArray.length;
    // final length is at most 2 less than original due to at least 1 unescaping
    StringBuffer unescaped = new StringBuffer(max - 2);
    // Can append everything up to first escape character
    unescaped.append(escapedArray, 0, first);

    for (int i = first; i < max; i++) {
      char c = escapedArray[i];
      if (c == '+') {
        // + is translated directly into a space
        unescaped.append(' ');
      } else if (c == '%') {
        // Are there even two more chars? if not we will just copy the escaped sequence and be done
        if (i >= max - 2) {
          unescaped.append('%'); // append that % and move on
        } else {
          int firstDigitValue = parseHexDigit(escapedArray[++i]);
          int secondDigitValue = parseHexDigit(escapedArray[++i]);
          if (firstDigitValue < 0 || secondDigitValue < 0) {
            // bad digit, just move on
            unescaped.append('%');
            unescaped.append(escapedArray[i-1]);
            unescaped.append(escapedArray[i]);
          }
          unescaped.append((char) ((firstDigitValue << 4) + secondDigitValue));
        }
      } else {
        unescaped.append(c);
      }
    }
    return unescaped.toString();
  }

  private static int findFirstEscape(char[] escapedArray) {
    int max = escapedArray.length;
    for (int i = 0; i < max; i++) {
      char c = escapedArray[i];
      if (c == '+' || c == '%') {
        return i;
      }
    }
    return -1;
  }

  private static int parseHexDigit(char c) {
    if (c >= 'a') {
      if (c <= 'f') {
        return 10 + (c - 'a');
      }
    } else if (c >= 'A') {
      if (c <= 'F') {
        return 10 + (c - 'A');
      }
    } else if (c >= '0') {
      if (c <= '9') {
        return c - '0';
      }
    }
    return -1;
  }

  protected static boolean isStringOfDigits(String value, int length) {
    if (value == null) {
      return false;
    }
    int stringLength = value.length();
    if (length != stringLength) {
      return false;
    }
    for (int i = 0; i < length; i++) {
      char c = value.charAt(i);
      if (c < '0' || c > '9') {
        return false;
      }
    }
    return true;
  }

  protected static boolean isSubstringOfDigits(String value, int offset, int length) {
    if (value == null) {
      return false;
    }
    int stringLength = value.length();
    int max = offset + length;
    if (stringLength < max) {
      return false;
    }
    for (int i = offset; i < max; i++) {
      char c = value.charAt(i);
      if (c < '0' || c > '9') {
        return false;
      }
    }
    return true;
  }

  static Hashtable parseNameValuePairs(String uri) {
    int paramStart = uri.indexOf('?');
    if (paramStart < 0) {
      return null;
    }
    Hashtable result = new Hashtable(3);
    paramStart++;
    int paramEnd;
    while ((paramEnd = uri.indexOf('&', paramStart)) >= 0) {
      appendKeyValue(uri, paramStart, paramEnd, result);
      paramStart = paramEnd + 1;
    }
    appendKeyValue(uri, paramStart, uri.length(), result);
    return result;
  }

  private static void appendKeyValue(String uri, int paramStart, int paramEnd, Hashtable result) {
    int separator = uri.indexOf('=', paramStart);
    if (separator >= 0) {
      // key = value
      String key = uri.substring(paramStart, separator);
      String value = uri.substring(separator + 1, paramEnd);
      value = urlDecode(value);
      result.put(key, value);
    }
    // Can't put key, null into a hashtable
  }

  static String[] matchPrefixedField(String prefix, String rawText, char endChar, boolean trim) {
    Vector matches = null;
    int i = 0;
    int max = rawText.length();
    while (i < max) {
      i = rawText.indexOf(prefix, i);
      if (i < 0) {
        break;
      }
      i += prefix.length(); // Skip past this prefix we found to start
      int start = i; // Found the start of a match here
      boolean done = false;
      while (!done) {
        i = rawText.indexOf((int) endChar, i);
        if (i < 0) {
          // No terminating end character? uh, done. Set i such that loop terminates and break
          i = rawText.length();
          done = true;
        } else if (rawText.charAt(i - 1) == '\\') {
          // semicolon was escaped so continue
          i++;
        } else {
          // found a match
          if (matches == null) {
            matches = new Vector(3); // lazy init
          }
          String element = unescapeBackslash(rawText.substring(start, i));
          if (trim) {
            element = element.trim();
          }
          matches.addElement(element);
          i++;
          done = true;
        }
      }
    }
    if (matches == null || matches.isEmpty()) {
      return null;
    }
    return toStringArray(matches);
  }

  static String matchSinglePrefixedField(String prefix, String rawText, char endChar, boolean trim) {
    String[] matches = matchPrefixedField(prefix, rawText, endChar, trim);
    return matches == null ? null : matches[0];
  }

  static String[] toStringArray(Vector strings) {
    int size = strings.size();
    String[] result = new String[size];
    for (int j = 0; j < size; j++) {
      result[j] = (String) strings.elementAt(j);
    }
    return result;
  }

}
