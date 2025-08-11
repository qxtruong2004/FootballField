package com.example.football_field_management.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void login(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean("is_logged_in", true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }

    public String getSession() {
        return sharedPreferences.getString(KEY_USERNAME, "Guest");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
