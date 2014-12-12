package com.myaddressbook.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.daogenerator.AddressBook;
import com.myaddressbook.R;

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
        PeopleViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_people, parent, false);
            viewHolder = new PeopleViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PeopleViewHolder) convertView.getTag();
        }
        AddressBook addressBook = addressBookList.get(position);
        viewHolder.build(addressBook.getPeopleName(), addressBook.getPeoplePhone());
        return convertView;
    }

    public void setAddressBookList(List<AddressBook> addressBookList) {
        this.addressBookList = addressBookList;
    }

    class PeopleViewHolder {
        public TextView txtName;
        public TextView txtPhone;

        public PeopleViewHolder(View view) {
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtPhone = (TextView) view.findViewById(R.id.txt_phone);
        }

        void build(String name, String phone) {
            txtName.setText(name);
            txtPhone.setText(phone);

        }
    }
}
