package com.myaddressbook.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.daogenerator.AddressBook;
import com.daogenerator.Tag;
import com.dynamicgrid.DynamicGridView;
import com.myaddressbook.Cheeses;
import com.myaddressbook.R;
import com.myaddressbook.adapter.CheeseDynamicAdapter;
import com.myaddressbook.app.AppController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActGridSortSetting extends Activity {
    private static final String TAG = ActGridSortSetting.class.getName();
    private DynamicGridView gridView;
    private List<AddressBook> addressBookArrayList;
    private int mLevel;
    private String mParentNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_grid_sort_setting);
        mLevel = getIntent().getIntExtra("Level", -1);
        mParentNo = getIntent().getStringExtra("ParentNo");
        addressBookArrayList = AppController.getInstance().getDaofManger().getAddressBookList(mLevel, mParentNo);

        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        CheeseDynamicAdapter cheeseDynamicAdapter = new CheeseDynamicAdapter(this,
                addressBookArrayList,
                getResources().getInteger(R.integer.column_count));
        gridView.setAdapter(cheeseDynamicAdapter);

        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));

                AddressBook oldBook = addressBookArrayList.get(oldPosition);
                addressBookArrayList.remove(oldPosition);
                addressBookArrayList.add(newPosition, oldBook);

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_grid_sort_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit) {
            //TODO:UPDATE DB
            gridView.stopEditMode();
            for (int i = 0; i < addressBookArrayList.size(); i++) {
                addressBookArrayList.get(i).setSort(String.valueOf(1000 + i));
            }
            AppController.getInstance().getDaofManger().updateDataForSort(addressBookArrayList);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }
}
