package com.meatloversv2.alphalockdb; 

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LockListAdapter extends BaseAdapter {
	
	private Context m_context;
	private List<Lock> m_lockList;

	public LockListAdapter(Context context, List<Lock> lockList) {
		this.m_context = context;
		this.m_lockList = lockList;
		if(getCount() == 0) {
			Log.d("empty", "u hoh");
			
		}
		else
			Log.d("empty", "good");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_lockList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return m_lockList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LockView view = null;
		
		if(convertView == null) {
			view = new LockView(this.m_context, this.m_lockList.get(position));
		}
		else {
			view = (LockView)convertView;
		}
		
		view.setLock(this.m_lockList.get(position));
		return view;
	}

}
