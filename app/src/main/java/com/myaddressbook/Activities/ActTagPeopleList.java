package com.myaddressbook.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.daogenerator.AddressBook;
import com.daogenerator.Tag;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFloat;
import com.myaddressbook.R;
import com.myaddressbook.adapter.PeopleListAdapter;
import com.myaddressbook.app.AppController;

import java.util.ArrayList;
import java.util.List;

public class ActTagPeopleList extends Activity {
    private ListView mListview;
    private ButtonFloat mBtn_newpeople;
    private TextView mTxt_noData;
    private List<AddressBook> addressBookList;
    private String tagId;
    private PeopleListAdapter mPeopleListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_tag_people_list);
        mListview = (ListView) findViewById(R.id.listView_people);
        mBtn_newpeople = (ButtonFloat) findViewById(R.id.btn_newpeople);
        mTxt_noData = (TextView) findViewById(R.id.txt_no_data);

        tagId = getIntent().getStringExtra("TagId");

        addressBookList = new ArrayList<AddressBook>();
        mPeopleListAdapter = new PeopleListAdapter(this, addressBookList);
        mListview.setAdapter(mPeopleListAdapter);

        mBtn_newpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActTagPeopleList.this, ActCreatePeopleTag.class);

                startActivityForResult(intent, 2);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        addressBookList.addAll(AppController.getInstance().getDaofManger().getAddressBookByTag(tagId));
        if (addressBookList.size() <= 0) {
            mListview.setVisibility(View.GONE);
            mTxt_noData.setVisibility(View.VISIBLE);
        } else {
            mTxt_noData.setVisibility(View.GONE);
        }
        mPeopleListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_tag_people_list, menu);
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
