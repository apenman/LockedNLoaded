package com.meatloversv2.alphalockdb;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.ActionMode.Callback;
import com.actionbarsherlock.view.Menu;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class AddActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor> {

	//private ArrayList<PInfo> packages;
	private ArrayList<String> m_arrAppNames;
	private ArrayList<PInfo> m_arrPInfo;
	private ListView lv;
	private Callback mActionModeCallback;
	private ActionMode mActionMode;
	private int selected_position;
	private String newPattern = null;
	private Menu m_vwMenu;
	
	private static final int REQ_CREATE_PATTERN = 1;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selected_position = -1;
		initLayout();
	}

	private void getInstalledApps(boolean getSysPackages) {
	    //ArrayList<PInfo> res = new ArrayList<PInfo>();  
		m_arrAppNames = new ArrayList<String>();
		m_arrPInfo = new ArrayList<PInfo>();
	    List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
	    for(int i=0;i<packs.size();i++) {
	        PackageInfo p = packs.get(i);
	        if ((!getSysPackages) && (p.versionName == null)) {
	            continue ;
	        }
	        PInfo newInfo = new PInfo();
	        newInfo.setAppName(p.applicationInfo.loadLabel(getPackageManager()).toString());
	        newInfo.setPName(p.packageName);
	        newInfo.setVersionName(p.versionName);
	        newInfo.setVersionCode(p.versionCode);
	        newInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));
	        m_arrPInfo.add(newInfo);
	        m_arrAppNames.add(p.applicationInfo.loadLabel(getPackageManager()).toString());
	    }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.add, menu);
		menu.findItem(R.id.menu_add).setEnabled(false);
		this.m_vwMenu = menu;
		return true;
    }
	
	private void initLayout() {
		// INFLATE
		setContentView(R.layout.activity_add);
		// GET LIST OF APP NAMES
		getInstalledApps(false);

		lv = (ListView) findViewById(R.id.appNameListView);
		
        ArrayAdapter<String> arrayAdapter =      
        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, m_arrAppNames);
        lv.setAdapter(arrayAdapter); 
        
		this.lv.setOnItemLongClickListener (new OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				/*if (mActionMode != null) {
		            return false;
		        }
		        mActionMode = getSherlock().startActionMode(mActionModeCallback);*/
				selected_position = position;
				m_vwMenu.findItem(R.id.menu_add).setEnabled(true);
		        //Log.d("position", "updated to " + selected_position); 
		        return true; 
		  }
		});
		
		mActionModeCallback = new Callback() {
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.addactionmenu, menu);
		        return true;
		    }

		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        return false;
		    }

		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		    	Log.d("long click", "click");
		        switch (item.getItemId()) {
		            case R.id.add_actionadd:
		            	// SET ACTIVITY RESULTS
		            	newPattern = null;
		            	Intent createIntent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null,
		        		        getApplicationContext(), LockPatternActivity.class);
		            	//startActivityForResult(createIntent, REQ_CREATE_PATTERN);
		        		startActivityForResult(createIntent, REQ_CREATE_PATTERN);
		        		
		            	mode.finish();
		            	AddActivity.this.finish();
		            	Log.d("test", "ADDED");
		                return true;
		                
		            default:
		                return false;
		        }  	
		        
		    }
		    

		    public void onDestroyActionMode(ActionMode mode) {
		        mActionMode = null;
		    }
		};
	}
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//START NEW INTENT FOR RESULT
		newPattern = null;
    	//startActivityForResult(createIntent, REQ_CREATE_PATTERN);
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null,
		        this, LockPatternActivity.class);
		startActivityForResult(intent, REQ_CREATE_PATTERN);

    	Log.d("test", "ADDED");
        return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("res", "ADD ACTIVITY CHECK");
	    switch (requestCode) {
	    case REQ_CREATE_PATTERN: 
	        if (resultCode == RESULT_OK) {
	            char[] pattern = data.getCharArrayExtra(
	                    LockPatternActivity.EXTRA_PATTERN);
	            newPattern = new String(pattern);
	            Log.d("result", "new string = " + newPattern);
	            
	            Log.d("test", "after pat, before string return");
	        	Intent returnIntent = new Intent();
	        	if(newPattern != null && selected_position > 0) {
	        		returnIntent.putExtra(AlphaLock.APP_CALLBACK, m_arrPInfo.get(selected_position).getAppName());
	        		returnIntent.putExtra(AlphaLock.PACK_CALLBACK, m_arrPInfo.get(selected_position).getPName());
	        		Log.d("item clicked", "new patt: " + newPattern);
	        		returnIntent.putExtra(AlphaLock.CODE_CALLBACK, newPattern);
	        		setResult(RESULT_OK, returnIntent); 
	        	}
	        	else {
	        		setResult(RESULT_CANCELED, returnIntent);  
	        	}
	        	
	        	this.finish();
	        }
	        break;
	    }
	}

}
