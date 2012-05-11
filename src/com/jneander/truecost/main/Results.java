/* True Cost Mobile : Results.java */
package com.jneander.truecost.main;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.jneander.truecost.R;
import com.jneander.truecost.data.AccountData;
import com.jneander.truecost.data.Calculator;
import com.jneander.truecost.data.MessageBuilder;

public class Results extends Activity {
  // Initialize class storage
  private Calculator.Method method;
  private double price, interest, truecost;
  private int duration;

  TextView message; // destination for Results message

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.results );

    // Fetch 'extras' from Intent
    Bundle extras = getIntent().getExtras();
    method = extras.getInt( "method" ) == 0 ? Calculator.Method.CASH
        : Calculator.Method.CREDIT;
    price = extras.getDouble( "price" );

    // Calculate the results of the query
    Calculator calculator = new Calculator(
        AccountData.getBalance( this ),
        AccountData.getAPR( this ),
        AccountData.getPayment( this ),
        price,
        method
        );
    
    if ( calculator.paymentIsTooSmall())
      this.finish(); // TEMPORARY: must avoid impossible repayment
    else
      calculator.calculate();

    interest = calculator.getInterest();
    truecost = calculator.getTrueCost();
    duration = calculator.getDuration();

    updateViews();
  }

  private void updateViews() {
    TextView trueview =
        (TextView) findViewById( R.id.results_message_truecost );
    trueview.setText( MessageBuilder.getCurrencyString( truecost ) );

    message = (TextView) findViewById( R.id.results_message );
    message.setText( Html.fromHtml( MessageBuilder.buildResultsMessage(
        this, truecost, price, interest, duration, method
        ) ) );
  }
}