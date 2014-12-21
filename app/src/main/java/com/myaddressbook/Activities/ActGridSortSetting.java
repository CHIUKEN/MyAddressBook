package com.myaddressbook.Activities;

import android.app.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daogenerator.AddressBook;

import com.dynamicgrid.DynamicGridView;
import com.gc.materialdesign.views.MaterialEditText;
import com.gc.materialdesign.widgets.ColorSelector;
import com.gc.materialdesign.widgets.Dialog;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.myaddressbook.R;
import com.myaddressbook.adapter.DynamicAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class ActGridSortSetting extends Activity {
    private static final String TAG = ActGridSortSetting.class.getName();
    private DynamicGridView gridView;
    private List<AddressBook> addressBookArrayList;
    private int mLevel;
    private String mParentNo;
    DynamicAdapter mDynamicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // Get tracker.
        Tracker t = ((AppController) this.getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(ActGridSortSetting.class.getSimpleName());
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        setContentView(R.layout.activity_act_grid_sort_setting);
        mLevel = getIntent().getIntExtra("Level", -1);
        mParentNo = getIntent().getStringExtra("ParentNo");
        addressBookArrayList = AppController.getInstance().getDaofManger().getAddressBookList(mLevel, mParentNo);

        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        mDynamicAdapter = new DynamicAdapter(this,
                addressBookArrayList,
                getResources().getInteger(R.integer.column_count));
        gridView.setAdapter(mDynamicAdapter);

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
                final AddressBook selectPosition = addressBookArrayList.get(position);
                final MaterialDialog alert = new MaterialDialog(ActGridSortSetting.this);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        ActGridSortSetting.this,
                        android.R.layout.simple_list_item_1
                );
                arrayAdapter.add(getResources().getString(R.string.dialog_item1));
                arrayAdapter.add(getResources().getString(R.string.dialog_item2));
                arrayAdapter.add(getResources().getString(R.string.dialog_item3));
                ListView listView = new ListView(ActGridSortSetting.this);
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (8 * scale + 0.5f);
                listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
                listView.setDividerHeight(0);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            showChangeTitle(selectPosition);
                        } else if (i == 1) {
                            showChangeColor(selectPosition);
                        } else if (i == 2) {
                            //TODO:DELETE GROUP
                            showDelete(selectPosition);
                        }
                        alert.dismiss();
                    }
                });
                alert.setTitle(getResources().getString(R.string.dialog_change_title))
                        .setContentView(listView);


                alert.show();

            }
        });
    }

    private void showChangeTitle(final AddressBook addressBook) {
        final MaterialDialog editDialog = new MaterialDialog(ActGridSortSetting.this)
                .setTitle(R.string.btn_new_group);

        final MaterialEditText editText = new MaterialEditText(ActGridSortSetting.this);

        editText.setHint(R.string.edit_hint_text);
        editText.setSingleLineEllipsis(true);
        editText.setMaxCharacters(10);
        editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);

        editText.setBaseColor(getResources().getColor(R.color.base_color));
        editText.setPrimaryColor(getResources().getColor(R.color.primaryColor));
        editText.setErrorColor(getResources().getColor(R.color.error_color));
        editText.setText(addressBook.getPeopleName());
        editText.setSelection(editText.length());
        editText.setTextSize(18);
        editDialog.setContentView(editText);

        editDialog.setPositiveButton(R.string.alert_submit, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupname = editText.getText().toString().trim();
                if (groupname.length() > 10) {
                    Toast.makeText(ActGridSortSetting.this, R.string.toast_edit_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                //TODO:UPDATE DB
                addressBook.setPeopleName(groupname);
                AppController.getInstance().getDaofManger().updateSingleAddressbook(addressBook);
                mDynamicAdapter.notifyDataSetChanged();
                editDialog.dismiss();
            }
        });

        editDialog.setNegativeButton(R.string.alert_cancal, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog.dismiss();
            }
        });

        editDialog.show();
    }

    private void showChangeColor(final AddressBook addressBook) {

        ColorSelector colorSelector = new ColorSelector(this, Color.parseColor(addressBook.getDisplayColor()), new ColorSelector.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                String hexColor = String.format("#%06X", (0xFFFFFF & color));
                Log.d("==========", hexColor);
                //TODO: UPDATE DB
                addressBook.setDisplayColor(hexColor);
                AppController.getInstance().getDaofManger().updateSingleAddressbook(addressBook);
                mDynamicAdapter.notifyDataSetChanged();
            }
        });
        colorSelector.show();
    }

    private void showDelete(final AddressBook addressBook) {
        Dialog dialog = new Dialog(ActGridSortSetting.this,
                getResources().getString(R.string.dialog_deletegroup_title),
                getResources().getString(R.string.dialog_deletegroup_msg),
                getResources().getString(R.string.dialog_cancel),
                getResources().getString(R.string.dialog_goon));

        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO DELETE GROUP

                AppController.getInstance().getDaofManger().deletedGroup(addressBook);
                Toast.makeText(ActGridSortSetting.this, R.string.toast_delete_group_success, Toast.LENGTH_SHORT).show();
                mDynamicAdapter.remove(addressBook);
            }
        });
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
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
