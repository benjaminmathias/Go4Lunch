package com.bmathias.go4lunch.data.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.bmathias.go4lunch.utils.App;

public class SharedPrefs {

   private final static String RADIUS_KEY = "radius";

   private final static String NOTIFICATION_KEY = "notification";

   private static SharedPreferences mSharedPref;
   private static volatile SharedPrefs instance;

   private SharedPrefs() {}

   public static SharedPrefs getInstance() {
      SharedPrefs result = instance;
      if (result != null) {
         return result;
      }
      synchronized (SharedPrefs.class) {
         if (instance == null) {
            instance = new SharedPrefs();
            init(App.getContext());
         }
         return instance;
      }
   }

   public static void init(Context context) {
      if(mSharedPref == null)
         mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
   }

   public String getString(String key, String defValue) {
      return mSharedPref.getString(key, defValue);
   }

   public void putString(String key, String value) {
      SharedPreferences.Editor prefsEditor = mSharedPref.edit();
      prefsEditor.putString(key, value);
      prefsEditor.apply();
   }

   public void setRadius(String radius) {
      putString(RADIUS_KEY, radius);
   }

   public String getRadius() {
      return getString(RADIUS_KEY, "1000");
   }

   public Boolean getBoolean(String key, Boolean defValue) {
      return mSharedPref.getBoolean(key, defValue);
   }

   public void putBoolean(String key, Boolean value) {
      SharedPreferences.Editor prefsEditor = mSharedPref.edit();
      prefsEditor.putBoolean(key, value);
      prefsEditor.apply();
   }

   public void setNotificationsPreferences(Boolean value){
      putBoolean(NOTIFICATION_KEY, value);
   }

   public Boolean getNotificationsPreferences() {
      return getBoolean(NOTIFICATION_KEY, true);
   }
}
