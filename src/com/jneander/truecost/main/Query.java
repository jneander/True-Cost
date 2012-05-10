/* True Cost Mobile : Query.java */
package com.jneander.truecost.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jneander.truecost.R;
import com.jneander.truecost.data.AccountData;

public class Query extends Activity implements OnClickListener {
  // Declare class storage.
  private static int paymentMethodValue = 0;
  private Intent resultsIntent;

  /** Called when the activity is first created. */
  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.query );

    // Build the 'Payment Method' Spinner
    Spinner spinner =
        (Spinner) findViewById( R.id.query_spinner_paymentMethod );
    spinner.setOnItemSelectedListener( new QueryOnItemSelectedListener() );

    // Set the '[query]' Button
    Button resultsButton =
        (Button) findViewById( R.id.query_button_results );
    resultsButton.setOnClickListener( this );

    // Initialize 'Results' Intent
    resultsIntent = new Intent( this, Results.class );
  }

  @Override
  public void onResume() {
    super.onResume();

    // Check for existing AccountData
    if ( !AccountData.exists( this ) ) {
      // Load the 'Introduction'
      Intent i = new Intent( this, Account.class );
      startActivity( i );
    }
  }

  /** Listener for 'Payment Method' Spinner selections */
  public class QueryOnItemSelectedListener implements OnItemSelectedListener {
    public void onItemSelected( AdapterView< ? > parent, View view,
        int pos, long id ) {
      // Store the index position of the selected item.
      paymentMethodValue = pos;
    }

    public void onNothingSelected( AdapterView< ? > parent ) {
      // Do nothing.
    }
  }

  /** When the interface detects a 'click'... */
  public void onClick( View v ) {
    // Get the ID of the 'clicked' element
    switch ( v.getId() ) {
    case R.id.query_button_results:
      // If the 'Price' field contains characters...
      if ( ((EditText) findViewById( R.id.query_price_edittext ))
          .length() > 0 ) {
        // Insert 'Price' field data into 'Results' Intent extras
        resultsIntent.putExtra( "price",
            Double.valueOf( ((EditText) findViewById(
                R.id.query_price_edittext ))
                    .getText().toString() ) );
        resultsIntent.putExtra( "method", paymentMethodValue );

        startActivity( resultsIntent ); // Start 'Results' Activity
      } else {
        // Nothing was entered; inform the user.
        // TODO: Alert > "Please enter a price."
      }
      break;
    }
  }

  /** Create 'Menu' for this Activity */
  @Override
  public boolean onCreateOptionsMenu( Menu menu ) {
    super.onCreateOptionsMenu( menu ); // Call base class method.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate( R.menu.menu, menu );
    return true;
  }

  /** When a 'Menu' item is selected... */
  @Override
  public boolean onOptionsItemSelected( MenuItem item ) {
    // Get the ID of the selected item
    switch ( item.getItemId() ) {
    case R.id.menu_about:
      // Start 'About' Activity
      // TODO: Create 'About' Activity
      startActivity( new Intent( this, About.class ) );
      return true;
    case R.id.menu_account:
      // Start 'Account' Activity
      startActivity( new Intent( this, Account.class ) );
      return true;
    }

    return false;
  }
}