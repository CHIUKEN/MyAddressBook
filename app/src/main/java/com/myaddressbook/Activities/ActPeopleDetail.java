package com.myaddressbook.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daogenerator.AddressBook;
import com.myaddressbook.R;
import com.myaddressbook.app.AppController;

import org.w3c.dom.Text;

public class ActPeopleDetail extends Activity {
    private AddressBook mAddressbook;
    private int mLevel;
    private TextView txt_detail_name;
    private TextView txt_detail_phone;
    private TextView txt_detail_group1;
    private TextView txt_detail_group2;
    private TextView txt_detail_group3;
    private TextView txt_detail_tag1;
    private TextView txt_detail_tag2;
    private EditText editText_name;
    private EditText editText_phone;
    private boolean IsEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_people_detail);
        mLevel = getIntent().getIntExtra("Level", -1);
        //mAddressbook = (AddressBook) getIntent().getExtras().getSerializable("AddressBook");
        txt_detail_name = (TextView) findViewById(R.id.txt_detail_name);
        txt_detail_phone = (TextView) findViewById(R.id.txt_detail_phone);
        txt_detail_group1 = (TextView) findViewById(R.id.txt_detail_group1);
        txt_detail_group2 = (TextView) findViewById(R.id.txt_detail_group2);
        txt_detail_group3 = (TextView) findViewById(R.id.txt_detail_group3);
        txt_detail_tag1 = (TextView) findViewById(R.id.txt_detail_tag1);
        txt_detail_tag2 = (TextView) findViewById(R.id.txt_detail_tag2);
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        if(mAddressbook==null)
        {
            mAddressbook=new AddressBook();
            mAddressbook.setPeopleName("test1");
            mAddressbook.setPeoplePhone("111111");
        }
        txt_detail_name.setText(mAddressbook.getPeopleName());
        txt_detail_phone.setText(mAddressbook.getPeoplePhone());
        if (mLevel == 1) {
            AddressBook newAdd = AppController.getInstance().getDaofManger().getParentNameByParentNo(mAddressbook.getParentNo());
            if (newAdd == null)
                return;
            txt_detail_group1.setText(newAdd.getPeopleName());
            txt_detail_group2.setVisibility(View.GONE);
            txt_detail_group3.setVisibility(View.GONE);
        } else if (mLevel == 2) {
            AddressBook group2 = AppController.getInstance().getDaofManger().getParentNameByParentNo(mAddressbook.getParentNo());
            AddressBook group1 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group2.getParentNo());
            if (group1 == null || group2 == null)
                return;
            txt_detail_group1.setText(group1.getPeopleName());
            txt_detail_group2.setText(group2.getPeopleName());
            txt_detail_group3.setVisibility(View.GONE);
        } else if (mLevel == 3) {
            AddressBook group3 = AppController.getInstance().getDaofManger().getParentNameByParentNo(mAddressbook.getParentNo());
            AddressBook group2 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group3.getParentNo());
            AddressBook group1 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group2.getParentNo());
            if (group1 == null || group2 == null || group3 == null)
                return;
            txt_detail_group1.setText(group1.getPeopleName());
            txt_detail_group2.setText(group2.getPeopleName());
            txt_detail_group3.setText(group3.getPeopleName());
        }

        txt_detail_tag1.setText(mAddressbook.getTag1Name());
        txt_detail_tag2.setText(mAddressbook.getTag2Name());


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(IsEdit) {

            menu.findItem(R.id.action_edit).setVisible(false);
        }else{
            menu.findItem(R.id.action_cancel).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_people_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            editText_phone.setVisibility(View.VISIBLE);
            editText_name.setVisibility(View.VISIBLE);
            txt_detail_name.setVisibility(View.GONE);
            txt_detail_phone.setVisibility(View.GONE);
            IsEdit = true;
            invalidateOptionsMenu();
            return true;
        }
        if(id==R.id.action_cancel){
            editText_phone.setVisibility(View.GONE);
            editText_name.setVisibility(View.GONE);
            txt_detail_name.setVisibility(View.VISIBLE);
            txt_detail_phone.setVisibility(View.VISIBLE);
            IsEdit = false;
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }
}
