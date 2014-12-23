package com.myaddressbook.Activities;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import com.daogenerator.AddressBook;

import com.daogenerator.Tag;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.myaddressbook.R;
import com.myaddressbook.adapter.SearchGroupAdapter;

import com.myaddressbook.adapter.SearchTagAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;


public class ActGroupChange extends Activity implements SearchView.OnQueryTextListener {
    private ListView mListview;
    private SearchView mSearchView;
    private int mLevel;
    private String mParentNO;
    private SearchGroupAdapter arrayAdapter;
    private List<AddressBook> addressBookList;
    private SearchTagAdapter tagAdapter;
    private List<Tag> tagList;
    private TextView txt_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get tracker.
        Tracker t = ((AppController) this.getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(ActGroupChange.class.getSimpleName());
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);

        setContentView(R.layout.activity_act_group_change);
        getActionBar().setTitle(getIntent().getStringExtra("Title"));
        mLevel = getIntent().getIntExtra("Level", -1);
        mParentNO = getIntent().getStringExtra("ParentNo");

        if (mParentNO == null) {
            mParentNO = "";
        }
        addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(mLevel, mParentNO);
        mListview = (ListView) findViewById(R.id.listview_group);
        txt_no_data = (TextView) findViewById(R.id.txt_no_data);

        if (mLevel != -1) {
            arrayAdapter = new SearchGroupAdapter(this, addressBookList);
            mListview.setAdapter(arrayAdapter);
            if (addressBookList.size() == 0) {
                mListview.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.VISIBLE);
            } else {
                mListview.setVisibility(View.VISIBLE);
                txt_no_data.setVisibility(View.GONE);
            }

            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //TODO:取值，返回前頁

                    Intent result = new Intent();
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("Model", addressBookList.get(i));
                    result.putExtras(bundle);
                    setResult(RESULT_OK, result);
                    finish();
                }
            });
        } else {
            tagList = AppController.getInstance().getDaofManger().getAllTag();
            tagAdapter = new SearchTagAdapter(this, tagList);
            mListview.setAdapter(tagAdapter);
            if (tagList.size() == 0) {
                mListview.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.VISIBLE);
                txt_no_data.setText(R.string.tip_no_addtag);
            } else {
                mListview.setVisibility(View.VISIBLE);
                txt_no_data.setVisibility(View.GONE);
            }
            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //TODO:取值，返回前頁
                    Intent result = new Intent();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Model", tagList.get(i));
                    result.putExtras(bundle);
                    setResult(RESULT_OK, result);
                    finish();
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_group_change, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();
//        searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
//                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        searchItem.expandActionView();
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if (TextUtils.isEmpty(s)) {
            // Clear the text filter.
            if (mLevel != -1) {
                addressBookList.clear();
                addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(mLevel, mParentNO));
                arrayAdapter.notifyDataSetChanged();
                checkDataCount(addressBookList);

            } else {
                tagList.clear();
                tagList.addAll(AppController.getInstance().getDaofManger().getAllTag());
                tagAdapter.notifyDataSetChanged();
                checkDataCount(tagList);
            }
        } else {
            // Sets the initial value for the text filter.
            if (mLevel != -1) {
                addressBookList.clear();
                List<AddressBook> result = AppController.getInstance().getDaofManger().filterGroup(mLevel, mParentNO, "%" + s.toString() + "%");
                if (result.size() > 0) {
                    addressBookList.addAll(result);
                }
                checkDataCount(addressBookList);
                arrayAdapter.notifyDataSetChanged();
            } else {
                tagList.clear();
                tagList.addAll(AppController.getInstance().getDaofManger().filerTag("%" + s.toString() + "%"));
                tagAdapter.notifyDataSetChanged();
                checkDataCount(tagList);
            }
        }
        return false;
    }

    private void checkDataCount(List<?> data) {
        if (data.size() == 0) {
            mListview.setVisibility(View.GONE);
            txt_no_data.setText(R.string.tip_no_data);
            txt_no_data.setVisibility(View.VISIBLE);
        } else {
            mListview.setVisibility(View.VISIBLE);
            txt_no_data.setVisibility(View.GONE);
        }
    }
}
