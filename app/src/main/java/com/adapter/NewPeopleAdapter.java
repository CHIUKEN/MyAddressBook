package com.adapter;

import android.content.Context;
import android.database.Cursor;

import android.widget.SimpleCursorAdapter;

/**
 * Created by K on 2014/12/3.
 */
public class NewPeopleAdapter extends SimpleCursorAdapter{
        public NewPeopleAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
}
