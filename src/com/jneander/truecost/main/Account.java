/* True Cost Mobile : Account.java */
package com.jneander.truecost.main;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jneander.truecost.R;
import com.jneander.truecost.data.AccountData;
import com.jneander.truecost.data.Repayment;
import com.jneander.truecost.util.Alert;

public class Account extends Activity implements OnClickListener {
  private EditText balanceField;
  private EditText paymentField;
  private EditText aprField;
  private ViewFlipper viewFlipper;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.account );

    viewFlipper = (ViewFlipper) findViewById( R.id.account_viewFlipper );
    balanceField = (EditText) findViewById( R.id.account_balance_field );
    paymentField = (EditText) findViewById( R.id.account_payment_field );
    aprField = (EditText) findViewById( R.id.account_apr_field );

    Button activateButton = (Button) findViewById( R.id.intro_button_begin );
    activateButton.setOnClickListener( this );

    Button saveButton = (Button) findViewById( R.id.account_save_btn );
    saveButton.setOnClickListener( this );
  }

  @Override
  protected void onResume() {
    super.onResume();

    if ( !AccountData.exists( this ) )
      showIntroView();
    else
      loadAccountData();
  }

  @Override
  public void onConfigurationChanged( Configuration newConfig ) {
    super.onConfigurationChanged( newConfig );

    if ( findViewById( R.id.intro_layout ).getVisibility() == View.VISIBLE )
      showIntroView();
  }

  public void onClick( View v ) {
    switch ( v.getId() ) {
    case R.id.intro_button_begin:
      viewFlipper.setInAnimation( this, R.anim.view_transition_in_left );
      viewFlipper.setOutAnimation( this, R.anim.view_transition_out_right );
      viewFlipper.showNext();
      break;
    case R.id.account_save_btn:
      if ( saveData() )
        this.finish();
      break;
    }
  }

  @Override
  public boolean onKeyDown( int keyCode, KeyEvent event ) {
    boolean returnValue;

    if ( keyCode == KeyEvent.KEYCODE_BACK && !AccountData.exists( this ) ) {
      moveTaskToBack( true );
      returnValue = true;
    } else
      returnValue = super.onKeyDown( keyCode, event );

    return returnValue;
  }

  private void loadAccountData() {
    if ( AccountData.exists( this ) ) {
      balanceField.setText( String.valueOf( AccountData.getBalance( this ) ) );
      paymentField.setText( String.valueOf( AccountData.getPayment( this ) ) );
      aprField.setText( String.valueOf( AccountData.getAPR( this ) ) );
    }
  }

  private boolean saveData() {
    boolean dataWasSaved = false;

    if ( isValidData() ) {
      AccountData.saveData( this, getFieldString( balanceField ), getFieldString( paymentField ),
          getFieldString( aprField ) );
      dataWasSaved = true;
    } else {
      Alert.alertUser( this, Alert.AlertType.SMALL_PAYMENT );
    }

    return dataWasSaved;
  }

  private boolean isValidData() {
    boolean dataIsValid = true;

    dataIsValid = balanceField.length() > 0 && paymentField.length() > 0 && aprField.length() > 0;
    if ( dataIsValid ) {
      Repayment testRepayment =
          new Repayment( getFieldDouble( balanceField ), getFieldDouble( aprField ), getFieldDouble( paymentField ) );
      dataIsValid = !testRepayment.paymentIsTooSmall();
    }

    return dataIsValid;
  }

  private double getFieldDouble( EditText field ) {
    return Double.valueOf( field.getText().toString() );
  }

  private String getFieldString( EditText field ) {
    return field.getText().toString();
  }

  private void showIntroView() {
    if ( viewFlipper.getCurrentView().getId() == R.id.intro_layout )
      viewFlipper.showNext();
  }
}