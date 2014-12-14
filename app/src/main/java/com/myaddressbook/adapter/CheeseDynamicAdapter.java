package com.myaddressbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.daogenerator.AddressBook;
import com.dynamicgrid.BaseDynamicGridAdapter;
import com.myaddressbook.R;
import com.myaddressbook.util.TextDrawable;
import com.myaddressbook.util.Utils;

import java.util.List;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:56 PM
 */
public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {
    private Context mContext;

    public CheeseDynamicAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
        mContext = context;
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
        AddressBook addressBook = (AddressBook) getItem(position);
        holder.build(addressBook.getPeopleName(), addressBook.getDisplayColor());

        return convertView;
    }

    private class CheeseViewHolder {
        private TextView titleText;
        private ImageView image;
        private Drawable drawable;

        private CheeseViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void build(String title, String color) {
            titleText.setText(title);
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(Utils.toPx(mContext, 2))
                    .endConfig()
                    .buildRoundRect("", Color.parseColor(color), Utils.toPx(mContext, 10));
            image.setImageDrawable(drawable);

        }
    }
}
