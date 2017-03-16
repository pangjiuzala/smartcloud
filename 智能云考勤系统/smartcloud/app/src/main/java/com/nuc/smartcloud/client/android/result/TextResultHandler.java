
package com.nuc.smartcloud.client.android.result;

import com.nuc.smartcloud.client.result.ParsedResult;
import com.nuc.smartcloud.R;

import android.app.Activity;
import android.widget.Button;


public final class TextResultHandler extends ResultHandler {

  private static final int[] buttons = {
   
  };

  public TextResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
  }

  @Override
  public int getButtonCount() {
    return hasCustomProductSearch() ? buttons.length : buttons.length - 1;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override

  public void handleButtonPress(int index) {
    String text = getResult().getDisplayResult();
    switch (index) {
      case 0:
        webSearch(text);
        openURL(fillInCustomSearchURL(text));
        break;

    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_text;
  }
}
