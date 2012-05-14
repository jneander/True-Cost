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
  private static int paymentMethodIndex = 0;
  private Intent resultsIntent;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.query );

    Spinner spinner = (Spinner) findViewById( R.id.query_spinner_paymentMethod );
    spinner.setOnItemSelectedListener( new QueryOnItemSelectedListener() );

    Button resultsButton = (Button) findViewById( R.id.query_button_results );
    resultsButton.setOnClickListener( this );

    resultsIntent = new Intent( this, Results.class );
  }

  @Override
  public void onResume() {
    super.onResume();

    if ( !AccountData.exists( this ) ) {
      Intent introIntent = new Intent( this, Account.class );
      startActivity( introIntent );
    }
  }

  public void onClick( View v ) {
    switch ( v.getId() ) {
    case R.id.query_button_results:
      if ( priceFieldContainsCharacters() ) {
        resultsIntent.putExtra( "price", getPriceFieldValue() );
        resultsIntent.putExtra( "method", paymentMethodIndex );

        startActivity( resultsIntent );
      } else {
        // Nothing was entered; inform the user.
        // TODO: Alert > "Please enter a price."
      }
      break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu ) {
    super.onCreateOptionsMenu( menu );

    MenuInflater inflater = getMenuInflater();
    inflater.inflate( R.menu.menu, menu );

    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item ) {
    boolean validItemSelected = false;

    switch ( item.getItemId() ) {
    case R.id.menu_about:
      startActivity( new Intent( this, About.class ) );
      validItemSelected = true;
      break;
    case R.id.menu_account:
      startActivity( new Intent( this, Account.class ) );
      validItemSelected = true;
      break;
    }

    return validItemSelected;
  }

  public class QueryOnItemSelectedListener implements OnItemSelectedListener {
    public void onItemSelected( AdapterView< ? > parent, View view, int selectedIndex, long id ) {
      paymentMethodIndex = selectedIndex;
    }

    public void onNothingSelected( AdapterView< ? > parent ) {}
  }

  private Double getPriceFieldValue() {
    return Double.valueOf( ((EditText) findViewById( R.id.query_price_edittext )).getText().toString() );
  }

  private boolean priceFieldContainsCharacters() {
    return ((EditText) findViewById( R.id.query_price_edittext )).length() > 0;
  }
}