package com.myaddressbook.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.daogenerator.AddressBook;
import com.myaddressbook.R;
import com.myaddressbook.adapter.GroupAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

public class ActSecond extends Activity {
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_second);
        String parentNo = getIntent().getStringExtra("ParentNo");
        List<AddressBook> addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(2, parentNo);
        gridView = (GridView) findViewById(R.id.gridview);
        GroupAdapter groupAdapter = new GroupAdapter(this, addressBookList);
        gridView.setAdapter(groupAdapter);
        Log.d("", addressBookList.get(0).getPeopleNo());
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
