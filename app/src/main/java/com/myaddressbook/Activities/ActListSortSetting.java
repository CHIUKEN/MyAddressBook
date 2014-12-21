package com.myaddressbook.Activities;

import android.app.Activity;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Toast;

import com.daogenerator.AddressBook;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.myaddressbook.R;

import com.myaddressbook.adapter.PeopleSortAdapter;
import com.myaddressbook.app.AppController;

import java.util.ArrayList;
import java.util.List;

public class ActListSortSetting extends Activity {


    private int mDragStartMode = DragSortController.ON_DRAG;
    private boolean mRemoveEnabled = true;
    private int mRemoveMode = DragSortController.CLICK_REMOVE;
    private boolean mSortEnabled = true;
    private boolean mDragEnabled = true;

    private DragSortListView mDslv;
    private DragSortController mController;


    private String mParentNo;
    private List<AddressBook> addressBookList;
    private PeopleSortAdapter mPeopleListAdapter;
    //DELETE LIST
    private List<AddressBook> deleteList;

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

        setContentView(R.layout.activity_act_list_sort_setting);
        mDslv = (DragSortListView) findViewById(android.R.id.list);
        mController = buildController(mDslv);
        mDslv.setFloatViewManager(mController);
        mDslv.setOnTouchListener(mController);
        mDslv.setDragEnabled(mDragEnabled);


        mParentNo = getIntent().getStringExtra("ParentNo");
        addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(4, mParentNo);
        mPeopleListAdapter = new PeopleSortAdapter(this, addressBookList);
        mDslv.setAdapter(mPeopleListAdapter);

        deleteList = new ArrayList<AddressBook>();

        mDslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String message = String.format("Clicked item %d", i);
                Toast.makeText(ActListSortSetting.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        mDslv.setDropListener(onDrop);
        mDslv.setRemoveListener(onRemove);
    }

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    if (from != to) {
                        Log.d("", String.format("drag item position changed from %d to %d", from, to));
                        AddressBook addressBook = addressBookList.get(from);
                        addressBookList.remove(from);
                        addressBookList.add(to, addressBook);
                        mPeopleListAdapter.notifyDataSetChanged();
                    }
                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    AddressBook delet = addressBookList.get(which);
                    deleteList.add(delet);

//                    adapter.remove(adapter.getItem(which));
                }
            };


    public DragSortController buildController(DragSortListView dslv) {
        // defaults are
        //   dragStartMode = onDown
        //   removeMode = flingRight
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setClickRemoveId(R.id.click_remove);
        controller.setRemoveEnabled(mRemoveEnabled);
        controller.setSortEnabled(mSortEnabled);
        controller.setDragInitMode(mDragStartMode);
        controller.setRemoveMode(mRemoveMode);
        return controller;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_list_sort_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_submit) {
            //TODO:DELETE DB OR UPDATE DB
            if (deleteList.size() > 0) {
                AppController.getInstance().getDaofManger().deleteList(deleteList);
            }
            for (int i = 0; i < addressBookList.size(); i++) {
                addressBookList.get(i).setSort(String.valueOf(1000 + i));

            }
            AppController.getInstance().getDaofManger().updateDataForSort(addressBookList);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
