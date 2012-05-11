package com.jneander.truecost.data;

import android.content.Context;

import com.jneander.truecost.R;

public class MessageBuilder {
  /** Builds and return the Results message. */
  public static String buildResultsMessage( Context caller, double truecost,
      double price, double interest, int duration,
      Calculator.Method method ) {
    // Initialize the message String
    String messageBody = "";

    /* INTEREST MESSAGE */
    messageBody +=
        caller.getString( R.string.results_message_body_interest );
    messageBody = messageBody.replace( "#", getCurrencyString( interest ) );

    // Determine if the interest is high enough to merit mention
    String interestMessage = "";
    if ( interest >= (price * 0.8) ) {
      if ( interest < price ) { // interest is small
        interestMessage = caller.getString(
            R.string.results_message_body_highinterest_low );
      } else if ( interest == price ) { // interest equals price
        interestMessage = caller.getString(
            R.string.results_message_body_highinterest_equal );
      } else if ( interest < (price * 1.3) ) { // interest is greater
        interestMessage = caller.getString(
            R.string.results_message_body_highinterest_mid );
      } else { // interest is very high
        interestMessage = caller.getString(
            R.string.results_message_body_highinterest_high );
        String multiple = String.valueOf( (int) (truecost / price) );
        multiple += ((truecost / price) % 1) >= 0.5 ? " 1/2" : "";
        // multiple = colorString( multiple, "red");
        interestMessage = interestMessage.replace( "#", multiple );
      }
      messageBody += " " + interestMessage;
    }

    messageBody += "<br><br>"; // paragraph break

    /* CASH MESSAGE */
    if ( method == Calculator.Method.CASH ) { // paying with cash
      String cashMessage =
          caller.getString( R.string.results_message_body_cash )
              .replace( "#", getCurrencyString( price ) );
      messageBody += cashMessage;
    } else { // paying with credit
      messageBody +=
          caller.getString( R.string.results_message_body_credit );
    }

    /* DURATION MESSAGE */
    if ( duration > 0 ) { // time can be saved during repayment
      messageBody += "<br><br>" + caller.getString(
          R.string.results_message_body_duration )
          .replace( "#", String.valueOf( duration ) )
          .replace( "MONTH", (duration > 1) ? "months" : "month" );
    }

    return messageBody; // return the completed String
  }

  /* Convert Double values representing currency into an accurate String */
  public static String getCurrencyString( Double value ) {
    return java.text.DecimalFormat.getCurrencyInstance().format( value );
  }

  public static String colorString( String string, String color ) {
    return "<font color='" + color + "'>" + string + "</font>";
  }

  public static String underlineString( String string ) {
    return "<u>" + string + "</u>";
  }
}