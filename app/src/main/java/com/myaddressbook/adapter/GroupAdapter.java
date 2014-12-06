package com.myaddressbook.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daogenerator.AddressBook;
import com.myaddressbook.R;

import java.util.List;

/**
 * Created by K on 2014/12/6.
 */
public class GroupAdapter extends BaseAdapter{
    private List<AddressBook>mAddressbookList;
    private Activity activity;
    private LayoutInflater inflater;
    public GroupAdapter(Activity activity,List<AddressBook>addressBookList){
        this.activity=activity;
        this.mAddressbookList=addressBookList;
        inflater=LayoutInflater.from(activity);
    }
    @Override
    public int getCount() {
        return mAddressbookList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddressbookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheeseViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid, null);
            holder = new CheeseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CheeseViewHolder) convertView.getTag();
        }
        AddressBook addressBook=(AddressBook)getItem(position);
        holder.build(addressBook.getPeopleName(),addressBook.getDisplayColor());
        // convertView.setBackgroundColor(Color.parseColor(addressBook.getDisplayColor()));
        return convertView;

    }



    private class CheeseViewHolder {
        private TextView titleText;
        private ImageView image;

        private CheeseViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void build(String title,String color) {
            titleText.setText(title);
            image.setImageResource(R.drawable.ic_launcher);
            image.setBackgroundColor(Color.parseColor(color));
        }
    }
}
