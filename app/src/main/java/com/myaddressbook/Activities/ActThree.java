package com.myaddressbook.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.daogenerator.AddressBook;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.MaterialEditText;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.myaddressbook.R;
import com.myaddressbook.adapter.GroupAdapter;
import com.myaddressbook.app.AppController;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class ActThree extends Activity {
    private GridView gridView;
    private ButtonRectangle btn_three_group;


    private GroupAdapter groupAdapter;
    private List<AddressBook> addressBookList;
    private String mParentNo;
    private String mParentName;

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

        setContentView(R.layout.activity_act_three);
        gridView = (GridView) findViewById(R.id.gridview);
        btn_three_group = (ButtonRectangle) findViewById(R.id.btn_three_group);

        mParentNo = getIntent().getStringExtra("ParentNo");
        mParentName = getIntent().getStringExtra("ParentName");
        getActionBar().setTitle(mParentName);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addressBookList = new ArrayList<AddressBook>();// AppController.getInstance().getDaofManger().getAddressBookList(3, mParentNo);
        groupAdapter = new GroupAdapter(this, addressBookList);
        gridView.setAdapter(groupAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressBook addressBook = addressBookList.get(position);
                Intent intent = new Intent(ActThree.this, ActPeopleList.class);
                intent.putExtra("ParentNo", addressBook.getPeopleNo());
                intent.putExtra("ParentName", addressBook.getPeopleName());
                intent.putExtra("Level", 3);
                startActivity(intent);
            }
        });
        //新增群組
        btn_three_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog editDialog = new MaterialDialog(ActThree.this)
                        .setTitle(R.string.btn_new_group);

                final MaterialEditText editText = new MaterialEditText(ActThree.this);

                editText.setHint(R.string.edit_hint_text);
                editText.setSingleLineEllipsis(true);
                editText.setMaxCharacters(10);
                editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);

                editText.setBaseColor(getResources().getColor(R.color.base_color));
                editText.setPrimaryColor(getResources().getColor(R.color.primaryColor));
                editText.setErrorColor(getResources().getColor(R.color.error_color));

                editText.setTextSize(18);
                editDialog.setContentView(editText);

                editDialog.setPositiveButton(R.string.alert_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String groupname = editText.getText().toString().trim();
                        if (groupname == null || groupname == "") {
                            Toast.makeText(getApplicationContext(), R.string.toast_error_msg, Toast.LENGTH_SHORT).show();
                            editDialog.dismiss();
                            return;
                        }
                        boolean isSuccess = AppController.getInstance().getDaofManger().InsertAdd(groupname, 3, mParentNo);
                        if (isSuccess) {
                            addressBookList.clear();
                            addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(3, mParentNo));
                            groupAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), R.string.toast_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.toast_error, Toast.LENGTH_SHORT).show();
                        }
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
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        addressBookList.clear();
        addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(3, mParentNo));
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_three, menu);
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
            Intent intent = new Intent(this, ActGridSortSetting.class);
            Bundle bundle = new Bundle();
            bundle.putInt("Level", 3);
            intent.putExtra("ParentNo", mParentNo);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
