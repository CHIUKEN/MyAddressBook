package com.myaddressbook.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.daogenerator.AddressBook;
import com.dynamicgrid.DynamicGridView;
import com.myaddressbook.R;
import com.myaddressbook.adapter.CheeseDynamicAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

public class ActGridSortSetting extends Activity {
    private static final String TAG = ActGridSortSetting.class.getName();
    private DynamicGridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_grid_sort_setting);
        List<AddressBook> addressBookArrayList = AppController.getInstance().getDaofManger().getAddressBookList("1");
        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        gridView.setAdapter(new CheeseDynamicAdapter(this,
                addressBookArrayList,
                getResources().getInteger(R.integer.column_count)));

        //
//        add callback to stop edit mode if needed
//        gridView.setOnDropListener(new DynamicGridView.OnDropListener()
//        {
//            @Override
//            public void onActionDrop()
//            {
//                gridView.stopEditMode();
//            }
//        });
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
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
}
