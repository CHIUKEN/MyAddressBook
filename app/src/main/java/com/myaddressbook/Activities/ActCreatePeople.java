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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.myaddressbook.Model.Contacts;
import com.myaddressbook.R;
import com.myaddressbook.adapter.ContactsAdapter;
import com.myaddressbook.adapter.NewPeopleAdapter;
import com.myaddressbook.app.AppController;
import com.myaddressbook.util.DaoManager;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.*;
import static android.provider.ContactsContract.CommonDataKinds.*;


public class ActCreatePeople extends Activity {
    private static String Tag = ActCreatePeople.class.getName();
    private ListView mlistView;


    private List<Contacts> mContactsList;

    private Uri uri = Phone.CONTENT_URI;
    private int mlevel;
    private String mParentNo;
    private String mParentName;
    private ArrayList<Contacts> listContacts;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_act_create_people);
        mlistView = (ListView) findViewById(R.id.listView_new);
        mContactsList = new ArrayList<Contacts>();

        mlevel = getIntent().getIntExtra("Level", -1);
        mParentNo = getIntent().getStringExtra("ParentNo");
        mParentName = getIntent().getStringExtra("ParentName");


        listContacts = fetchAll();

        contactsAdapter = new ContactsAdapter(this, listContacts);
        mlistView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();

    }

    public void fetchContactNumbers(Cursor cursor, Contacts contact) {
        // Get numbers
        final String[] numberProjection = new String[]{Phone.NUMBER, Phone.TYPE,};
        Cursor phone = new CursorLoader(this, Phone.CONTENT_URI, numberProjection,
                Phone.CONTACT_ID + "= ?",
                new String[]{String.valueOf(contact.id)},
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);

            while (!phone.isAfterLast()) {
                final String number = phone.getString(contactNumberColumnIndex);
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType =
                        ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                                this.getResources(), type, customLabel);
                contact.addNumber(number, phoneType.toString());
                phone.moveToNext();
            }

        }
        phone.close();
    }

    public ArrayList<Contacts> fetchAll() {
        ArrayList<Contacts> listContacts = new ArrayList<Contacts>();
        CursorLoader cursorLoader = new CursorLoader(this,
                ContactsContract.Contacts.CONTENT_URI, // uri
                null, // the columns to retrieve (all)
                null, // the selection criteria (none)
                null, // the selection args (none)
                null // the sort order (default)
        );
        // This should probably be run from an AsyncTask
        Cursor c = cursorLoader.loadInBackground();
        if (c.moveToFirst()) {
            do {
                Contacts contact = loadContactData(c);
                listContacts.add(contact);
            } while (c.moveToNext());
        }
        c.close();
        return listContacts;
    }

    private Contacts loadContactData(Cursor c) {
        // Get Contact ID
        int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
        String contactId = c.getString(idIndex);
        // Get Contact Name
        int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String contactDisplayName = c.getString(nameIndex);
        Contacts contact = new Contacts(contactId, contactDisplayName);
        fetchContactNumbers(c, contact);
        fetchContactEmails(c, contact);
        return contact;
    }

    public void fetchContactEmails(Cursor cursor, Contacts contact) {
        // Get email
        final String[] emailProjection = new String[]{Email.DATA, Email.TYPE};

        Cursor email = new CursorLoader(this, Email.CONTENT_URI, emailProjection,
                Email.CONTACT_ID + "= ?",
                new String[]{String.valueOf(contact.id)},
                null).loadInBackground();

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);
            final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);

            while (!email.isAfterLast()) {
                final String address = email.getString(contactEmailColumnIndex);
                final int type = email.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence emailType =
                        ContactsContract.CommonDataKinds.Email.getTypeLabel(
                                this.getResources(), type, customLabel);
                contact.addEmail(address, emailType.toString());
                email.moveToNext();
            }

        }

        email.close();
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
            for (Contacts contacts : listContacts) {
                if (!mContactsList.contains(contacts)) {
                    mContactsList.add(contacts);
                }
            }
            contactsAdapter.notifyDataSetChanged();
//            Cursor cursor = contactsAdapter.getCursor();
//            if (cursor.moveToFirst()) {
//                do {
//                    try {
//                        Contacts contacts = new Contacts();
//                        contacts.setContactsName(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
//                        contacts.setContactsPhone(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
//                        if (!mContactsList.contains(contacts)) {
//                            mContactsList.add(contacts);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } while (cursor.moveToNext());
//            }
//            contactsAdapter.notifyDataSetChanged();

            return true;
        }
        //確認
        if (id == R.id.action_ok) {
            //TODO:Insert to db
            mContactsList = contactsAdapter.getSelectedItems();
            DaoManager daoManager = AppController.getInstance().getDaofManger();
            daoManager.InsertPeopleList(mParentNo, mParentName, mContactsList);
            setResult(RESULT_OK);
            finish();
            //清空
            AppController.getInstance().clearSelectContacts();
            Toast.makeText(this, getResources().getString(R.string.toast_insertpeople_success), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AppController.getInstance().clearSelectContacts();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
