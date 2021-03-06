package com.myaddressbook.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daogenerator.AddressBook;
import com.daogenerator.Tag;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.myaddressbook.R;
import com.myaddressbook.app.AppController;

import org.w3c.dom.Text;

import java.util.List;

public class ActPeopleDetail extends Activity implements View.OnClickListener {
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
    private LinearLayout layout_detail_tag1;
    private LinearLayout layout_detail_tag2;
    private LinearLayout layout_detail_group1;
    private LinearLayout layout_detail_group2;
    private LinearLayout layout_detail_group3;
    private View line4;
    private View line5;
    private String groupParent1;
    private String groupParent2;
    private String groupParent3;
    private static final int RESULT_GROUP1 = 1;
    private static final int RESULT_GROUP2 = 2;
    private static final int RESULT_GROUP3 = 3;
    private static final int RESULT_TAG1 = 4;
    private static final int RESULT_TAG2 = 5;
    private boolean isHasNext = false;
    private ImageView img_next_group1;
    private ImageView img_next_group2;
    private ImageView img_next_group3;
    private ImageView img_next_tag1;
    private ImageView img_next_tag2;
    private LinearLayout layoutPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get tracker.
        Tracker t = ((AppController) this.getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(this.getClass().getSimpleName());
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);

        setContentView(R.layout.activity_act_people_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mLevel = getIntent().getIntExtra("Level", -1);
        mAddressbook = (AddressBook) getIntent().getSerializableExtra("AddressBook");


        txt_detail_name = (TextView) findViewById(R.id.txt_detail_name);
        txt_detail_phone = (TextView) findViewById(R.id.txt_detail_phone);
        txt_detail_group1 = (TextView) findViewById(R.id.txt_detail_group1);
        txt_detail_group2 = (TextView) findViewById(R.id.txt_detail_group2);
        txt_detail_group3 = (TextView) findViewById(R.id.txt_detail_group3);
        txt_detail_tag1 = (TextView) findViewById(R.id.txt_detail_tag1);
        txt_detail_tag2 = (TextView) findViewById(R.id.txt_detail_tag2);
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        layout_detail_tag1 = (LinearLayout) findViewById(R.id.layout_detail_tag1);
        layout_detail_tag2 = (LinearLayout) findViewById(R.id.layout_detail_tag2);
        layout_detail_group1 = (LinearLayout) findViewById(R.id.layout_detail_group1);
        layout_detail_group2 = (LinearLayout) findViewById(R.id.layout_detail_group2);
        layout_detail_group3 = (LinearLayout) findViewById(R.id.layout_detail_group3);
        line4 = (View) findViewById(R.id.line4);
        line5 = (View) findViewById(R.id.line5);
        img_next_group1 = (ImageView) findViewById(R.id.img_next_group1);
        img_next_group2 = (ImageView) findViewById(R.id.img_next_group2);
        img_next_group3 = (ImageView) findViewById(R.id.img_next_group3);
        img_next_tag1 = (ImageView) findViewById(R.id.img_next_tag1);
        img_next_tag2 = (ImageView) findViewById(R.id.img_next_tag2);
        layoutPhone = (LinearLayout) findViewById(R.id.layout_call);

        txt_detail_name.setText(mAddressbook.getPeopleName());
        txt_detail_phone.setText(mAddressbook.getPeoplePhone());
        editText_name.setText(mAddressbook.getPeopleName());
        editText_phone.setText(mAddressbook.getPeoplePhone());
        editText_name.setSelection(editText_name.length());
        editText_phone.setSelection(editText_phone.length());

        layoutPhone.setOnClickListener(this);

        initView(mAddressbook);
//        if (mLevel == 1) {
//            AddressBook newAdd = AppController.getInstance().getDaofManger().getParentNameByParentNo(mAddressbook.getParentNo());
//            if (newAdd == null)
//                return;
//            txt_detail_group1.setText(newAdd.getPeopleName());
//            groupParent1 = "";
//            layout_detail_group2.setVisibility(View.GONE);
//            layout_detail_group3.setVisibility(View.GONE);
//            txt_detail_group2.setVisibility(View.GONE);
//            txt_detail_group3.setVisibility(View.GONE);
//            line4.setVisibility(View.GONE);
//            line5.setVisibility(View.GONE);
//        } else if (mLevel == 2) {
//            AddressBook group2 = AppController.getInstance().getDaofManger().getParentNameByParentNo(mAddressbook.getParentNo());
//            AddressBook group1 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group2.getParentNo());
//            if (group1 == null || group2 == null)
//                return;
//            txt_detail_group1.setText(group1.getPeopleName());
//            txt_detail_group2.setText(group2.getPeopleName());
//            groupParent1 = "";
//            groupParent2 = group1.getPeopleNo();
//            layout_detail_group3.setVisibility(View.GONE);
//            txt_detail_group3.setVisibility(View.GONE);
//            line5.setVisibility(View.GONE);
//        } else if (mLevel == 3) {
//            AddressBook group3 = AppController.getInstance().getDaofManger().getParentNameByParentNo(mAddressbook.getParentNo());
//            AddressBook group2 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group3.getParentNo());
//            AddressBook group1 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group2.getParentNo());
//            if (group1 == null || group2 == null || group3 == null)
//                return;
//            groupParent1 = "";
//            groupParent2 = group1.getPeopleNo();
//            groupParent3 = group2.getPeopleNo();
//            txt_detail_group1.setText(group1.getPeopleName());
//            txt_detail_group2.setText(group2.getPeopleName());
//            txt_detail_group3.setText(group3.getPeopleName());
//        }

        if (!mAddressbook.getTag1Name().equals("")) {
            txt_detail_tag1.setText(mAddressbook.getTag1Name());
        }
        if (!mAddressbook.getTag2Name().equals("")) {
            txt_detail_tag2.setText(mAddressbook.getTag2Name());
        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (IsEdit) {

            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_submit).setVisible(true);
            layout_detail_group1.setBackground(getResources().getDrawable(R.drawable.style_list_click));
            layout_detail_group2.setBackground(getResources().getDrawable(R.drawable.style_list_click));
            layout_detail_group3.setBackground(getResources().getDrawable(R.drawable.style_list_click));
            layout_detail_tag1.setBackground(getResources().getDrawable(R.drawable.style_list_click));
            layout_detail_tag2.setBackground(getResources().getDrawable(R.drawable.style_list_click));
            img_next_group1.setVisibility(View.VISIBLE);
            img_next_group2.setVisibility(View.VISIBLE);
            img_next_group3.setVisibility(View.VISIBLE);
            img_next_tag1.setVisibility(View.VISIBLE);
            img_next_tag2.setVisibility(View.VISIBLE);
            layout_detail_group1.setOnClickListener(this);
            layout_detail_group2.setOnClickListener(this);
            layout_detail_group3.setOnClickListener(this);
            layout_detail_tag1.setOnClickListener(this);
            layout_detail_tag2.setOnClickListener(this);
            layoutPhone.setVisibility(View.GONE);
        } else {
            menu.findItem(R.id.action_cancel).setVisible(false);
            menu.findItem(R.id.action_submit).setVisible(false);
            img_next_group1.setVisibility(View.GONE);
            img_next_group2.setVisibility(View.GONE);
            img_next_group3.setVisibility(View.GONE);
            img_next_tag1.setVisibility(View.GONE);
            img_next_tag2.setVisibility(View.GONE);
            layout_detail_group1.setBackground(null);
            layout_detail_group2.setBackground(null);
            layout_detail_group3.setBackground(null);
            layout_detail_tag1.setBackground(null);
            layout_detail_tag2.setBackground(null);
            layout_detail_group1.setOnClickListener(null);
            layout_detail_group2.setOnClickListener(null);
            layout_detail_group3.setOnClickListener(null);
            layout_detail_tag1.setOnClickListener(null);
            layout_detail_tag2.setOnClickListener(null);
            layoutPhone.setVisibility(View.VISIBLE);
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

        int id = item.getItemId();

        if (id == R.id.action_edit) {
            editText_phone.setVisibility(View.VISIBLE);
            editText_name.setVisibility(View.VISIBLE);
            txt_detail_name.setVisibility(View.GONE);
            txt_detail_phone.setVisibility(View.GONE);
            IsEdit = true;
            invalidateOptionsMenu();
            return true;
        }
        if (id == R.id.action_cancel) {
            editText_phone.setVisibility(View.GONE);
            editText_name.setVisibility(View.GONE);
            txt_detail_name.setVisibility(View.VISIBLE);
            txt_detail_phone.setVisibility(View.VISIBLE);
            IsEdit = false;
            //重新取得原本的model
            invalidateOptionsMenu();
            finish();
        }
        if (id == R.id.action_submit) {
            if (isHasNext) {
                Toast.makeText(this, "尚有群組未設定", Toast.LENGTH_SHORT).show();
                return true;
            }
            //TODO:UPDATE DB
            mAddressbook.setPeopleName(editText_name.getText().toString());
            mAddressbook.setPeoplePhone(editText_phone.getText().toString());
            AppController.getInstance().getDaofManger().updateSingleAddressbook(mAddressbook);
            editText_phone.setVisibility(View.GONE);
            editText_name.setVisibility(View.GONE);
            txt_detail_name.setVisibility(View.VISIBLE);
            txt_detail_phone.setVisibility(View.VISIBLE);
            txt_detail_name.setText(editText_name.getText());
            txt_detail_phone.setText(editText_phone.getText());
            IsEdit = false;
            invalidateOptionsMenu();
        }
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView(AddressBook model) {
        if (mLevel == 1) {
            AddressBook parentBook = AppController.getInstance().getDaofManger().getParentNameByParentNo(model.getParentNo());
            if (model == null)
                return;
            txt_detail_group1.setText(parentBook.getPeopleName());
            groupParent1 = "";
            layout_detail_group2.setVisibility(View.GONE);
            layout_detail_group3.setVisibility(View.GONE);
            txt_detail_group2.setVisibility(View.GONE);
            txt_detail_group3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            line5.setVisibility(View.GONE);
        } else if (mLevel == 2) {
            AddressBook group2 = AppController.getInstance().getDaofManger().getParentNameByParentNo(model.getParentNo());
            AddressBook group1 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group2.getParentNo());
            if (group1 == null || group2 == null)
                return;
            txt_detail_group1.setText(group1.getPeopleName());
            txt_detail_group2.setText(group2.getPeopleName());
            groupParent1 = "";
            groupParent2 = group1.getPeopleNo();
            layout_detail_group3.setVisibility(View.GONE);
            txt_detail_group3.setVisibility(View.GONE);
            line5.setVisibility(View.GONE);
        } else if (mLevel == 3) {
            AddressBook group3 = AppController.getInstance().getDaofManger().getParentNameByParentNo(model.getParentNo());
            AddressBook group2 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group3.getParentNo());
            AddressBook group1 = AppController.getInstance().getDaofManger().getParentNameByParentNo(group2.getParentNo());
            if (group1 == null || group2 == null || group3 == null)
                return;
            groupParent1 = "";
            groupParent2 = group1.getPeopleNo();
            groupParent3 = group2.getPeopleNo();
            txt_detail_group1.setText(group1.getPeopleName());
            txt_detail_group2.setText(group2.getPeopleName());
            txt_detail_group3.setText(group3.getPeopleName());
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ActGroupChange.class);
        Bundle bundle = new Bundle();
        int result = 0;
        switch (view.getId()) {
            case R.id.layout_detail_tag1:
                result = RESULT_TAG1;
                intent.putExtra("Title", getResources().getString(R.string.title_activity_act_change_tag));
                break;
            case R.id.layout_detail_tag2:
                result = RESULT_TAG2;
                intent.putExtra("Title", getResources().getString(R.string.title_activity_act_change_tag));
                break;
            case R.id.layout_detail_group1:
                result = RESULT_GROUP1;
                bundle.putInt("Level", 1);
                intent.putExtra("ParentNo", "");
                intent.putExtra("Title", getResources().getString(R.string.title_activity_act_change_group));
                break;
            case R.id.layout_detail_group2:
                result = RESULT_GROUP2;
                bundle.putInt("Level", 2);
                intent.putExtra("ParentNo", groupParent2);
                intent.putExtra("Title", getResources().getString(R.string.title_activity_act_change_group));
                break;
            case R.id.layout_detail_group3:
                result = RESULT_GROUP3;
                bundle.putInt("Level", 3);
                intent.putExtra("ParentNo", groupParent3);
                intent.putExtra("Title", getResources().getString(R.string.title_activity_act_change_group));
                break;
            case R.id.layout_call:
                Intent tel = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + editText_phone.getText()));
                Log.i("phonenumber", editText_phone.getText().toString());
                startActivity(tel);
                return;
        }
        intent.putExtras(bundle);

        startActivityForResult(intent, result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        AddressBook addressBook;
        Tag tag;
        switch (requestCode) {
            case RESULT_GROUP1:
                addressBook = (AddressBook) data.getSerializableExtra("Model");
                txt_detail_group1.setText(addressBook.getPeopleName());
                if (checkHasNext(2, addressBook.getPeopleNo())) {
                    groupParent2 = addressBook.getPeopleNo();
                    txt_detail_group2.setText("請選擇");
                    txt_detail_group2.setVisibility(View.VISIBLE);
                    layout_detail_group2.setVisibility(View.VISIBLE);
                    line4.setVisibility(View.VISIBLE);
                    txt_detail_group3.setText("請選擇");
                    layout_detail_group3.setVisibility(View.GONE);
                    isHasNext = true;
                } else {
                    mAddressbook.setParentNo(addressBook.getPeopleNo());
                    layout_detail_group2.setVisibility(View.GONE);
                    layout_detail_group3.setVisibility(View.GONE);
                    txt_detail_group3.setText("請選擇");
                    txt_detail_group2.setText("請選擇");
                    isHasNext = false;
                }
                break;
            case RESULT_GROUP2:
                addressBook = (AddressBook) data.getSerializableExtra("Model");
                txt_detail_group2.setText(addressBook.getPeopleName());
                if (checkHasNext(3, addressBook.getPeopleNo())) {
                    groupParent3 = addressBook.getPeopleNo();
                    layout_detail_group3.setVisibility(View.VISIBLE);
                    txt_detail_group3.setText("請選擇");
                    txt_detail_group3.setVisibility(View.VISIBLE);
                    line5.setVisibility(View.VISIBLE);
                    isHasNext = true;
                } else {
                    mAddressbook.setParentNo(addressBook.getPeopleNo());
                    layout_detail_group3.setVisibility(View.GONE);
                    txt_detail_group3.setText("請選擇");
                    isHasNext = false;
                }
                break;
            case RESULT_GROUP3:
                addressBook = (AddressBook) data.getSerializableExtra("Model");
                txt_detail_group3.setText(addressBook.getPeopleName());
                mAddressbook.setParentNo(addressBook.getPeopleNo());
                isHasNext = false;
                break;
            case RESULT_TAG1:
                tag = (Tag) data.getSerializableExtra("Model");
                txt_detail_tag1.setText(tag.getTagName());
                mAddressbook.setTag1Name(tag.getTagName());
                mAddressbook.setTagId1(String.valueOf(tag.getId()));
                break;
            case RESULT_TAG2:
                tag = (Tag) data.getSerializableExtra("Model");
                txt_detail_tag2.setText(tag.getTagName());
                mAddressbook.setTag2Name(tag.getTagName());
                mAddressbook.setTagId2(String.valueOf(tag.getId()));

                break;

        }
    }

    private boolean checkHasNext(int level, String peoploNo) {
        List<AddressBook> addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(level, peoploNo);
        if (addressBookList.size() > 1) {
            return true;
        }
        return false;
    }
}
