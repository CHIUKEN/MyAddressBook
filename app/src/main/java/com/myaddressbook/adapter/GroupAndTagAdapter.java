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
 * Created by K on 2014/12/13.
 */
public class GroupAndTagAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<AddressBook> addressBookList;

    public GroupAndTagAdapter(Activity activity, List<AddressBook> addressBookList) {
        this.addressBookList = addressBookList;
        inflater = LayoutInflater.from(activity);
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
        GroupAndTagViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_search, viewGroup, false);
            viewHolder = new GroupAndTagViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (GroupAndTagViewHolder) view.getTag();
        }
        viewHolder.build(addressBookList.get(i).getPeopleName());
        return view;
    }

    class GroupAndTagViewHolder {
        TextView txt_item;

        public GroupAndTagViewHolder(View view) {
            this.txt_item = (TextView) view.findViewById(R.id.txt_item);
        }

        public void build(String str) {
            this.txt_item.setText(str);
        }
    }
}
