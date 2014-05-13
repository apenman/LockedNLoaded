package com.meatloversv2.alphalockdb;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LockTable {
	
	/** Lock table in the database. */
	public static final String DATABASE_TABLE_LOCK = "lock_table";
	
	/** Joke table column names and IDs for database access. */
	public static final String LOCK_KEY_ID = "_id";
	public static final int KEY_COL_ID = 0;	
	
	public static final String LOCK_PATTERN = "pattern";
	public static final int PATTERN_COL_ID = KEY_COL_ID + 1;	
	
	public static final String LOCK_APP_NAME = "app_name";
	public static final int APP_NAME_COL_ID = KEY_COL_ID + 2;	
	
	public static final String LOCK_PACKAGE_NAME = "package_name";
	public static final int PACKAGE_NAME_COL_ID = KEY_COL_ID + 3;
	
	/** SQLite database creation statement. Auto-increments IDs of inserted jokes.
	 * Joke IDs are set after insertion into the database. */
	public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_LOCK + " (" + 
			LOCK_KEY_ID + " integer primary key autoincrement, " + 
			LOCK_PATTERN	+ " text not null, " + 
			LOCK_APP_NAME	+ " text not null, " + 
			LOCK_PACKAGE_NAME + " text not null);";
	
	/** SQLite database table removal statement. Only used if upgrading database. */
	public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_LOCK;
	
	/**
	 * Initializes the database.
	 * 
	 * @param database
	 * 				The database to initialize.	
	 */
	public static void onCreate(SQLiteDatabase database) {
		// TODO
		database.execSQL(DATABASE_CREATE);
	}
	
	/**
	 * Upgrades the database to a new version.
	 * 
	 * @param database
	 * 					The database to upgrade.
	 * @param oldVersion
	 * 					The old version of the database.
	 * @param newVersion
	 * 					The new version of the database.
	 */
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// TODO
		Log.w(LockTable.class.getName(), "updating database from " + oldVersion + "to " + newVersion);
		
		database.execSQL(DATABASE_DROP);
		onCreate(database);
	}
}
