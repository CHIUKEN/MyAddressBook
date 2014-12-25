package com.myaddressbook.Activities;

import android.app.Activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;

import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.ContactsContract;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;


import android.view.View;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.myaddressbook.Model.Contacts;
import com.myaddressbook.R;
import com.myaddressbook.adapter.ContactsAdapter;

import com.myaddressbook.app.AppController;
import com.myaddressbook.util.DaoManager;

import java.util.ArrayList;
import java.util.List;


import me.drakeet.materialdialog.MaterialDialog;

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
    private ProgressBarCircularIndeterminate progress;
    private MaterialDialog mMaterialDialog;
    private TextView txt_no_data;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case 1:
                    contactsAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                    if (listContacts.size() == 0) {
                        txt_no_data.setVisibility(View.VISIBLE);
                    } else {
                        txt_no_data.setVisibility(View.GONE);
                    }
                    break;

            }
        }
    };

    private HandlerThread mThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Get tracker.
        Tracker t = ((AppController) this.getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(ActCreatePeople.class.getSimpleName());
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_act_create_people);

//            ContentValues values = new ContentValues();
//
//            Uri rawContactUri =
//                    getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
//            long rawContactId = ContentUris.parseId(rawContactUri);
//
//            values.clear();
//            values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
//            values.put(ContactsContract.RawContacts.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
//            values.put(StructuredName.GIVEN_NAME, "AFadfafdSEQ"+i);
//            getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
//
//            values.clear();
//            values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
//            values.put(ContactsContract.RawContacts.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
//            values.put(Phone.TYPE, Phone.TYPE_MOBILE);
//            values.put(Phone.NUMBER, "0945123654");
//            getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        mMaterialDialog = new MaterialDialog(this);
        txt_no_data = (TextView) findViewById(R.id.txt_createpeople_tiptext);
        mlistView = (ListView) findViewById(R.id.listView_new);
        progress = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndetermininate);

        mContactsList = new ArrayList<Contacts>();

        mlevel = getIntent().getIntExtra("Level", -1);
        mParentNo = getIntent().getStringExtra("ParentNo");
        mParentName = getIntent().getStringExtra("ParentName");

        mThread = new HandlerThread("Create");
        mThread.start();

        Handler mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(runnable);

        listContacts = new ArrayList<Contacts>();

        contactsAdapter = new ContactsAdapter(this, listContacts);
        mlistView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<Contacts> arrayList = fetchAll();
            for (int i = 0; i < arrayList.size(); i++) {
                boolean isExist = AppController.getInstance().getDaofManger().queryIsExist(arrayList.get(i).getContactsName());
                if (!isExist) {
                    listContacts.add(arrayList.get(i));
                }
            }
            mHandler.obtainMessage(1).sendToTarget();
        }
    };

    @Override
    protected void onDestroy() {
        //将线程与当前handler解除
        mHandler.removeCallbacks(runnable);

        super.onDestroy();
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
            View view = LayoutInflater.from(this).inflate(R.layout.progressbar_item, null);
            mMaterialDialog.setView(view).show();
            //TODO:Insert to db
            mContactsList = contactsAdapter.getSelectedItems();
            if (mContactsList.size() > 0) {
                DaoManager daoManager = AppController.getInstance().getDaofManger();
                daoManager.InsertPeopleList(mParentNo, mParentName, mContactsList);
            }
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

        }
        return super.onKeyDown(keyCode, event);

    }
}
