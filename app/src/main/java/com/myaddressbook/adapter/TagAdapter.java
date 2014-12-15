package com.myaddressbook.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daogenerator.Tag;
import com.myaddressbook.R;
import com.myaddressbook.util.TextDrawable;
import com.myaddressbook.util.Utils;

import java.util.List;

/**
 * Created by K on 2014/12/15.
 */
public class TagAdapter extends BaseAdapter {
    private Activity activity;
    private List<Tag> tagList;
    private LayoutInflater inflater;

    public TagAdapter(Activity activity, List<Tag> tagList) {
        inflater = LayoutInflater.from(activity);
        this.tagList = tagList;
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int i) {
        return tagList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TagViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid, null);
            holder = new TagViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TagViewHolder) convertView.getTag();
        }
        holder.build(tagList.get(i).getTagName(), tagList.get(i).getTagDisplayColor());
        return convertView;
    }

    class TagViewHolder {
        public TextView titleText;
        private ImageView image;
        private Drawable drawable;

        public TagViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);

        }

        void build(String title, String color) {
            titleText.setText(title);

            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(Utils.toPx(activity, 2))
                    .endConfig()
                    .buildRoundRect("", Color.parseColor(color), Utils.toPx(activity, 10));
            image.setImageDrawable(drawable);
        }
    }
}
