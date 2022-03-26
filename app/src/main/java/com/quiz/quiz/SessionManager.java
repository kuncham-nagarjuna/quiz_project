package com.quiz.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "quiz";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = "name";
    public static final String KEY_COUNTRY_CODE = "country_code";
    public static final String KEY_PHONE = "phone";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String country_code, String phone) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_COUNTRY_CODE, country_code);
        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }

    public void updateNameSession(String name) {

        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public String get_phone() {
        return pref.getString(KEY_PHONE, "phone");
    }

    public String get_country_code() {
        return pref.getString(KEY_COUNTRY_CODE, "");
    }

    public String get_name() {
        return pref.getString(KEY_NAME, "");
    }

    public void setStringPreference(String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public String getStringPreference(String key) {
        String value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    public void clearPreference(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            Editor editor = preferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public void clear() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        if (preferences != null) {
            Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    public boolean isLoggedin() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, ""));
        user.put(KEY_COUNTRY_CODE, pref.getString(KEY_COUNTRY_CODE, ""));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, ""));
        return user;
    }

    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, WelcomeScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
}