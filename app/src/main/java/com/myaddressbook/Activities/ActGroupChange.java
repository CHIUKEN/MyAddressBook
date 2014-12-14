package com.myaddressbook.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.ViewGroup.LayoutParams;

import com.daogenerator.AddressBook;
import com.myaddressbook.Cheeses;
import com.myaddressbook.R;
import com.myaddressbook.adapter.GroupAndTagAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

public class ActGroupChange extends Activity implements SearchView.OnQueryTextListener {
    private ListView mListview;
    private SearchView mSearchView;
    private int mLevel;
    private String mParentNO;
    private GroupAndTagAdapter arrayAdapter;
    private List<AddressBook> addressBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_group_change);

        mLevel = getIntent().getIntExtra("Level", -1);
        mParentNO = getIntent().getStringExtra("ParentNo");

        if (mParentNO == null) {
            mParentNO = "";
        }
        addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(mLevel, mParentNO);
        mListview = (ListView) findViewById(R.id.listview_group);

        // String[] groupArray = Cheeses.sCheeseStrings;
        arrayAdapter = new GroupAndTagAdapter(this, addressBookList);

        mListview.setAdapter(arrayAdapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO:取值，返回前頁

                Intent result = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("AddressBook", addressBookList.get(i));
                result.putExtras(bundle);
                setResult(RESULT_OK, result);
                finish();
            }
        });
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
            addressBookList.clear();
            addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(mLevel, mParentNO));
            arrayAdapter.notifyDataSetChanged();
        } else {
            // Sets the initial value for the text filter.
            addressBookList.clear();
            addressBookList.addAll(AppController.getInstance().getDaofManger().filterGroup(mLevel, mParentNO, "%" + s.toString() + "%"));
            arrayAdapter.notifyDataSetChanged();

        }
        return false;
    }
}
