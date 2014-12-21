package com.myaddressbook.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//import com.google.analytics.tracking.android.GAServiceManager;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.Logger;
//import com.google.analytics.tracking.android.Tracker;
import com.google.analytics.tracking.android.Logger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.myaddressbook.Model.Contacts;

import com.myaddressbook.util.DaoManager;
import com.myaddressbook.util.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by K on 2014/12/6.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private PrefManager pref;
    private DaoManager daof;
    private List<Contacts> contactsList;


    /*
     * Google Analytics configuration values.
     */
    // Placeholder property ID.
    private static final String GA_PROPERTY_ID = "UA-55740021-2";

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(GA_PROPERTY_ID);
            mTrackers.put(trackerId, t);

        }
        return  mTrackers.get(trackerId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        pref = new PrefManager(this);
        daof = new DaoManager(this);
        contactsList = new ArrayList<Contacts>();
//        initializeGa();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public PrefManager getPrefManger() {
        if (pref == null) {
            pref = new PrefManager(this);
        }

        return pref;
    }

    public DaoManager getDaofManger() {
        if (daof == null) {
            daof = new DaoManager(this);
        }
        return daof;
    }
    //取得選取項目
    public List<Contacts> getContactsList() {
        if (contactsList == null) {
            contactsList = new ArrayList<Contacts>();
        }
        return contactsList;
    }
    //清空選取項目
    public void clearSelectContacts() {
        if (contactsList == null) {
            contactsList = new ArrayList<Contacts>();
        }
        contactsList.clear();
    }
}
