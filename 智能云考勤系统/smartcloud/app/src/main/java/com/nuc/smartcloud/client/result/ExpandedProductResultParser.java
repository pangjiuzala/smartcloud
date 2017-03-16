

package com.nuc.smartcloud.client.result;

import java.util.Hashtable;

import com.nuc.smartcloud.BarcodeFormat;
import com.nuc.smartcloud.Result;

final class ExpandedProductResultParser extends ResultParser {

  private ExpandedProductResultParser() {
  }

 
  public static ExpandedProductParsedResult parse(Result result) {
    BarcodeFormat format = result.getBarcodeFormat();
    if (!(BarcodeFormat.RSS_EXPANDED.equals(format))) {
     
      return null;
    }
   
    String rawText = result.getText();
    if (rawText == null) {
    
      return null;
    }

    String productID = "-";
    String sscc = "-";
    String lotNumber = "-";
    String productionDate = "-";
    String packagingDate = "-";
    String bestBeforeDate = "-";
    String expirationDate = "-";
    String weight = "-";
    String weightType = "-";
    String weightIncrement = "-";
    String price = "-";
    String priceIncrement = "-";
    String priceCurrency = "-";
    Hashtable uncommonAIs = new Hashtable();

    int i = 0;

    while (i < rawText.length()) {
      String ai = findAIvalue(i, rawText);
      if ("ERROR".equals(ai)) {
       
        return null;
      }
      i += ai.length() + 2;
      String value = findValue(i, rawText);
      i += value.length();

      if ("00".equals(ai)) {
        sscc = value;
      } else if ("01".equals(ai)) {
        productID = value;
      } else if ("10".equals(ai)) {
        lotNumber = value;
      } else if ("11".equals(ai)) {
        productionDate = value;
      } else if ("13".equals(ai)) {
        packagingDate = value;
      } else if ("15".equals(ai)) {
        bestBeforeDate = value;
      } else if ("17".equals(ai)) {
        expirationDate = value;
      } else if ("3100".equals(ai) || "3101".equals(ai)
          || "3102".equals(ai) || "3103".equals(ai)
          || "3104".equals(ai) || "3105".equals(ai)
          || "3106".equals(ai) || "3107".equals(ai)
          || "3108".equals(ai) || "3109".equals(ai)) {
        weight = value;
        weightType = ExpandedProductParsedResult.KILOGRAM;
        weightIncrement = ai.substring(3);
      } else if ("3200".equals(ai) || "3201".equals(ai)
          || "3202".equals(ai) || "3203".equals(ai)
          || "3204".equals(ai) || "3205".equals(ai)
          || "3206".equals(ai) || "3207".equals(ai)
          || "3208".equals(ai) || "3209".equals(ai)) {
        weight = value;
        weightType = ExpandedProductParsedResult.POUND;
        weightIncrement = ai.substring(3);
      } else if ("3920".equals(ai) || "3921".equals(ai)
          || "3922".equals(ai) || "3923".equals(ai)) {
        price = value;
        priceIncrement = ai.substring(3);
      } else if ("3930".equals(ai) || "3931".equals(ai)
          || "3932".equals(ai) || "3933".equals(ai)) {
        if (value.length() < 4) {
        
          return null;
        }
        price = value.substring(3);
        priceCurrency = value.substring(0, 3);
        priceIncrement = ai.substring(3);
      } else {
        // No match with common AIs
        uncommonAIs.put(ai, value);
      }
    }

    return new ExpandedProductParsedResult(productID, sscc, lotNumber,
        productionDate, packagingDate, bestBeforeDate, expirationDate,
        weight, weightType, weightIncrement, price, priceIncrement,
        priceCurrency, uncommonAIs);
  }

  private static String findAIvalue(int i, String rawText) {
    StringBuffer buf = new StringBuffer();
    char c = rawText.charAt(i);
    // First character must be a open parenthesis.If not, ERROR
    if (c != '(') {
      return "ERROR";
    }

    String rawTextAux = rawText.substring(i + 1);

    for (int index = 0; index < rawTextAux.length(); index++) {
      char currentChar = rawTextAux.charAt(index);
      switch (currentChar){
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
          buf.append(currentChar);
          break;
        case ')':
          return buf.toString();
        default:
          return "ERROR";
      }
    }
    return buf.toString();
  }

  private static String findValue(int i, String rawText) {
    StringBuffer buf = new StringBuffer();
    String rawTextAux = rawText.substring(i);

    for (int index = 0; index < rawTextAux.length(); index++) {
      char c = rawTextAux.charAt(index);
      if (c == '(') {
        // We look for a new AI. If it doesn't exist (ERROR), we coninue
        // with the iteration
        if ("ERROR".equals(findAIvalue(index, rawTextAux))) {
          buf.append('(');
        } else {
          break;
        }
      } else {
        buf.append(c);
      }
    }
    return buf.toString();
  }
}
