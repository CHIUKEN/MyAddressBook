package com.myaddressbook.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.daogenerator.AddressBook;
import com.gc.materialdesign.views.CheckBox;
import com.myaddressbook.Model.Contacts;
import com.myaddressbook.R;
import com.myaddressbook.app.AppController;

import java.util.List;

/**
 * Created by K on 2014/12/3.
 */
public class NewPeopleAdapter extends SimpleCursorAdapter {
    private Context context;
   // private Cursor cursor;
    private LayoutInflater inflater;
    private List<AddressBook>newAddressBookList;
    private List<Contacts>contactsList;
    public NewPeopleAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context=context;

        this.contactsList= AppController.getInstance().getContactsList();
    }
    /**
     * Reuses old views if they have not already been reset and inflates new
     * views for the rows in the list that needs a new one. It the adds a
     * listener to each checkbox that is used to store information about which
     * checkboxes have been checked or not. Finally we set the checked status of
     * the checkbox and let the super class do it's thing.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Cursor c = getCursor();
        c.moveToPosition(position);
        final Contacts contacts = new Contacts();
        contacts.setContactsName(c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
        contacts.setContactsPhone(c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_newpeople, null);
        }
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.selected);
        checkBox.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(boolean isChecked) {
                if(isChecked){
                    contactsList.add(contacts);
                }else{
                    contactsList.remove(contacts);
                }
            }
        });

        // If the selected items contains the current item, set the checkbox to be checked
        checkBox.setChecked(contactsList.contains(contacts));
        return super.getView(position, convertView, parent);
    }

    /**
     * Returns an array list with all the selected items as Track objects.
     *
     * @return the selected items
     */
    public List<Contacts> getSelectedItems()
    {
        return contactsList;
    }
//    public NewPeopleAdapter(Context context, Cursor c, int flags) {
//        super(context, c, flags);
//        this.context = context;
//        this.cursor = c;
//        inflater = LayoutInflater.from(context);
//
//    }

//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        final View view = inflater.inflate(R.layout.item_newpeople, parent, false);
//        CheckBox checkBox= (CheckBox) view.findViewById(R.id.selected);
//        checkBox.setOncheckListener(new CheckBox.OnCheckListener() {
//            @Override
//            public void onCheck(boolean isChecked) {
//                if(isChecked){
//
//                }else{
//
//                }
//            }
//        });
//        return view;
//    }

//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        TextView userName = (TextView) view.findViewById(R.id.txt_name);
//        userName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
//
//        TextView phoneNumber = (TextView) view.findViewById(R.id.txtphone);
//        phoneNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
//        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//        String emailid = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID));
//        //String email=getEmail(context, Long.parseLong(id));
////        Cursor emails =context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " =?" ,new String[]{id}, null);
////        while (emails.moveToNext())
////        {
////            email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
////            break;
////        }
//        String email=GetEmail(context);
//        if(email!=null) {
//            Log.i("----------", email);
//        }
////        emails.close();
//    }
    public static String getEmail(Context c, long id) {
        Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Email._ID,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.TYPE };
        String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID
                + " = '" + id + "'";
        String sortOrder = ContactsContract.CommonDataKinds.Email.ADDRESS
                + " COLLATE LOCALIZED ASC";

        Cursor cursor = c.getContentResolver().query(uri, projection,
                selection, null, sortOrder);

        int index = cursor
                .getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
        if (cursor.moveToNext()) {
            return cursor.getString(index);
        }
        return null;
    }
    private static final String[] PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.DATA
    };


    private String GetEmail(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, null, null, null);
        String displayName, address=null;
        if (cursor != null) {
            try {
                final int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
                final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                long contactId;

                while (cursor.moveToNext()) {
                    contactId = cursor.getLong(contactIdIndex);
                    displayName = cursor.getString(displayNameIndex);
                    address = cursor.getString(emailIndex);

                }
            } finally {
                cursor.close();
            }
        }
        return address;
    }
}
