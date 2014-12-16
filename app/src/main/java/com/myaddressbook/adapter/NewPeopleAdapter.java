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
    private List<AddressBook> newAddressBookList;
    private List<Contacts> contactsList;
    private List<AddressBook> addressBookList;

    public NewPeopleAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;

        this.contactsList = AppController.getInstance().getContactsList();
        this.addressBookList = AppController.getInstance().getDaofManger().getall();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

        final Contacts contacts = new Contacts();
        contacts.setContactsName(c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
        contacts.setContactsPhone(c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_newpeople, null);
        }
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.selected);
        checkBox.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(boolean isChecked) {
                if (isChecked) {
                    contactsList.add(contacts);
                } else {
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
    public List<Contacts> getSelectedItems() {
        return contactsList;
    }



}
