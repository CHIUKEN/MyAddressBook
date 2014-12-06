package com.myaddressbook.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.daogenerator.AddressBook;

import java.util.List;

/**
 * Created by K on 2014/12/6.
 */
public class PeopleListAdapter extends BaseAdapter {
    private List<AddressBook> addressBookList;
    private LayoutInflater inflater;

    public PeopleListAdapter(Activity activity, List<AddressBook> addressBookList) {
        this.addressBookList = addressBookList;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return addressBookList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
