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
 * Created by K on 2014/12/15.
 */
public class AddTagAdapter extends BaseAdapter {
    private List<AddressBook> addressBookList;
    private Activity activity;
    private LayoutInflater inflater;

    public AddTagAdapter(Activity activity, List<AddressBook> addressBookList) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.addressBookList = addressBookList;
    }

    @Override
    public int getCount() {
        return addressBookList.size();
    }

    @Override
    public Object getItem(int i) {
        return addressBookList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TagPeopleViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_newpeople, viewGroup, false);
            viewHolder=new TagPeopleViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder= (TagPeopleViewHolder) view.getTag();
        }
        viewHolder.build(addressBookList.get(i).getPeopleName(),addressBookList.get(i).getPeoplePhone());
        return view;
    }

    class TagPeopleViewHolder {
        TextView txtName;
        TextView txtPhone;

        public TagPeopleViewHolder(View view) {
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtPhone = (TextView) view.findViewById(R.id.txt_phone);
        }

        public void build(String name, String phone) {
            txtPhone.setText(phone);
            txtName.setText(name);
        }
    }
}
