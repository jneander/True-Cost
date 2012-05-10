package com.jneander.truecost.data;

import com.jneander.truecost.R;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class AccountData {
  public static void saveData( final Context caller, double balance,
      double payment, double APR ) {
    saveData( caller, String.valueOf( balance ), String.valueOf( payment ),
        String.valueOf( APR ) );
  }

  /* Save input data to preferences */
  public static void saveData( final Context caller, String balance,
      String payment, String APR ) {
    // Get a reference to the user preferences
    SharedPreferences data =
        caller.getSharedPreferences(
            caller.getString( R.string.app_prefs ),
            Context.MODE_PRIVATE );
    SharedPreferences.Editor editor = data.edit();

    // Prepare the values for storage with the editor.
    editor.putString( caller.getString( R.string.data_account_balance ),
        balance );
    editor.putString( caller.getString( R.string.data_account_payment ),
        payment );
    editor.putString( caller.getString( R.string.data_account_apr ), APR );

    // Commit the changes to the preferences.
    editor.commit();
  }

  /* Retrieve the Balance value currently stored in preferences */
  public static double getBalance( final Context caller ) {
    SharedPreferences data = caller.getSharedPreferences(
        caller.getString( R.string.app_prefs ), Context.MODE_PRIVATE );
    if ( data.contains(
        caller.getString( R.string.data_account_balance ) ) )
      return Double.valueOf( data.getString(
          caller.getString( R.string.data_account_balance ), null ) );
    return Double.NaN;
  }

  /* Retrieve the Payment value currently stored in preferences */
  public static double getPayment( final Context caller ) {
    SharedPreferences data = caller.getSharedPreferences(
        caller.getString( R.string.app_prefs ), Context.MODE_PRIVATE );
    if ( data.contains(
        caller.getString( R.string.data_account_payment ) ) )
      return Double.valueOf( data.getString(
          caller.getString( R.string.data_account_payment ), null ) );
    return Double.NaN;
  }

  /* Retrieve the APR value currently stored in preferences */
  public static double getAPR( final Context caller ) {
    SharedPreferences data = caller.getSharedPreferences(
        caller.getString( R.string.app_prefs ), Context.MODE_PRIVATE );
    if ( data.contains(
        caller.getString( R.string.data_account_apr ) ) )
      return Double.valueOf( data.getString(
          caller.getString( R.string.data_account_apr ), null ) );
    return Double.NaN;
  }

  /* Returns whether Account data is already present */
  public static boolean exists( final Context caller ) {
    SharedPreferences data = caller.getSharedPreferences(
        caller.getString( R.string.app_prefs ), Context.MODE_PRIVATE );
    return data.contains(
        caller.getString( R.string.data_account_balance ) );
  }
}
