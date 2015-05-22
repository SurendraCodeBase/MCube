package com.mcube.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import java.util.Calendar;
import java.util.TimeZone;

public class McubeUtils {

    private final static boolean LOGGING = true;

    public static void errorLog(String message) {
        if (LOGGING) {
            String fullClassName = Thread.currentThread().getStackTrace()[4].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[4].getLineNumber();
            String fullClassName2 = Thread.currentThread().getStackTrace()[3].getClassName();
            String className2 = fullClassName2.substring(fullClassName2.lastIndexOf(".") + 1);
            String methodName2 = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber2 = Thread.currentThread().getStackTrace()[3].getLineNumber();
            if (null != message) {
                Log.e(className + "." + methodName + "():" + lineNumber + " -> " + className2 + "." +
                        methodName2 + "():" + lineNumber2, message);
            }
        }
    }

    public static void debugLog(String message) {
        if (LOGGING) {
            String fullClassName = Thread.currentThread().getStackTrace()[4].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[4].getLineNumber();
            String fullClassName2 = Thread.currentThread().getStackTrace()[3].getClassName();
            String className2 = fullClassName2.substring(fullClassName2.lastIndexOf(".") + 1);
            String methodName2 = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber2 = Thread.currentThread().getStackTrace()[3].getLineNumber();
            if (null != message) {
                Log.d(className + "." + methodName + "():" + lineNumber + " -> " + className2 + "." +
                        methodName2 + "():" + lineNumber2, message);
            }
        }
    }

    public static void infoLog(String message) {
        if (LOGGING) {
            String fullClassName = Thread.currentThread().getStackTrace()[4].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[4].getLineNumber();
            String fullClassName2 = Thread.currentThread().getStackTrace()[3].getClassName();
            String className2 = fullClassName2.substring(fullClassName2.lastIndexOf(".") + 1);
            String methodName2 = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber2 = Thread.currentThread().getStackTrace()[3].getLineNumber();
            if (null != message) {
                Log.i(className + "." + methodName + "():" + lineNumber + " -> " + className2 + "." +
                        methodName2 + "():" + lineNumber2, message);
            }
        }
    }

    //Shared preferences get and set methods
    /**
     * Called to save supplied value in shared preferences against given key.
     * @param context Context of caller activity
     * @param key Key of value to save against
     * @param value Value to save
     */
    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void saveToPrefs(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     * @param context Context of caller activity
     * @param key Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static Boolean getFromPrefsBoolean(Context context, String key, Boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static Calendar getCurrentDate() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
    }

    public static String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    public static int convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) Math.ceil(px / (metrics.densityDpi / 160f));
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToSp(float px, Context context) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }
}
