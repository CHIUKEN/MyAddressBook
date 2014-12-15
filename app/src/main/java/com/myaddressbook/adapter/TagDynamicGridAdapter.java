package com.myaddressbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daogenerator.Tag;
import com.dynamicgrid.BaseDynamicGridAdapter;
import com.myaddressbook.R;
import com.myaddressbook.util.TextDrawable;
import com.myaddressbook.util.Utils;

import java.util.List;

/**
 * Created by K on 2014/12/15.
 */
public class TagDynamicGridAdapter extends BaseDynamicGridAdapter {
    private Context mContext;

    public TagDynamicGridAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
        this.mContext = context;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TagDynamicViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
            holder = new TagDynamicViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TagDynamicViewHolder) convertView.getTag();
        }
        Tag tag = (Tag) getItem(i);
        holder.build(tag.getTagName(), tag.getTagDisplayColor());
        return convertView;
    }

    class TagDynamicViewHolder {
        public TextView titleText;
        private ImageView image;
        private Drawable drawable;

        public TagDynamicViewHolder(View view) {
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
