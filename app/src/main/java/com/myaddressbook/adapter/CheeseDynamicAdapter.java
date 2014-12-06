package com.myaddressbook.adapter;
import android.content.Context;
import android.graphics.Color;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.daogenerator.AddressBook;
import com.dynamicgrid.BaseDynamicGridAdapter;
import com.myaddressbook.R;

import java.util.List;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:56 PM
 */
public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {
    public CheeseDynamicAdapter(Context context, List<AddressBook> items, int columnCount) {
        super(context, items, columnCount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheeseViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
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
