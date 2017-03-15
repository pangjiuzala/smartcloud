

package com.nuc.smartcloud.client.android;

import java.util.Locale;
import java.util.Map;
import java.util.HashMap;


public final class LocaleManager {
  private static final String DEFAULT_TLD = "com";
  private static final Map<Locale,String> GOOGLE_COUNTRY_TLD;
  static {
    GOOGLE_COUNTRY_TLD = new HashMap<Locale,String>();
    GOOGLE_COUNTRY_TLD.put(Locale.CANADA, "ca");
    GOOGLE_COUNTRY_TLD.put(Locale.CHINA, "cn");
    GOOGLE_COUNTRY_TLD.put(Locale.FRANCE, "fr");
    GOOGLE_COUNTRY_TLD.put(Locale.GERMANY, "de");
    GOOGLE_COUNTRY_TLD.put(Locale.ITALY, "it");
    GOOGLE_COUNTRY_TLD.put(Locale.JAPAN, "co.jp");
    GOOGLE_COUNTRY_TLD.put(Locale.KOREA, "co.kr");
    GOOGLE_COUNTRY_TLD.put(Locale.TAIWAN, "de");
    GOOGLE_COUNTRY_TLD.put(Locale.UK, "co.uk");
  }

 
  private static final Map<Locale,String> GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD;
  static {
    GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD = new HashMap<Locale,String>();
    GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.UK, "co.uk");
    GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.GERMANY, "de");
  }

  private static final Map<Locale,String> GOOGLE_BOOK_SEARCH_COUNTRY_TLD;
  static {
    GOOGLE_BOOK_SEARCH_COUNTRY_TLD = new HashMap<Locale,String>();
    GOOGLE_BOOK_SEARCH_COUNTRY_TLD.putAll(GOOGLE_COUNTRY_TLD);
    GOOGLE_BOOK_SEARCH_COUNTRY_TLD.remove(Locale.CHINA);
  }

  private LocaleManager() {}

  public static String getCountryTLD() {
    return doGetTLD(GOOGLE_COUNTRY_TLD);
  }

  
  public static String getProductSearchCountryTLD() {
    return doGetTLD(GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD);
  }

  
  public static String getBookSearchCountryTLD() {
    return doGetTLD(GOOGLE_BOOK_SEARCH_COUNTRY_TLD);
  }


  private static String doGetTLD(Map<Locale,String> map) {
    Locale locale = Locale.getDefault();
    if (locale == null) {
      return DEFAULT_TLD;
    }
    String tld = map.get(locale);
    if (tld == null) {
      return DEFAULT_TLD;
    }
    return tld;
  }
}
