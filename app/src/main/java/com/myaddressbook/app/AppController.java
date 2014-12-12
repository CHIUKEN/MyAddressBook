package com.myaddressbook.app;

import android.app.Application;

import com.myaddressbook.Model.Contacts;
import com.myaddressbook.util.DaoManager;
import com.myaddressbook.util.PrefManager;

import java.util.ArrayList;
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

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        pref = new PrefManager(this);
        daof = new DaoManager(this);
        contactsList = new ArrayList<Contacts>();
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
