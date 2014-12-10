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
import com.myaddressbook.R;
import com.myaddressbook.adapter.GroupAdapter;
import com.myaddressbook.app.AppController;

import org.w3c.dom.Text;

import java.util.List;

public class ActSecond extends Activity {
    private GridView mGridView;
    private Button btn_second_group;
    private Button btn_second_people;
    private GroupAdapter mGroupAdapter;
    private List<AddressBook> addressBookList;
    private String mParentNo;
    private TextView mTxt_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_second);
        mParentNo = getIntent().getStringExtra("ParentNo");
        addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(2, mParentNo);

        mGridView = (GridView) findViewById(R.id.gridview);
        btn_second_group = (Button) findViewById(R.id.btn_second_group);
        btn_second_people = (Button) findViewById(R.id.btn_second_people);
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
                Intent intent = new Intent(ActSecond.this, ActThree.class);
                intent.putExtra("ParentNo", addressBookList.get(position).getPeopleNo());
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

        return super.onOptionsItemSelected(item);
    }
}
