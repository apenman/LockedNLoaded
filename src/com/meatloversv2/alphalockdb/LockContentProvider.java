package com.meatloversv2.alphalockdb;

import java.util.Arrays;
import java.util.HashSet;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class LockContentProvider extends ContentProvider  {
	public static final String AUTHORITY = "com.meatloversv2.alphalockdb.ContentProvider";
	private static final int LOCK_ID = 1;
	public static final String BASE_PATH = "lock_table";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, LOCK_ID);
	}
	
	private LockDatabaseHelper database;
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		int deleteCount = 0;
		sURIMatcher.match(arg0);
		
		String delete = LockTable.LOCK_KEY_ID + "=" + arg0.getLastPathSegment();
		deleteCount = sqlDB.delete(LockTable.DATABASE_TABLE_LOCK, delete, null);
		Log.d("delete", "deleting joke " + delete);
		
		if(deleteCount > 0) {
			getContext().getContentResolver().notifyChange(arg0, null);
		}
		
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		//Toast.makeText(this.getContext(), (CharSequence) values.get(LockTable.LOCK_PATTERN), Toast.LENGTH_LONG).show();
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		sURIMatcher.match(uri);
		long id;
		id = sqlDB.insert(LockTable.DATABASE_TABLE_LOCK, null, values);
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		database = new LockDatabaseHelper(this.getContext(), "database", null, 1);
		String settingsPattern = "fa3b8ffe29ed4bc6de29350d87ef72d42b13d990";
		ContentValues values = new ContentValues();
		values.put(LockTable.LOCK_PATTERN, settingsPattern);
		values.put(LockTable.LOCK_APP_NAME, "SETTINGS");
		values.put(LockTable.LOCK_PACKAGE_NAME, "com.meatloversv2.alphalockdb.AlphaLock.class");
		
		this.insert(CONTENT_URI, values);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d("query", "queried");
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	 
		checkColumns(projection);
	 
		queryBuilder.setTables(LockTable.DATABASE_TABLE_LOCK);
	 
		sURIMatcher.match(uri);
		
		selection = null;
		
		SQLiteDatabase db = this.database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, null, null, null, null);
	 
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		if(cursor == null) {
			Log.d("query", "NULL");
			return null;
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		sURIMatcher.match(uri);
		int updateCount = 0;
		String update = LockTable.LOCK_KEY_ID + "=" + uri.getLastPathSegment();
		
		updateCount = sqlDB.update(LockTable.DATABASE_TABLE_LOCK, values, update, null);
		
		if(updateCount > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return updateCount;
	}
	
	
	/**
	 * Verifies the correct set of columns to return data from when performing a query.
	 * 
	 * @param projection
	 * 						The set of columns about to be queried.
	 */
	private void checkColumns(String[] projection) {
		String[] available = { LockTable.LOCK_KEY_ID, LockTable.LOCK_PATTERN, LockTable.LOCK_APP_NAME,
				LockTable.LOCK_PACKAGE_NAME};
		
		if(projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			
			if(!availableColumns.containsAll(requestedColumns))	{
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

}
