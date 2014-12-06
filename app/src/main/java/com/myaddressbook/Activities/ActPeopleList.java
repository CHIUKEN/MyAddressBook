package com.myaddressbook.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.myaddressbook.R;

public class ActPeopleList extends Activity {
    private String Level;
    private ListView mlistView;
    private Button btn_newpeople;
    private Button btn_newgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_people_list);
        Level = getIntent().getStringExtra("Level");
        mlistView = (ListView) findViewById(R.id.listView_people);
        btn_newpeople = (Button) findViewById(R.id.btn_newpeople);
        btn_newgroup = (Button) findViewById(R.id.btn_newgroup);
        if (Level == null) {
            btn_newgroup.setVisibility(View.GONE);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
