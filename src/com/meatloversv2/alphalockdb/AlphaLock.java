package com.meatloversv2.alphalockdb;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

import java.io.Serializable;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.meatloversv2.alphalockdb.LockView.OnLockChangeListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.ActionMode.Callback;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class AlphaLock extends SherlockFragmentActivity implements LoaderCallbacks<Cursor>, OnLockChangeListener {
	private static final int INTENT_CODE = 3;
	private static final int LOADER_ID = 1;
	public static final String APP_CALLBACK = "appName";
	public static final String PACK_CALLBACK = "packageName";
	public static final String CODE_CALLBACK = "codePattern";
	private static final int REQ_ENTER_PATTERN = 2;
	private static final int REQ_CREATE_PATTERN = 1;
	
	private LockCursorAdapter m_lockAdapter;
	protected Menu m_vwMenu;
	protected ListView m_vwLockLayout;
	private Callback mActionModeCallback;
	private ActionMode mActionMode;
	private int selected_position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.m_lockAdapter = new LockCursorAdapter(this, null, 0);
		this.m_lockAdapter.setOnLockChangeListener(this);
		
		this.getSupportLoaderManager().initLoader(LOADER_ID, null, this);
		
		initLayout();
	}

	protected void initLayout() {
		this.setContentView(R.layout.activity_main);
		this.m_vwLockLayout = (ListView)this.findViewById(R.id.lockListViewGroup);
		this.m_vwLockLayout.setAdapter(m_lockAdapter);
		
		this.m_vwLockLayout.setOnItemLongClickListener (new OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (mActionMode != null) {
		            return false;
		        }
		        mActionMode = getSherlock().startActionMode(mActionModeCallback);
		        selected_position = position;
		        return true;
		  }
		});
		
		mActionModeCallback = new Callback() {
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.actionmenu, menu);
		        return true;
		    }

		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        return false;
		    }

		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		    	Lock selectedLock = ((LockView) m_vwLockLayout.getChildAt(selected_position)).getLock();
		    	Uri uri = Uri.parse("content://" + LockContentProvider.AUTHORITY + "/" + LockContentProvider.BASE_PATH + "/" + Long.toString(selectedLock.getId()));
		    	
		        switch (item.getItemId()) {
		            case R.id.menu_remove:
		            	// REMOVE
		        		Log.d("test", "REMOVING ID: " + selectedLock.getId());
		        		getContentResolver().delete(uri, null, null);
		        		fillData();
		        		mode.finish();
		                break;
		            case R.id.menu_update:
		            	Log.d("test", "TRYING TO UPDATE"); 
		            	
		            	// UPDATE
		            	Intent createIntent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null,
		        		       getBaseContext(), LockPatternActivity.class);
		        		startActivityForResult(createIntent, REQ_CREATE_PATTERN);
		        		mode.finish();
		        		break;
		            default:
		                return false;
		        }
		        
		        return true;
		    }

		    public void onDestroyActionMode(ActionMode mode) {
		        mActionMode = null;
		    }
		};
	}
	
	
	private void fillData() {
		Log.d("fill", "filling");
		this.getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
		this.m_vwLockLayout.setAdapter(m_lockAdapter);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		this.m_vwMenu = menu;
		return true;
    }

	@Override
	public void onLockChanged(LockView view, Lock lock) {
		// TODO Auto-generated method stub
		fillData();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = {LockTable.LOCK_KEY_ID, LockTable.LOCK_PATTERN, LockTable.LOCK_APP_NAME, LockTable.LOCK_PACKAGE_NAME};
		
		Uri uri = Uri.parse("content://com.meatloversv2.alphalockdb.ContentProvider/lock_table");
		
		CursorLoader loader = new CursorLoader(this, uri, projection, null, null, null);
		
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		m_lockAdapter.swapCursor(arg1);
		m_lockAdapter.setOnLockChangeListener(this);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		m_lockAdapter.swapCursor(null);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
		case R.id.menu_add:
			//START NEW INTENT FOR RESULT
			Intent intent = new Intent(this, AddActivity.class);
			startActivityForResult(intent, INTENT_CODE);
			break;
		/*case R.id.menu_launch:
			// QUERY DATABASE
			Uri uri = Uri.parse("content://com.meatloversv2.alphalockdb.ContentProvider/lock_table");
			Cursor c = getContentResolver().query(uri, null, null, null, null);
			if(c == null) {
				Log.d("fuck", "query fuck");
			}
			// ITERATE OVER CURSOR
			c.moveToFirst();
			int i = 0;
			String[] packName = new String[c.getCount()];
			String[] patt = new String[c.getCount()];
			while (c.isAfterLast() == false) 
			{
				patt[i] = c.getString(1);
			    packName[i]  = c.getString(3);
			    Log.d("setting", "patt: " + patt[i] + " pack: " + packName[i]);
			    i++;
			    c.moveToNext();
			}
			// PUT ARRAYS IN INTENT
			Intent compIntent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null,
			        this, LockPatternActivity.class);
			compIntent.putExtra("patterns", patt);
			compIntent.putExtra("packages", packName);
			compIntent.putExtra(LockPatternActivity.EXTRA_PATTERN, "HI".toCharArray());

			startActivityForResult(compIntent, REQ_ENTER_PATTERN);
			// START LOCK COMPARE ACTIVITY
			break;*/
		}
		
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("res", "checking");
		if(requestCode == INTENT_CODE) {
			Log.d("res", "intentmatch");
			if(resultCode == RESULT_OK) {
				Log.d("res", "okmatch");
				// GET DATA
				String appName = data.getStringExtra(APP_CALLBACK);
				String packName = data.getStringExtra(PACK_CALLBACK);
				String pattName = data.getStringExtra(CODE_CALLBACK);
				// INSERT INTO DB
				Uri uri = LockContentProvider.CONTENT_URI;
				ContentValues values = new ContentValues();
				values.put(LockTable.LOCK_PATTERN, pattName);
				Log.d("patt out", "patt : " + pattName);
				values.put(LockTable.LOCK_APP_NAME, appName);
				values.put(LockTable.LOCK_PACKAGE_NAME, packName);
				
				String id = getContentResolver().insert(uri, values).getLastPathSegment();
				Log.d("test", "ID is: " + id);
				fillData();
				
				Toast.makeText(this, "ADDED " + data.getStringExtra(APP_CALLBACK) + data.getStringExtra(PACK_CALLBACK), Toast.LENGTH_LONG).show();
			}
		}
		else if(requestCode == REQ_ENTER_PATTERN) {
			if(resultCode == RESULT_OK) {
				String packName = data.getStringExtra("packageName");
				Log.d("okay", "starting : " + packName);
			    PackageManager  pmi = getPackageManager();
			    Intent intent = null;
			    
			    intent = pmi.getLaunchIntentForPackage(packName);                    
			    if (intent != null){
			    	startActivity(intent);
			    }
			}
			// START ACTIVITY WITH PACKAGE NAME FROM EXTRA
		}
		else if(requestCode == REQ_CREATE_PATTERN) {
			if(resultCode == RESULT_OK) {
	            char[] pattern = data.getCharArrayExtra(
	                    LockPatternActivity.EXTRA_PATTERN);
	            String newPattern = new String(pattern);
		    	Lock selectedLock = ((LockView) m_vwLockLayout.getChildAt(selected_position)).getLock();
		    	Uri uri = Uri.parse("content://" + LockContentProvider.AUTHORITY + "/" + LockContentProvider.BASE_PATH + "/" + Long.toString(selectedLock.getId()));
		    	ContentValues cv = new ContentValues();
		    	cv.put(LockTable.LOCK_PATTERN, newPattern);
        		getContentResolver().update(uri, cv, null, null);
        		fillData();
			}
		}
	}
	
	
}
