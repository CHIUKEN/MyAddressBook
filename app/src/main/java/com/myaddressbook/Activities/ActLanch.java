package com.myaddressbook.Activities;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.daogenerator.AddressBook;

import com.myaddressbook.R;
import com.myaddressbook.app.AppController;
import com.myaddressbook.util.DaoManager;

import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class ActLanch extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_act_lanch);
        getActionBar().hide();
        //新增預設資料夾
        //家庭
        AddressBook addressBook = new AddressBook(null, "10000000", "家庭", "1", "", "家庭", "", "", "", "", "", "", "", "","#FFFFFF" ,new Date());
        //工作
        AddressBook addressBook2 = new AddressBook(null, "20000000", "工作", "1", "", "工作", "", "", "", "", "", "", "", "","#FFFF77", new Date());

        DaoManager daof= AppController.getInstance().getDaofManger();
        //insert
        daof.InsertAddressBook(addressBook);
        daof.InsertAddressBook(addressBook2);

        List<AddressBook>addressBookList = daof.getAddressBookList("1");
        for (AddressBook n : addressBookList) {
            Log.d("Level=1:", n.getPeopleName());

        }
        Intent intent = new Intent();
        intent.setClass(this,ActHome.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_lanch, menu);
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
