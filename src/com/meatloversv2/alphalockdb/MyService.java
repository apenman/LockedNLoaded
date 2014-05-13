package com.meatloversv2.alphalockdb;




import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class MyService extends Service{
	 private static final int REQ_ENTER_PATTERN = 2;
	BroadcastReceiver mReceiver;
	// Intent myIntent;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onCreate() {
		KeyguardManager.KeyguardLock k1;
	 
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

		KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
     		k1= km.newKeyguardLock("IN");
     		k1.disableKeyguard();

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);

		mReceiver = new LockScreenReceiver();
		registerReceiver(mReceiver, filter);


		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
