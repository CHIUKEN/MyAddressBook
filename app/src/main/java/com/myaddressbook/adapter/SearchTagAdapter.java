package com.myaddressbook.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daogenerator.AddressBook;
import com.daogenerator.Tag;
import com.myaddressbook.R;

import java.util.List;

/**
 * Created by K on 2014/12/18.
 */
public class SearchTagAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Tag> tagList;


    public SearchTagAdapter(Activity activity, List<Tag> tagList) {
        this.tagList = tagList;

        inflater = LayoutInflater.from(activity);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        TagViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.item_search, viewGroup, false);
            viewHolder = new TagViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (TagViewHolder) view.getTag();
        }

        viewHolder.build(tagList.get(i).getTagName());


        return view;
    }
    class TagViewHolder {
        TextView txt_item;

        public TagViewHolder(View view) {
            this.txt_item = (TextView) view.findViewById(R.id.txt_item);
        }

        public void build(String str) {
            this.txt_item.setText(str);
        }
    }
}
