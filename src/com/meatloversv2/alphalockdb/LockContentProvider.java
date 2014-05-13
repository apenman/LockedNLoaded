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
	/** The authority for this content provider. */
	public static final String AUTHORITY = "com.meatloversv2.alphalockdb.ContentProvider";
	
	/** Values for the URIMatcher. */
	private static final int LOCK_ID = 1;
	
	/** The database table to read from and write to, and also the root path for use in the URI matcher.
	 * This is essentially a label to a two-dimensional array in the database filled with rows of jokes
	 * whose columns contain joke data. */
	public static final String BASE_PATH = "lock_table";
	
	/** This provider's content location. Used by accessing applications to
	 * interact with this provider. */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	/** Matches content URIs requested by accessing applications with possible
	 * expected content URI formats to take specific actions in this provider. */
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
			/** Alert any watchers of an underlying data change for content/view refreshing. */
			getContext().getContentResolver().notifyChange(arg0, null);
		}
		
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		/** Use a helper class to perform a query for us. */
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	 
		/** Make sure the projection is proper before querying. */
		checkColumns(projection);
	 
		/** Set up helper to query our jokes table. */
		queryBuilder.setTables(LockTable.DATABASE_TABLE_LOCK);
	 
		/** Match the passed-in URI to an expected URI format. */
		sURIMatcher.match(uri);
		
		selection = null;
		
		SQLiteDatabase db = this.database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, null, null, null, null);
	 
		/** Set the cursor to automatically alert listeners for content/view refreshing. */
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
		// TODO Auto-generated method stub
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		sURIMatcher.match(uri);
		int updateCount = 0;
		String update = LockTable.LOCK_KEY_ID + "=" + uri.getLastPathSegment();
		
		updateCount = sqlDB.update(LockTable.DATABASE_TABLE_LOCK, values, update, null);
		
		if(updateCount > 0) {
			/** Alert any watchers of an underlying data change for content/view refreshing. */
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
