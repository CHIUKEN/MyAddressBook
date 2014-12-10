package com.myaddressbook.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.daogenerator.AddressBook;
import com.myaddressbook.R;
import com.myaddressbook.adapter.PeopleListAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

public class ActPeopleList extends Activity {
    private int mLevel;
    private String mParentNo;
    private String mParentName;
    private ListView mlistView;
    private Button btn_newpeople;
    private Button btn_newgroup;
    private TextView mTxt_no_data;

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
        btn_newpeople = (Button) findViewById(R.id.btn_newpeople);
        btn_newgroup = (Button) findViewById(R.id.btn_newgroup);
        mTxt_no_data = (TextView) findViewById(R.id.txt_no_data);

        if (mLevel == 3) {
            btn_newgroup.setVisibility(View.GONE);
        }
        List<AddressBook> addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(4, mParentNo);
        PeopleListAdapter peopleListAdapter = new PeopleListAdapter(this, addressBookList);
        mlistView.setAdapter(peopleListAdapter);

        if (addressBookList.size() <= 0) {
            mlistView.setVisibility(View.GONE);
            mTxt_no_data.setVisibility(View.VISIBLE);
        }

        //明細頁
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
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
