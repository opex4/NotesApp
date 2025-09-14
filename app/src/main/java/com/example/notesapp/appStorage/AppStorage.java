package com.example.notesapp.appStorage;

import android.content.SharedPreferences;
import android.content.Context;
import com.example.notesapp.dto.UserDTO;

public class AppStorage {
    private static AppStorage instance;
    private final SharedPreferences prefs;

    private static final String PREFS_NAME = "user_storage";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_JWT_TOKEN = "jwt_token";

    private AppStorage(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new AppStorage(context);
        }
    }

    public static AppStorage getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Call initialize() first!");
        }
        return instance;
    }

    public void saveUser(UserDTO user) {
        if (user != null) {
            prefs.edit()
                    .putInt(KEY_USER_ID, user.getId())
                    .putString(KEY_USER_NAME, user.getName())
                    .putString(KEY_USER_EMAIL, user.getEmail())
                    .apply();
        }
    }

    public void saveJwtToken(String jwtToken) {
        if (jwtToken != null) {
            prefs.edit().putString(KEY_JWT_TOKEN, jwtToken).apply();
        }
    }

    public void saveSession(UserDTO user, String jwtToken) {
        saveUser(user);
        saveJwtToken(jwtToken);
    }

    public String getJwtToken() {
        return prefs.getString(KEY_JWT_TOKEN, null);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    public boolean isJwtTokenExists() {
        return getJwtToken() != null && !getJwtToken().isEmpty();
    }

    public static boolean isInitialized() {
        return instance != null;
    }
}