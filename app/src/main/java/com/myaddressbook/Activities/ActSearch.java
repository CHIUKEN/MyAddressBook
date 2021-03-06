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

import com.daogenerator.AddressBook;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.myaddressbook.R;
import com.myaddressbook.adapter.SearchAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ActSearch extends Activity implements SearchView.OnQueryTextListener {
    private StickyListHeadersListView mListview;
    private List<AddressBook> addressBookList;
    private SearchAdapter searchAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get tracker.
        Tracker t = ((AppController) this.getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(this.getClass().getSimpleName());
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);

        setContentView(R.layout.activity_act_search);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        mListview = (StickyListHeadersListView) findViewById(R.id.lv_search);
        addressBookList = AppController.getInstance().getDaofManger().getall();
        searchAdapter = new SearchAdapter(this, addressBookList);
        mListview.setAdapter(searchAdapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressBook addressBook = addressBookList.get(i);
                int level=AppController.getInstance().getDaofManger().getParentNoLevel(addressBook.getParentNo());
                Intent intent = new Intent(ActSearch.this, ActPeopleDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Level", level);
                bundle.putSerializable("AddressBook", addressBook);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();
        //searchItem.expandActionView();
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
        if(id==android.R.id.home){
            finish();
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
            addressBookList.clear();
            addressBookList.addAll(AppController.getInstance().getDaofManger().getall());
            searchAdapter.notifyDataSetChanged();
        } else {
            // Sets the initial value for the text filter.
            addressBookList.clear();
            addressBookList.addAll(AppController.getInstance().getDaofManger().searchAll("%" + s.toString() + "%"));
            searchAdapter.notifyDataSetChanged();

        }
        return false;
    }
}
