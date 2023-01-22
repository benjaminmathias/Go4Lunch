package com.bmathias.go4lunch_.data.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.bmathias.go4lunch_.utils.App;

public class DistanceSharedPrefs {

   private final static String RADIUS_KEY = "radius";


   private static SharedPreferences mSharedPref;
   private static volatile DistanceSharedPrefs instance;

   private DistanceSharedPrefs() {}

   public static DistanceSharedPrefs getInstance() {
      DistanceSharedPrefs result = instance;
      if (result != null) {
         return result;
      }
      synchronized (DistanceSharedPrefs.class) {
         if (instance == null) {
            instance = new DistanceSharedPrefs();
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
}