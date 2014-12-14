package com.myaddressbook.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.daogenerator.AddressBook;
import com.myaddressbook.R;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by K on 2014/12/13.
 */
public class SearchAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {
    private Activity activity;
    private List<AddressBook> addressBookList;
    private LayoutInflater mInflater;


    public SearchAdapter(Activity activity, List<AddressBook> addressBookList) {
        this.addressBookList = addressBookList;
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;

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
        PeopleViewHolder peopleViewHolder;
        if (view == null) {

            view = mInflater.inflate(R.layout.item_people, viewGroup, false);
            peopleViewHolder = new PeopleViewHolder(activity, view);

            view.setTag(peopleViewHolder);
        } else {
            peopleViewHolder = (PeopleViewHolder) view.getTag();
        }
        AddressBook addressBook = addressBookList.get(i);
        peopleViewHolder.build(addressBook.getPeopleName(),addressBook.getPeoplePhone());


        return view;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int i) {
        return i;
    }

    @Override
    public int getSectionForPosition(int i) {
        return i;
    }

    @Override
    public View getHeaderView(int i, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.item_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.txt_search_header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        AddressBook addressBook = addressBookList.get(i);
        holder.text.setText(addressBook.getParentName());
        return convertView;
    }

    @Override
    public long getHeaderId(int i) {

        return Long.parseLong(addressBookList.get(i).getParentNo());
    }

    class HeaderViewHolder {
        TextView text;
    }

    public class PeopleViewHolder {
        public TextView txtName;
        public TextView txtPhone;
        public RelativeLayout RLcall;


        public PeopleViewHolder(final Activity activity, View view) {
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtPhone = (TextView) view.findViewById(R.id.txt_phone);
            RLcall = (RelativeLayout) view.findViewById(R.id.RL_cell);
            RLcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + txtPhone.getText()));
                    Log.i("phonenumber", txtPhone.getText().toString());
                    activity.startActivity(intent);
                }
            });
        }

        void build(String name, String phone) {
            txtName.setText(name);
            txtPhone.setText(phone);

        }
    }
}
