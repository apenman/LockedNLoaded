package com.meatloversv2.alphalockdb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LockView extends LinearLayout{
	
	private Lock m_lock;
	private TextView m_LockText;
	private OnLockChangeListener m_onLockChangeListener;
	
	public LockView (Context context, Lock lock) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.lock_view, this, true);
		this.m_LockText = (TextView)findViewById(R.id.lockTextView);
		this.m_onLockChangeListener = null;
		this.setLock(lock);
	}
	
	public void setLock(Lock lock) {
		this.m_lock = lock;
		Log.d("set lock", "set name " + m_lock.getName());
		this.m_LockText.setText(m_lock.getName());
		Log.d("set lock", "complete");
	}
	
	public void setOnLockChangeListener(OnLockChangeListener listener) {
		this.m_onLockChangeListener = listener;
	}
	
	public void notifyOnLockChangeListener() {
		if(this.m_onLockChangeListener != null) {
			this.m_onLockChangeListener.onLockChanged(this, m_lock);
		}
	}
	
	/**
	 * Interface definition for a callback to be invoked when the underlying
	 * Joke is changed in this JokeView object.
	 */
	public static interface OnLockChangeListener {

		/**
		 * Called when the underlying Joke in a JokeView object changes state.
		 * 
		 * @param view
		 *            The JokeView in which the Joke was changed.
		 * @param joke
		 *            The Joke that was changed.
		 */
		public void onLockChanged(LockView view, Lock lock);
	}
	
	public Lock getLock() {
		return this.m_lock;
	}
}
