package com.meatloversv2.alphalockdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class LockDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper{

	/** The name of the database. */
	public static final String DATABASE_NAME = "lockdatabase.db";
	
	/** The starting database version. */
	public static final int DATABASE_VERSION = 1;
	
	public LockDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LockTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LockTable.onUpgrade(db, oldVersion, newVersion);
	}

}
