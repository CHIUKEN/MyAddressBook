package com.myaddressbook.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.myaddressbook.R;
import com.myaddressbook.adapter.GroupAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

public class ActThree extends Activity {
    private GridView gridView;
    private Button btn_three_group;
    private Button btn_three_people;
    private String parentNo;
    private GroupAdapter groupAdapter;
    private List<AddressBook> addressBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_three);
        gridView = (GridView) findViewById(R.id.gridview);
        btn_three_group = (Button) findViewById(R.id.btn_three_group);
        btn_three_people = (Button) findViewById(R.id.btn_three_peole);
        parentNo = getIntent().getStringExtra("ParentNo");
        addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(3, parentNo);
        groupAdapter = new GroupAdapter(this, addressBookList);
        gridView.setAdapter(groupAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                        boolean isSuccess = AppController.getInstance().getDaofManger().InsertAdd(groupname, 3, parentNo);
                        if (isSuccess) {
                            addressBookList.clear();
                            addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(3, parentNo));
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

        return super.onOptionsItemSelected(item);
    }
}
