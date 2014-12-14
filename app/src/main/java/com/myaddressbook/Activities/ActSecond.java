package com.myaddressbook.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.daogenerator.AddressBook;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.MaterialEditText;
import com.myaddressbook.R;
import com.myaddressbook.adapter.GroupAdapter;
import com.myaddressbook.app.AppController;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class ActSecond extends Activity {
    private GridView mGridView;
    private ButtonRectangle btn_second_group;

    private GroupAdapter mGroupAdapter;
    private List<AddressBook> addressBookList;
    private String mParentNo;
    private String mParentName;
    private TextView mTxt_no_data;
    private ButtonFloat btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_second);
        mParentNo = getIntent().getStringExtra("ParentNo");
        mParentName = getIntent().getStringExtra("ParentName");
        getActionBar().setTitle(mParentName);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addressBookList = new ArrayList<AddressBook>();// AppController.getInstance().getDaofManger().getAddressBookList(2, mParentNo);

        mGridView = (GridView) findViewById(R.id.gridview);
        btn_second_group = (ButtonRectangle) findViewById(R.id.btn_second_group);

        mTxt_no_data = (TextView) findViewById(R.id.txt_no_data);


        mGroupAdapter = new GroupAdapter(this, addressBookList);
        mGridView.setAdapter(mGroupAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressBook nowAdd = addressBookList.get(position);
                List<AddressBook> nextAdd = AppController.getInstance().getDaofManger().getAddressBookList(3, nowAdd.getPeopleNo());
                Intent intent = new Intent();
                if (nextAdd.size() > 1) {
                    intent.setClass(ActSecond.this, ActThree.class);
                } else {
                    intent.setClass(ActSecond.this, ActPeopleList.class);
                }
                intent.putExtra("ParentNo", nowAdd.getPeopleNo());
                intent.putExtra("ParentName", nowAdd.getPeopleName());
                intent.putExtra("Level", 2);
                startActivity(intent);
            }
        });

        //新增群組
        btn_second_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog editDialog = new MaterialDialog(ActSecond.this)
                        .setTitle(R.string.btn_new_group);

                final MaterialEditText editText = new MaterialEditText(ActSecond.this);

                editText.setHint(R.string.edit_hint_text);
                editText.setSingleLineEllipsis(true);
                editText.setMaxCharacters(10);
                editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);

                editText.setBaseColor(getResources().getColor(R.color.base_color));
                editText.setPrimaryColor(getResources().getColor(R.color.primaryColor));
                editText.setErrorColor(getResources().getColor(R.color.error_color));

                editText.setTextSize(18);
                editDialog.setContentView(editText);

                editDialog.setPositiveButton(R.string.alert_submit,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String groupname = editText.getText().toString().trim();
                        if (groupname == null || groupname == "") {
                            Toast.makeText(getApplicationContext(), R.string.toast_error_msg, Toast.LENGTH_SHORT).show();
                            editDialog.dismiss();
                            return;
                        }
                        boolean isSuccess = AppController.getInstance().getDaofManger().InsertAdd(groupname, 2, mParentNo);
                        if (isSuccess) {
                            addressBookList.clear();
                            addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(2, mParentNo));
                            mGroupAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), R.string.toast_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.toast_error, Toast.LENGTH_SHORT).show();
                        }
                        editDialog.dismiss();

                    }
                });

                editDialog.setNegativeButton(R.string.alert_cancal,new View.OnClickListener() {
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
        addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(2, mParentNo));
        if (addressBookList.size() <= 1) {
            mTxt_no_data.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
        }
        mGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,ActGridSortSetting.class);
            Bundle bundle = new Bundle();
            bundle.putInt("Level", 2);
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
