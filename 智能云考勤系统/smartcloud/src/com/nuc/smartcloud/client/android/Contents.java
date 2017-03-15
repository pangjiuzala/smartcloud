
package com.nuc.smartcloud.client.android;

import android.provider.Contacts;

public final class Contents {
  private Contents() {
  }

  public static final class Type {
 
    public static final String TEXT = "TEXT_TYPE";

   
    public static final String EMAIL = "EMAIL_TYPE";

  
    public static final String PHONE = "PHONE_TYPE";

   
    public static final String SMS = "SMS_TYPE";

  
    public static final String CONTACT = "CONTACT_TYPE";

    
    public static final String LOCATION = "LOCATION_TYPE";

    private Type() {
    }
  }

 
  public static final String[] PHONE_KEYS = {
      Contacts.Intents.Insert.PHONE,
      Contacts.Intents.Insert.SECONDARY_PHONE,
      Contacts.Intents.Insert.TERTIARY_PHONE
  };

  public static final String[] EMAIL_KEYS = {
      Contacts.Intents.Insert.EMAIL,
      Contacts.Intents.Insert.SECONDARY_EMAIL,
      Contacts.Intents.Insert.TERTIARY_EMAIL
  };
}
