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
import com.myaddressbook.R;
import com.myaddressbook.adapter.GroupAdapter;
import com.myaddressbook.app.AppController;

import java.util.ArrayList;
import java.util.List;

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
        setContentView(R.layout.activity_act_three);
        gridView = (GridView) findViewById(R.id.gridview);
        btn_three_group = (ButtonRectangle) findViewById(R.id.btn_three_group);

        mParentNo = getIntent().getStringExtra("ParentNo");
        mParentName = getIntent().getStringExtra("ParentName");
        getActionBar().setTitle(mParentName);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addressBookList =new ArrayList<AddressBook>();// AppController.getInstance().getDaofManger().getAddressBookList(3, mParentNo);
        groupAdapter = new GroupAdapter(this, addressBookList);
        gridView.setAdapter(groupAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressBook addressBook=addressBookList.get(position);
                Intent intent=new Intent(ActThree.this,ActPeopleList.class);
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
                AlertDialog.Builder editDialog = new AlertDialog.Builder(ActThree.this);
                editDialog.setTitle(R.string.btn_new_group);

                final EditText editText = new EditText(ActThree.this);
                editText.setHint(R.string.edit_hint_text);
                editDialog.setView(editText);

                editDialog.setPositiveButton(R.string.alert_submit, new DialogInterface.OnClickListener() {
                    // insert group to db
                    public void onClick(DialogInterface arg0, int arg1) {
                        String groupname = editText.getText().toString().trim();
                        boolean isSuccess = AppController.getInstance().getDaofManger().InsertAdd(groupname, 3, mParentNo);
                        if (isSuccess) {
                            addressBookList.clear();
                            addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(3, mParentNo));
                            groupAdapter.notifyDataSetChanged();
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
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
