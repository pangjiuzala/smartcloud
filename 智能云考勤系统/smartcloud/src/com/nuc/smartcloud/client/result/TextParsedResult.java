

package com.nuc.smartcloud.client.result;


public final class TextParsedResult extends ParsedResult {

  private final String text;
  private final String language;

  public TextParsedResult(String text, String language) {
    super(ParsedResultType.TEXT);
    this.text = text;
    this.language = language;
  }

  public String getText() {
    return text;
  }

  public String getLanguage() {
    return language;
  }

  public String getDisplayResult() {
    return text;
  }

}
