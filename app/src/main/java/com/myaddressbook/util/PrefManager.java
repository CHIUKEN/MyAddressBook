package com.myaddressbook.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by K on 2014/12/6.
 */
public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Sharedpref file name
    private static final String PREF_NAME = "MyAddressBook";
    // gallery albums key
    private static final String KEY_FIRST = "First";
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Context
    Context _context;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }
    /**
     * Storing google username
     * */
    public void setGoogleUsername(String first) {
        editor = pref.edit();

        editor.putString(KEY_FIRST, first);

        // commit changes
        editor.commit();
    }

    public String getGoogleUserName() {
        return pref.getString(KEY_FIRST, "");
    }

}
