package com.meatloversv2.alphalockdb;

import com.meatloversv2.alphalockdb.LockView.OnLockChangeListener;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class LockCursorAdapter extends android.support.v4.widget.CursorAdapter {
	private OnLockChangeListener m_listener;
	
	public LockCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		// TODO Auto-generated constructor stub
	}

	public void setOnLockChangeListener(OnLockChangeListener listener) {
		this.m_listener = listener;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		Log.d("bind", "binding");
		long lockId = cursor.getLong(LockTable.KEY_COL_ID);
		String pattern = cursor.getString(LockTable.PATTERN_COL_ID);
		String name = cursor.getString(LockTable.APP_NAME_COL_ID);
		String pack = cursor.getString(LockTable.PACKAGE_NAME_COL_ID);
		Log.d("bind", pattern + " " + name);
		Lock lock = new Lock(lockId, pattern, name, pack);
		
		((LockView)view).setOnLockChangeListener(null);
		((LockView)view).setLock(lock);
		((LockView)view).setOnLockChangeListener(this.m_listener);
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup view) {
		// TODO Auto-generated method stub
		Log.d("new", "entered newView");
		long lockId = cursor.getLong(LockTable.KEY_COL_ID);
		Log.d("new", Long.toString(lockId));
		String pattern = cursor.getString(LockTable.PATTERN_COL_ID);
		Log.d("new", pattern);
		String name = cursor.getString(LockTable.APP_NAME_COL_ID);
		Log.d("new", name);
		String pack = cursor.getString(LockTable.PACKAGE_NAME_COL_ID);
		Log.d("new", pack);
		Lock lock = new Lock(lockId, pattern, name, pack);
		
		LockView newView = new LockView(context, lock);
		newView.setOnLockChangeListener(this.m_listener);
		
		return newView;
	}

	
}
