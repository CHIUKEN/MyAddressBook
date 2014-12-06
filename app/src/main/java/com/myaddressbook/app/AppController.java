package com.myaddressbook.app;

import android.app.Application;

import com.myaddressbook.util.DaoManager;
import com.myaddressbook.util.PrefManager;

/**
 * Created by K on 2014/12/6.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private PrefManager pref;
    private DaoManager daof;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        pref = new PrefManager(this);
        daof = new DaoManager(this);
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
}
