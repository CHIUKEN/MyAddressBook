package com.myaddressbook.Activities;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ColorSelector;
import com.gc.materialdesign.widgets.Dialog;
import com.myaddressbook.app.AppController;
import com.myaddressbook.fragments.AboutFragment;
import com.myaddressbook.fragments.GroupFragment;
import com.myaddressbook.fragments.TagFragment;

import com.myaddressbook.fragments.NavigationDrawerFragment;
import com.myaddressbook.R;

public class ActHome extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        switch (position) {
            case 0:
                //select db group data
                fragment = GroupFragment.newInstance(1);
                break;
            case 1:
                //select db tag data
                fragment = TagFragment.newInstance(2);
                break;
            case 2:
                fragment = AboutFragment.newInstance("", "");
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.act_home, menu);
            restoreActionBar();
            return true;
        }
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_setting_group) {
            Intent intent = new Intent(this, ActGridSortSetting.class);
            Bundle bundle = new Bundle();
            bundle.putInt("Level", 1);
            intent.putExtra("ParentNo", "");
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_setting_tag) {
            Intent intent = new Intent(this, ActGridTagSortSetting.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.search) {
            Intent intent = new Intent(this, ActSearch.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_reset) {
            Dialog dialog = new Dialog(ActHome.this,
                    getResources().getString(R.string.dialog_reset_title),
                    getResources().getString(R.string.dialog_reset_msg),
                    getResources().getString(R.string.dialog_cancel),
                    getResources().getString(R.string.dialog_goon));

            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                   //TODO RESET DB
                    AppController.getInstance().getDaofManger().deleteAll();
                    Fragment fragment = GroupFragment.newInstance(1);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                    Toast.makeText(ActHome.this,R.string.toast_reset_success,Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
