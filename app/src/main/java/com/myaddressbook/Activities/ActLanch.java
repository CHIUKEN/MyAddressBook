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
import com.myaddressbook.util.PrefManager;

import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class ActLanch extends Activity {
    private PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_act_lanch);
        getActionBar().hide();
        pref = new PrefManager(getApplicationContext());
        if(!pref.getGoogleUserName().equals("first")) {
            //新增預設資料夾
            DaoManager daof = AppController.getInstance().getDaofManger();

            AddressBook addressBook0 = new AddressBook(null, "1000000000", "未分類", 1, "", "", "", "", "", "", "", "", "", "", "#770077", "1000", new Date());
            AddressBook addressBook1 = new AddressBook(null, "1010000000", "未分類", 2, "1000000000", "", "", "", "", "", "", "", "", "", "#770077", "1000", new Date());
            AddressBook addressBook2 = new AddressBook(null, "1010010000", "未分類", 3, "1010000000", "", "", "", "", "", "", "", "", "", "#770077", "1000", new Date());
            daof.InsertAddressBook(addressBook0);
            daof.InsertAddressBook(addressBook1);
            daof.InsertAddressBook(addressBook2);

            //insert
            daof.InsertAdd("家庭", 1, "");
            daof.InsertAdd("工作", 1, "");
            pref.setGoogleUsername("first");
        }else{
           Log.d("","====not first===");
        }
//        List<AddressBook>ALL = daof.getall();

//        for (AddressBook n : ALL) {
//            Log.d("db all data:","getPeopleNo():"+n.getPeopleNo()+"getLevelNum():"+ n.getLevelNum()+"getParentNo"+n.getParentNo());
//
//        }
        Intent intent = new Intent();
        intent.setClass(this, ActHome.class);
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
