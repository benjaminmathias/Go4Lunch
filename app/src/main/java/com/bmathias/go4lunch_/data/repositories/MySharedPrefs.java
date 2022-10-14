package com.bmathias.go4lunch_.data.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.bmathias.go4lunch_.utils.App;

public class MySharedPrefs {

   private static SharedPreferences mSharedPref;

   private static volatile MySharedPrefs instance;


   private MySharedPrefs() {}

   public static MySharedPrefs getInstance() {
      MySharedPrefs result = instance;
      if (result != null) {
         return result;
      }
      synchronized (MySharedPrefs.class) {
         if (instance == null) {
            instance = new MySharedPrefs();
            init(App.getContext());
         }
         return instance;
      }
   }

   public static void init(Context context)
   {
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


   //// Clear Preference ////
   public static void clearPreference(Context context) {
      mSharedPref.edit().clear().apply();
   }

   //// Remove ////
   public static void removePreference(String Key){
      mSharedPref.edit().remove(Key).apply();
   }

}
