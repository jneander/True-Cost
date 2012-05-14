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
  private Calculator.Method method;
  private double price, interest, truecost;
  private int duration;

  TextView resultsMessage;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.results );

    loadCalculationArguments();

    Calculator calculator = new Calculator();
    calculator.setAccountBalance( AccountData.getBalance( this ) );
    calculator.setApr( AccountData.getAPR( this ) );
    calculator.setPayment( AccountData.getPayment( this ) );
    calculator.setPrice( price );
    calculator.setMethod( method );

    interest = calculator.getInterest();
    truecost = calculator.getTrueCost();
    duration = calculator.getDuration();

    updateViews();
  }

  private void loadCalculationArguments() {
    Bundle extras = getIntent().getExtras();
    method = extras.getInt( "method" ) == 0 ? Calculator.Method.CASH : Calculator.Method.CREDIT;
    price = extras.getDouble( "price" );
  }

  private void updateViews() {
    TextView trueview = (TextView) findViewById( R.id.results_message_truecost );
    trueview.setText( MessageBuilder.getCurrencyString( truecost ) );

    resultsMessage = (TextView) findViewById( R.id.results_message );
    resultsMessage.setText( Html.fromHtml( MessageBuilder.buildResultsMessage(
        this, truecost, price, interest, duration, method ) ) );
  }
}