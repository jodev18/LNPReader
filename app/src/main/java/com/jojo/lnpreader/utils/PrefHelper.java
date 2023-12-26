package com.jojo.lnpreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefHelper {

    private Context ct;
    private String COMMON_PREF_NAME = "lnpreader";

    public PrefHelper(Context ctx){
        this.ct = ctx;
    }

    public void saveCurrentURL(String currentPageURL){
        // Get the SharedPreferences object
        SharedPreferences sharedPreferences = this.ct
                .getSharedPreferences(this.COMMON_PREF_NAME, Context.MODE_PRIVATE);

        // Get the editor to edit SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put the data into SharedPreferences with a key
        editor.putString("curr_page", currentPageURL);

        // Apply the changes
        editor.apply();
        editor.commit();
    }

    public String getSavedURL(){
        SharedPreferences sharedPreferences = this.ct
                .getSharedPreferences(this.COMMON_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString("curr_page",null);
    }

}
