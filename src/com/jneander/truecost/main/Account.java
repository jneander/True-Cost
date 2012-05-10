/* True Cost Mobile : Account.java */
package com.jneander.truecost.main;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.jneander.truecost.R;
import com.jneander.truecost.data.AccountData;

public class Account extends Activity implements OnClickListener {
  // Declare class storage.
  private EditText balanceField;
  private EditText paymentField;
  private EditText aprField;
  private static ViewFlipper viewFlipper;

  /** Called when the activity is first created. */
  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.account );

    viewFlipper = (ViewFlipper) findViewById( R.id.account_viewFlipper );

    // Initialize interface element references
    balanceField = (EditText) findViewById( R.id.account_balance_field );
    paymentField = (EditText) findViewById( R.id.account_payment_field );
    aprField = (EditText) findViewById( R.id.account_apr_field );

    // Set the 'Let's Begin' Button
    Button activateButton =
        (Button) findViewById( R.id.intro_button_begin );
    activateButton.setOnClickListener( this );

    // Set the 'Save Changes' Button
    Button saveButton = (Button) findViewById( R.id.account_save_btn );
    saveButton.setOnClickListener( this );
  }

  /** Called when the activity resumes. */
  @Override
  protected void onResume() {
    super.onResume(); // Call base class method.

    // Check for the presence of stored data.
    if ( !AccountData.exists( this ) ) {
      // No data exists; first run is assumed.
      // Display 'Intro' View.
      viewFlipper.showNext();
    }
    loadData(); // load stored user data
  }

  @Override
  public void onConfigurationChanged( Configuration newConfig ) {
    boolean showIntro =
        findViewById( R.id.intro_layout ).getVisibility() == View.VISIBLE;
    super.onConfigurationChanged( newConfig );
    if ( showIntro
        && viewFlipper.getCurrentView().getId() == R.id.intro_layout ) {
      Log.d( "True Cost", "onConfigurationChanged: not the intro" );
      // viewFlipper.showNext();
    }
  }

  /** Called when the interface detects a 'click'. */
  public void onClick( View v ) {
    // Get the ID of the 'clicked' element
    switch ( v.getId() ) {
    case R.id.intro_button_begin: // 'Let's Begin' Button
      viewFlipper.setInAnimation( this, R.anim.view_transition_in_left );
      viewFlipper
          .setOutAnimation( this, R.anim.view_transition_out_right );
      viewFlipper.showNext();
      break;
    case R.id.account_save_btn: // 'Save Changes' Button
      if ( saveData() ) // if data was saved...
        this.finish(); // Destroy this Activity
      break;
    }
  }

  /** Called when a 'key' is pressed. */
  @Override
  public boolean onKeyDown( int keyCode, KeyEvent event ) {
    // If the 'back' key was pressed...
    if ( keyCode == KeyEvent.KEYCODE_BACK && !AccountData.exists( this ) ) {
      // Send the application to back
      moveTaskToBack( true );
      return true;
    }
    // Some other key was pressed
    return super.onKeyDown( keyCode, event );
  }

  /** Get and display stored user data */
  private void loadData() {
    // Check for the presence of stored data.
    if ( AccountData.exists( this ) ) {
      // Update the interface elements.
      balanceField.setText(
          String.valueOf( AccountData.getBalance( this ) ) );
      paymentField.setText(
          String.valueOf( AccountData.getPayment( this ) ) );
      aprField.setText(
          String.valueOf( AccountData.getAPR( this ) ) );
    }
  }

  /** Save the data entered by the user. */
  private boolean saveData() {
    // Check the interface fields for content.
    boolean validData = balanceField.length() > 0
        && paymentField.length() > 0
        && aprField.length() > 0;

    // Check 'validData' to determine if data is valid.
    if ( validData ) { // Values were entered.
      // Save the content into preferences.
      AccountData.saveData( this, balanceField.getText().toString(),
          paymentField.getText().toString(), aprField.getText()
              .toString() );
      return true; // SUCCESS: Data was acceptable.
    } else {
      // Not all fields are filled; warn the user.
      return false; // FAILURE: Data was not acceptable.
    }
  }
}