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
public class PeopleSortAdapter extends BaseAdapter {
    private List<AddressBook> addressBookList;
    private LayoutInflater inflater;
    private Activity activity;

    public PeopleSortAdapter(Activity activity, List<AddressBook> addressBookList) {
        this.addressBookList = addressBookList;
        this.activity = activity;
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
        PeopleSortViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_click_remove, viewGroup, false);
            viewHolder = new PeopleSortViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (PeopleSortViewHolder) view.getTag();
        }
        viewHolder.bindView(addressBookList.get(i).getPeopleName());
        return view;
    }

    class PeopleSortViewHolder {
        TextView text;

        public PeopleSortViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.text);
        }

        public void bindView(String txt) {
            text.setText(txt);
        }
    }
}
