package com.jneander.truecost.util;

import com.jneander.truecost.R;
import android.content.Context;
import android.widget.Toast;

public class Alert {
  public enum AlertType {
    NOTHING_ENTERED,
    SMALL_PAYMENT,
  }

  public static void alertUser( Context context, AlertType type ) {
    int resource = R.string.alert_generic;

    switch ( type ) {
    case NOTHING_ENTERED:
      resource = R.string.alert_nothing_entered;
      break;
    case SMALL_PAYMENT:
      resource = R.string.alert_small_payment;
      break;
    }

    Toast.makeText( context, context.getString( resource ), Toast.LENGTH_LONG ).show();
  }
}