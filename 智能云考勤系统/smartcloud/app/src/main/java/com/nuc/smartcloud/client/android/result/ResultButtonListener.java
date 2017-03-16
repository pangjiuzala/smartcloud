

package com.nuc.smartcloud.client.android.result;

import android.view.View;
import android.widget.Button;


public final class ResultButtonListener implements Button.OnClickListener {
  private final ResultHandler resultHandler;
  private final int index;

  public ResultButtonListener(ResultHandler resultHandler, int index) {
    this.resultHandler = resultHandler;
    this.index = index;
  }

  public void onClick(View view) {
    resultHandler.handleButtonPress(index);
  }
}
