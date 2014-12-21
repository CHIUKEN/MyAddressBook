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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.daogenerator.AddressBook;
import com.daogenerator.Tag;
import com.dynamicgrid.DynamicGridView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.MaterialEditText;
import com.gc.materialdesign.widgets.ColorSelector;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.myaddressbook.R;

import com.myaddressbook.adapter.TagAdapter;
import com.myaddressbook.adapter.TagDynamicGridAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class ActGridTagSortSetting extends Activity {
    private static final String TAG = ActGridSortSetting.class.getName();
    private DynamicGridView mGridView;
    private List<Tag> tagList;

    TagDynamicGridAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get tracker.
        Tracker t = ((AppController) this.getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(ActGridTagSortSetting.class.getSimpleName());
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);

        setContentView(R.layout.activity_act_grid_tag_sort_setting);
        mGridView = (DynamicGridView) findViewById(R.id.dynamic_grid);

        tagList = AppController.getInstance().getDaofManger().getAllTag();
        tagAdapter = new TagDynamicGridAdapter(this, tagList, 3);
        mGridView.setAdapter(tagAdapter);
        mGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));

                Tag oldBook = tagList.get(oldPosition);
                tagList.remove(oldPosition);
                tagList.add(newPosition, oldBook);

            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mGridView.startEditMode(position);
                return true;
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Tag selectPosition = tagList.get(position);
                final MaterialDialog alert = new MaterialDialog(ActGridTagSortSetting.this);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        ActGridTagSortSetting.this,
                        android.R.layout.simple_list_item_1
                );
                arrayAdapter.add(getResources().getString(R.string.dialog_item1));
                arrayAdapter.add(getResources().getString(R.string.dialog_item2));
                ListView listView = new ListView(ActGridTagSortSetting.this);
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

    private void showChangeTitle(final Tag tag) {
        final MaterialDialog editDialog = new MaterialDialog(ActGridTagSortSetting.this)
                .setTitle(R.string.btn_new_group);

        final MaterialEditText editText = new MaterialEditText(ActGridTagSortSetting.this);

        editText.setHint(R.string.edit_hint_text);
        editText.setSingleLineEllipsis(true);
        editText.setMaxCharacters(10);
        editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);

        editText.setBaseColor(getResources().getColor(R.color.base_color));
        editText.setPrimaryColor(getResources().getColor(R.color.primaryColor));
        editText.setErrorColor(getResources().getColor(R.color.error_color));
        editText.setText(tag.getTagName());
        editText.setSelection(editText.length());
        editText.setTextSize(18);
        editDialog.setContentView(editText);

        editDialog.setPositiveButton(R.string.alert_submit, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupname = editText.getText().toString().trim();
                if (groupname.length() > 10) {
                    Toast.makeText(ActGridTagSortSetting.this, R.string.toast_edit_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                //TODO:UPDATE DB
                tag.setTagName(groupname);
                AppController.getInstance().getDaofManger().updateSingleTag(tag);
                tagAdapter.notifyDataSetChanged();
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

    private void showChangeColor(final Tag tag) {

        ColorSelector colorSelector = new ColorSelector(this, Color.parseColor(tag.getTagDisplayColor()), new ColorSelector.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                String hexColor = String.format("#%06X", (0xFFFFFF & color));
                Log.d("==========", hexColor);
                //TODO: UPDATE DB
                tag.setTagDisplayColor(hexColor);
                AppController.getInstance().getDaofManger().updateSingleTag(tag);
                tagAdapter.notifyDataSetChanged();
            }
        });
        colorSelector.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_grid_tag_sort_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit) {
            //TODO:UPDATE DB
            mGridView.stopEditMode();
            for (int i = 0; i < tagList.size(); i++) {
                tagList.get(i).setSort(String.valueOf(1000 + i));

            }
            AppController.getInstance().getDaofManger().updateTagSort(tagList);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
