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
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Context
    Context _context;
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }
}
