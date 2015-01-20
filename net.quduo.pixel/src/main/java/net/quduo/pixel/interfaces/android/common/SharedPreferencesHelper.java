package net.quduo.pixel.interfaces.android.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    public static final String DEFAULT_SHARED_PREFERENCES_NAME = "shared_preferences";

    public static final String KEYBOARD_HEIGHT_KEY = "keyboard_height";

    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(DEFAULT_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences;
    }

    public static SharedPreferences getSharedPreferences(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences;
    }

    public static SharedPreferences getSharedPreferences(Context context, String name, int mode) {
        SharedPreferences preferences = context.getSharedPreferences(name, mode);
        return preferences;
    }

    /*
    public static boolean contains(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static String getString(Context context, String key, final String defaultValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultValue);
    }

    public static void putString(Context context, final String key, final String value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(key, value).commit();
    }

    public static boolean getBoolean(Context context, final String key, final boolean defaultValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, final String key, final boolean value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(key, value).commit();
    }

    public static void putInt(Context context, final String key, final int value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, final String key, final int defaultValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defaultValue);
    }

    public static void putFloat(Context context, final String key, final float value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, final String key, final float defaultValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getFloat(key, defaultValue);
    }

    public static void putLong(Context context, final String key, final long value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, final String key, final long defaultValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, defaultValue);
    }

    public static void clear(Context context, final SharedPreferences preferences) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    */
}
