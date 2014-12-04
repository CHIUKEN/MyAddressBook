package com.myaddressbook;

import android.app.Activity;
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

import com.Model.Contacts;
import com.adapter.NewPeopleAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.*;
import static android.provider.ContactsContract.CommonDataKinds.*;


public class ActCreatePeople extends Activity {
    private static String Tag = ActCreatePeople.class.getName();
    private ListView mlistView;
    private SimpleCursorAdapter mAdapter;
    private NewPeopleAdapter mNewPeopleAdapter;
    private List<Contacts> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_act_create_people);
        mlistView = (ListView) findViewById(R.id.listView_new);
        contactsList = new ArrayList<Contacts>();
        //顯示欄位
        String[] fields = new String[]{
                Data.DISPLAY_NAME, Phone.NUMBER,
        };

        Cursor cursor = getContacts();
        mAdapter = new SimpleCursorAdapter(this, R.layout.item_newpeople, cursor,
                fields, new int[]{R.id.txt_name, R.id.txtphone}, 0);

        mNewPeopleAdapter = new NewPeopleAdapter(this, cursor, 0);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //add list

                Cursor c = (Cursor) mAdapter.getItem(position);
                Contacts contacts = new Contacts();
                contacts.setContactsPhone(c.getString(c.getColumnIndex(Phone.NUMBER)));
                contacts.setContactsName(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                if (isSelected(contacts)) {
                    contactsList.remove(contacts);
                } else {
                    contactsList.add(contacts);
                }
                Log.i(Tag, String.valueOf(contactsList.size()));
            }
        });

        //mlistView.setAdapter(mAdapter);
        mlistView.setAdapter(mNewPeopleAdapter);
    }

    /*
    * Get Cursor
    * */
    private Cursor getContacts() {
        Uri uri = Phone.CONTENT_URI;// ContactsContract.Contacts.CONTENT_URI;
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
    /*
    判斷是否存在
    * */
    private boolean isSelected(Contacts userName) {
        int index = contactsList.indexOf(userName);
        return !(index == -1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_act_create_people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //全選
        if (id == R.id.action_all) {
            return true;
        }
        //確認
        if (id == R.id.action_ok) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
