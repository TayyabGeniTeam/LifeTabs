package com.geniteam.lifetabs.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class LifeTabPrefManager {
	private Context mMain;

	/*
	 * SharedPreferencesNames
	 */
	public static class SharedPreferencesNames {

		public static final String IS_SHOW_TUTORIALS = "is_show_tutorials";
        public static final String MIN_VALUE_SET = "min_value_set" ;
        public static final String MAX_VALUE_SET = "max_value_set" ;
        public static final String IS_DEFAULT_RANGES_SET = "is_default_ranges_set";
	}

	/*
	 * SharedPreferencesSettings
	 */
	public static class SharedPreferencesSettings {
		public final static String NAME = "StolikPrefs";
		
		public final static int DEFAULT_VALUE_INT = 0;
		public final static float DEFAULT_VALUE_FLOAT = 0f;
		public final static long DEFAULT_VALUE_LONG = 0;
		public final static boolean DEFAULT_VALUE_BOOLEAN = false;
		public final static String DEFAULT_VALUE_STRING = "";
		public final static int CUSTOM_VALUE_INT = -1;
	}

	/*
	 * SharedPrefencesModes
	 */
	public static class SharedPrefencesModes {
		public final static int MODE_PRIVATE = 0;
		public final static int MODE_WORLD_READABLE = 2;
		public final static int MODE_WORLD_WRITEABLE = 1;
	}

	public LifeTabPrefManager(Context pContext) {
		mMain = pContext;
	}

	/**
	 * setSharedPreference
	 * 
	 * @param pSharedPreferenceName
	 * @param pValue
	 */
	public void setSharedPreference(String pSharedPreferenceName, int pValue) {
		SharedPreferences settings = mMain.getSharedPreferences(
				SharedPreferencesSettings.NAME,
				SharedPrefencesModes.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(pSharedPreferenceName, pValue);
		editor.commit();
	}

	/**
	 * setSharedPreference
	 * 
	 * @param pSharedPreferenceName
	 * @param pValue
	 */
	public void setSharedPreference(String pSharedPreferenceName, long pValue) {
		SharedPreferences settings = mMain.getSharedPreferences(
				SharedPreferencesSettings.NAME,
				SharedPrefencesModes.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(pSharedPreferenceName, pValue);
		editor.commit();
	}

	/**
	 * setSharedPreference
	 * 
	 * @param pSharedPreferenceName
	 * @param pValue
	 */
	public void setSharedPreference(String pSharedPreferenceName, String pValue) {
		SharedPreferences settings = mMain.getSharedPreferences(
				SharedPreferencesSettings.NAME,
				SharedPrefencesModes.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(pSharedPreferenceName, pValue);
		editor.commit();
	}
	/**
	 * setSharedPreference boolean
	 * 
	 * @param pSharedPreferenceName
	 * @param pValue
	 */
	public void setSharedPreference(String pSharedPreferenceName, boolean pValue) {
		SharedPreferences settings = mMain.getSharedPreferences(
				SharedPreferencesSettings.NAME,
				SharedPrefencesModes.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(pSharedPreferenceName, pValue);
		editor.commit();
	}

	/**
	 * getSharedPreferenceInt
	 * 
	 * @param pSharedPreferenceName
	 * @return
	 */
	public int getSharedPreferenceInt(String pSharedPreferenceName) {
		SharedPreferences settings = mMain.getSharedPreferences(
				SharedPreferencesSettings.NAME,
				SharedPrefencesModes.MODE_PRIVATE);
		return settings.getInt(pSharedPreferenceName,
				SharedPreferencesSettings.DEFAULT_VALUE_INT);
	}

	/**
	 * getSharedPreferenceLong
	 * 
	 * @param pSharedPreferenceName
	 * @return
	 */
	public long getSharedPreferenceLong(String pSharedPreferenceName) {
		SharedPreferences settings = mMain.getSharedPreferences(
				SharedPreferencesSettings.NAME,
				SharedPrefencesModes.MODE_PRIVATE);
		return settings.getLong(pSharedPreferenceName,
				SharedPreferencesSettings.DEFAULT_VALUE_LONG);
	}

	/**
	 * getSharedPreferenceString
	 * 
	 * @param pSharedPreferenceName
	 * @return
	 */
	public String getSharedPreferenceString(String pSharedPreferenceName) {
		SharedPreferences settings = mMain.getSharedPreferences(
				SharedPreferencesSettings.NAME,
				SharedPrefencesModes.MODE_PRIVATE);
		return settings.getString(pSharedPreferenceName,
				SharedPreferencesSettings.DEFAULT_VALUE_STRING);
	}
	/**
	 * getSharedPreferenceBooelan
	 * 
	 * @param pSharedPreferenceName
	 * @return
	 */
	public boolean getSharedPreferenceBoolean(String pSharedPreferenceName) {
		SharedPreferences settings = mMain.getSharedPreferences(
				SharedPreferencesSettings.NAME,
				SharedPrefencesModes.MODE_PRIVATE);
		return settings.getBoolean(pSharedPreferenceName,
				SharedPreferencesSettings.DEFAULT_VALUE_BOOLEAN);
	}

	
}