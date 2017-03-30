package com.sstc.shivam.petrolpumplocator.offline.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shiva on 23-03-2017.
 */

public class PrefSingleton {
    public static final String keyOffline = "offlinePref";
    private static PrefSingleton mInstance;
    private Context mContext;
    //
    private SharedPreferences mMyPreferences;

    private PrefSingleton() {
    }

    public static PrefSingleton getInstance() {
        if (mInstance == null) mInstance = new PrefSingleton();
        return mInstance;
    }

    public void Initialize(Context ctxt) {
        mContext = ctxt;
        //
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void writePreference(String key, String value) {
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putString(key, value);
        e.commit();
    }

    public void writePreference(String key, Boolean value) {
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public SharedPreferences getPreference() {

        return mMyPreferences;
    }
}