
package com.nuc.smartcloud.client.android.result;

import com.nuc.smartcloud.Result;
import com.nuc.smartcloud.client.result.ParsedResult;
import com.nuc.smartcloud.client.result.ParsedResultType;
import com.nuc.smartcloud.client.result.ResultParser;

import android.app.Activity;


public final class ResultHandlerFactory {
  private ResultHandlerFactory() {
  }

  public static ResultHandler makeResultHandler(Activity activity, Result rawResult) {
    ParsedResult result = parseResult(rawResult);
    ParsedResultType type = result.getType();

   
     if (type.equals(ParsedResultType.TEXT)) {
      return new TextResultHandler(activity, result);
   
    } else {
      // The TextResultHandler is the fallthrough for unsupported formats.
      return new TextResultHandler(activity, result);
    }
  }

  private static ParsedResult parseResult(Result rawResult) {
    return ResultParser.parseResult(rawResult);
  }
}
