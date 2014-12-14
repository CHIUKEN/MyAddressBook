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

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daogenerator.AddressBook;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.Dialog;

import com.myaddressbook.R;
import com.myaddressbook.adapter.PeopleListAdapter;
import com.myaddressbook.app.AppController;

import java.util.ArrayList;
import java.util.List;

public class ActPeopleList extends Activity {
    private int mLevel;
    private String mParentNo;
    private String mParentName;
    private ListView mlistView;
    private ButtonFloat btn_newpeople;
    private ButtonRectangle btn_newgroup;
    private TextView mTxt_no_data;
    private List<AddressBook> addressBookList;
    private PeopleListAdapter mPeopleListAdapter;

    private String noClassify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_people_list);
        mLevel = getIntent().getIntExtra("Level", -1);
        mParentNo = getIntent().getStringExtra("ParentNo");
        mParentName = getIntent().getStringExtra("ParentName");

        getActionBar().setTitle(mParentName);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mlistView = (ListView) findViewById(R.id.listView_people);
        btn_newpeople = (ButtonFloat) findViewById(R.id.btn_newpeople);
        btn_newgroup = (ButtonRectangle) findViewById(R.id.btn_newgroup);
        mTxt_no_data = (TextView) findViewById(R.id.txt_no_data);
        //層級最後一層或由第一次未分類
        if (mLevel == 3 || mParentNo == "1000000000") {
            btn_newgroup.setVisibility(View.GONE);
        } else {

            noClassify = AppController.getInstance().getDaofManger().getAddressBookList(mLevel + 1, mParentNo).get(0).getPeopleNo();
        }
        addressBookList = new ArrayList<AddressBook>();// AppController.getInstance().getDaofManger().getAddressBookList(4, mParentNo);
        mPeopleListAdapter = new PeopleListAdapter(this, addressBookList);
        mlistView.setAdapter(mPeopleListAdapter);


        //明細頁
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressBook addressBook = addressBookList.get(i);
                Intent intent = new Intent();
                intent.setClass(ActPeopleList.this, ActPeopleDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Level", mLevel);
                intent.putExtra("AddressBook", addressBook);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //新增群組
        btn_newgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ActPeopleList.this,
                        getResources().getString(R.string.dialog_title),
                        getResources().getString(R.string.dialog_msg),
                        getResources().getString(R.string.dialog_cancel),
                        getResources().getString(R.string.dialog_goon));

                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ShowCreateInsertGroup();
                    }
                });
                dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });
                dialog.show();

            }
        });
        //新增聯絡人
        btn_newpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActPeopleList.this, ActCreatePeople.class);
                Bundle bundle = new Bundle();
                //傳目前所在的層級
                bundle.putInt("Level", mLevel);
                intent.putExtra("ParentNo", mParentNo);
                intent.putExtra("ParentName", mParentName);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });


    }

    private void ShowCreateInsertGroup() {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(ActPeopleList.this);
        editDialog.setTitle(R.string.btn_new_group);

        final EditText editText = new EditText(ActPeopleList.this);
        editText.setHint(R.string.edit_hint_text);
        editDialog.setView(editText);

        editDialog.setPositiveButton(R.string.alert_submit, new DialogInterface.OnClickListener() {
            // insert group to db
            public void onClick(DialogInterface arg0, int arg1) {
                String groupname = editText.getText().toString().trim();
                boolean isSuccess = AppController.getInstance().getDaofManger().InsertAdd(groupname, mLevel + 1, mParentNo);
                if (isSuccess) {
                    //TODO:更改資料
                    AppController.getInstance().getDaofManger().updateDataByCreateNewGroup(addressBookList, noClassify);
                    //TODO:移至群組頁
                    Intent intent = new Intent();
                    if (mLevel + 1 == 2) {
                        intent.setClass(ActPeopleList.this, ActSecond.class);
                    } else if (mLevel + 1 == 3) {
                        intent.setClass(ActPeopleList.this, ActThree.class);
                    }
                    intent.putExtra("ParentNo", mParentNo);
                    intent.putExtra("ParentName", mParentName);
                    intent.putExtra("Level", mLevel);
                    startActivity(intent);
                    finish();

                    Toast.makeText(getApplicationContext(), R.string.toast_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.toast_error, Toast.LENGTH_SHORT).show();
                }

            }
        });
        editDialog.setNegativeButton(R.string.alert_cancal, new DialogInterface.OnClickListener() {
            // cancel
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        editDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addressBookList.clear();
        addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(4, mParentNo));
        if (addressBookList.size() <= 0) {
            mlistView.setVisibility(View.GONE);
            mTxt_no_data.setVisibility(View.VISIBLE);
        } else {
            mTxt_no_data.setVisibility(View.GONE);
        }
        mPeopleListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (addressBookList == null) {
            addressBookList = new ArrayList<AddressBook>();
        }
        addressBookList.clear();
        addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(4, mParentNo);
        if (addressBookList.size() > 0) {
            mTxt_no_data.setVisibility(View.GONE);
            mlistView.setVisibility(View.VISIBLE);
        }
        mPeopleListAdapter.setAddressBookList(addressBookList);
        mPeopleListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_people_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //設定排序
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ActListSortSetting.class);
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
