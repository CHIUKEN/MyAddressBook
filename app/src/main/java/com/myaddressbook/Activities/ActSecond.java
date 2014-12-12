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
import com.gc.materialdesign.views.ButtonRectangle;
import com.myaddressbook.R;
import com.myaddressbook.adapter.GroupAdapter;
import com.myaddressbook.app.AppController;

import org.w3c.dom.Text;

import java.util.List;

public class ActSecond extends Activity {
    private GridView mGridView;
    private ButtonRectangle btn_second_group;
    private ButtonRectangle btn_second_people;
    private GroupAdapter mGroupAdapter;
    private List<AddressBook> addressBookList;
    private String mParentNo;
    private String mParentName;
    private TextView mTxt_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_second);
        mParentNo = getIntent().getStringExtra("ParentNo");
        mParentName = getIntent().getStringExtra("ParentName");
        getActionBar().setTitle(mParentName);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(2, mParentNo);

        mGridView = (GridView) findViewById(R.id.gridview);
        btn_second_group = (ButtonRectangle) findViewById(R.id.btn_second_group);
        btn_second_people = (ButtonRectangle) findViewById(R.id.btn_second_people);
        mTxt_no_data = (TextView) findViewById(R.id.txt_no_data);

        mGroupAdapter = new GroupAdapter(this, addressBookList);
        mGridView.setAdapter(mGroupAdapter);
        Log.d("", addressBookList.get(0).getPeopleNo());

        if (addressBookList.size() <= 1) {
            mTxt_no_data.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
        }

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
                AlertDialog.Builder editDialog = new AlertDialog.Builder(ActSecond.this);
                editDialog.setTitle(R.string.btn_new_group);

                final EditText editText = new EditText(ActSecond.this);
                editText.setHint(R.string.edit_hint_text);
                editDialog.setView(editText);

                editDialog.setPositiveButton(R.string.alert_submit, new DialogInterface.OnClickListener() {
                    // insert group to db
                    public void onClick(DialogInterface arg0, int arg1) {
                        String groupname = editText.getText().toString().trim();
                        if (groupname == null || groupname == "") {
                            Toast.makeText(getApplicationContext(), R.string.toast_error_msg, Toast.LENGTH_SHORT).show();
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

                    }
                });
                editDialog.setNegativeButton(R.string.alert_cancal, new DialogInterface.OnClickListener() {
                    // cancel
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                editDialog.show();
            }
        });
        //新增聯絡人
        btn_second_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActSecond.this, ActCreatePeople.class);
                Bundle bundle = new Bundle();
                //傳目前所在的層級
                bundle.putInt("Level", 2);

                intent.putExtra("ParentNo", mParentNo);
                intent.putExtra("ParentName", mParentName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_second, menu);
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
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
