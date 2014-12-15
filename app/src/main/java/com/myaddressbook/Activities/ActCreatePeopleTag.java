package com.myaddressbook.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.daogenerator.AddressBook;
import com.myaddressbook.R;
import com.myaddressbook.adapter.AddTagAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

public class ActCreatePeopleTag extends Activity {
    private ListView mListview;
    private List<AddressBook> addressBookList;
    private AddTagAdapter addTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_create_people_tag);
        mListview = (ListView) findViewById(R.id.listView_tag_create);
        addressBookList = AppController.getInstance().getDaofManger().getall();
        addTagAdapter = new AddTagAdapter(this, addressBookList);
        mListview.setAdapter(addTagAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_create_people_tag, menu);
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
