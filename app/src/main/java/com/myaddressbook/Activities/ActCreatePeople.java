package com.myaddressbook.Activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.myaddressbook.Model.Contacts;
import com.myaddressbook.R;
import com.myaddressbook.adapter.NewPeopleAdapter;
import com.myaddressbook.app.AppController;
import com.myaddressbook.util.DaoManager;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.*;
import static android.provider.ContactsContract.CommonDataKinds.*;


public class ActCreatePeople extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static String Tag = ActCreatePeople.class.getName();
    private ListView mlistView;
    private NewPeopleAdapter mAdapter;
    //private NewPeopleAdapter mNewPeopleAdapter;
    private List<Contacts> mContactsList;
    private LoaderManager mloaderManager;
    private Uri uri = Phone.CONTENT_URI;
    private int mlevel;
    private String mParentNo;
    private String mParentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_act_create_people);
        mlistView = (ListView) findViewById(R.id.listView_new);
        mContactsList = new ArrayList<Contacts>();
        mloaderManager = getLoaderManager();
        mloaderManager.initLoader(0, null, this);
        mlevel = getIntent().getIntExtra("Level", -1);
        mParentNo = getIntent().getStringExtra("ParentNo");
        mParentName = getIntent().getStringExtra("ParentName");
        //顯示欄位
        String[] fields = new String[]{
                Data.DISPLAY_NAME, Phone.NUMBER,
        };

        Cursor cursor = getContacts();
        mAdapter = new NewPeopleAdapter(this, R.layout.item_newpeople, cursor,
                fields, new int[]{R.id.txt_name, R.id.txtphone}, 0);

        //mNewPeopleAdapter = new NewPeopleAdapter(this, cursor, 0);

//        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //add list
//
//                Cursor c = (Cursor) mAdapter.getItem(position);
//                Contacts contacts = new Contacts();
//                contacts.setContactsPhone(c.getString(c.getColumnIndex(Phone.NUMBER)));
//                contacts.setContactsName(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
//
//                if (isSelected(contacts)) {
//                    contactsList.remove(contacts);
//                } else {
//                    contactsList.add(contacts);
//                }
//                Log.i(Tag, String.valueOf(contactsList.size()));
//            }
//        });

        mlistView.setAdapter(mAdapter);

    }

    /*
    * Get Cursor
    * */
    private Cursor getContacts() {
        uri = Phone.CONTENT_URI;// ContactsContract.Contacts.CONTENT_URI;
        //篩選條件
        String[] projection = new String[]{
                ContactsContract.Contacts.Data._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.DISPLAY_NAME,
                Phone.NUMBER, Email._ID};

        String selection = null;
        String[] selectionArgs = null;
        //排序
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " DESC";
        return getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_act_create_people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //全選
        if (id == R.id.action_all) {
            mContactsList = AppController.getInstance().getContactsList();
            Cursor cursor = mAdapter.getCursor();
            if (cursor.moveToFirst()) {
                do {
                    try {
                        Contacts contacts = new Contacts();
                        contacts.setContactsName(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
                        contacts.setContactsPhone(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        if (!mContactsList.contains(contacts)) {
                            mContactsList.add(contacts);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
            mAdapter.notifyDataSetChanged();

            return true;
        }
        //確認
        if (id == R.id.action_ok) {
            //TODO:Insert to db
            mContactsList = mAdapter.getSelectedItems();
            DaoManager daoManager = AppController.getInstance().getDaofManger();
            daoManager.InsertPeopleList(mParentNo, mParentName, mContactsList);
            finish();
            Toast.makeText(this, getResources().getString(R.string.toast_insertpeople_success),Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[]{
                ContactsContract.Contacts.Data._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.DISPLAY_NAME,
                Phone.NUMBER, Email._ID};

        String selection = null;
        String[] selectionArgs = null;
        //排序
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " DESC";
        CursorLoader cursorLoader = new CursorLoader(this, Phone.CONTENT_URI, projection, selection, selectionArgs,
                sortOrder);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (mAdapter == null) {
            //顯示欄位
            String[] fields = new String[]{
                    Data.DISPLAY_NAME, Phone.NUMBER,
            };
            mAdapter = new NewPeopleAdapter(getApplicationContext(), R.layout.item_newpeople, cursor,
                    fields, new int[]{R.id.txt_name, R.id.txtphone}, 0);
            mlistView.setAdapter(mAdapter);
        } else {
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
