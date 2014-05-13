package com.meatloversv2.alphalockdb;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

import java.util.List;

import com.meatloversv2.alphalockdb.R;
import com.meatloversv2.alphalockdb.R.id;
import com.meatloversv2.alphalockdb.R.layout;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class LockScreenAppActivity extends Activity {

	/** Called when the activity is first created. */
	boolean inDragMode;
	ImageView droid, phone, home;
	//LockPatternView patternView;

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().setType(
				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onAttachedToWindow();
	}

	/*public void openApplication() {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> pkgAppsList = getPackageManager()
				.queryIntentActivities(mainIntent, 0);

		String fb = null;

		for (ResolveInfo res : pkgAppsList) {
			if (res.activityInfo.packageName.contains("facebook")) {
				fb = res.activityInfo.packageName;
			}
			Log.d("name / packname", res.activityInfo.packageName + " "
					+ res.activityInfo.name);
		}

		PackageManager pmi = getPackageManager();
		Intent intent;
		if (fb != null) {
			intent = pmi.getLaunchIntentForPackage(fb);
			if (intent != null) {
				startActivity(intent);
			}
		}

		// startActivity(new Intent(Intent.ACTION_VIEW,
		// Uri.parse("content://contacts/people/")));
		finish();
	}*/

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Refer to
		// http://developer.android.com/reference/android/view/WindowManager.LayoutParams.html
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.lock_main);
		droid = (ImageView) findViewById(R.id.droid);
		//patternView = (LockPatternView) findViewById(R.id.patternView);
		if (getIntent() != null && getIntent().hasExtra("kill")
				&& getIntent().getExtras().getInt("kill") == 1) {
			finish();
		}

		try {
			startService(new Intent(this, MyService.class));
			
			StateListener phoneStateListener = new StateListener();
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			telephonyManager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);
			//Critical Section
/*			Uri uri = Uri.parse("content://com.meatloversv2.alphalockdb.ContentProvider/lock_table");
			Cursor c = getBaseContext().getContentResolver().query(uri, null, null, null, null);

			int i = 0;
			c.moveToFirst();
			String[] patterns = new String[c.getCount()];
			String[] packages = new String[c.getCount()];
			while(!c.isAfterLast()) {
				patterns[i] = c.getString(0);
				packages[i] = c.getString(3);
				i++;
				c.moveToNext();
			}
			 // TODO
			Intent matchIntent = new Intent();
			matchIntent.putExtra("patterns", patterns);
			matchIntent.putExtra("packages", packages);
			// TODO
			startActivityForResult(matchIntent, 2);
			
			
			droid.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					openApplication();
					return true;
				}
			});*/
			
		 	Log.d("test", "got here");
			Uri uri = Uri.parse("content://com.meatloversv2.alphalockdb.ContentProvider/lock_table");
			ContentProviderClient yourCR = getContentResolver().acquireContentProviderClient(uri);
			Log.d("test", "got cp");
			Cursor c = null;
			try {
				c = yourCR.query(uri, null, null, null, null);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e("cp", "caught error");
			}

			Log.d("test", "got to loop");
			// ITERATE OVER CURSOR
			
			int i = 0;
			String[] packName = new String[c.getCount()];
			String[] patt = new String[c.getCount()];
			c.moveToFirst();
			Log.d("test", "before");
			while (c.isAfterLast() == false) 
			{
				patt[i] = c.getString(1);
			    packName[i]  = c.getString(3);
			    Log.d("setting", "patt: " + patt[i] + " pack: " + packName[i]);
			    i++;
			    c.moveToNext();
			}
			Log.d("test", "after");
			// PUT ARRAYS IN INTENT
			Intent compIntent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null,
					this, LockPatternActivity.class);
			Log.d("test", "made / putting");
			compIntent.putExtra("patterns", patt);
			compIntent.putExtra("packages", packName);
			compIntent.putExtra(LockPatternActivity.EXTRA_PATTERN, "HI".toCharArray());
			Log.d("this is it", "made it");
			//t.startActivityForResult(compIntent, REQ_ENTER_PATTERN);s
			startActivityForResult(compIntent, 2);
			Log.d("this is it", "NOPE");
			
			//Toast.makeText(getBaseContext(), "If I appear, all is well", Toast.LENGTH_SHORT).show();
			
			//End Critical Section
		} catch (Exception e) {

		}

	}

	class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("call Activity off hook");
				finish();

				break;
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			}
		}
	};

	public void onSlideTouch(View view, MotionEvent event) {

	}

	@Override
	public void onBackPressed() {
		// Don't allow back to dismiss.
		return;
	}

	// only used in lockdown mode
	@Override
	protected void onPause() {
		super.onPause();

		// Don't hang around.
		// finish();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Don't hang around.
		// finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_POWER)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {
			return true;
		}
/*		if ((keyCode == KeyEvent.KEYCODE_HOME)) {
			return true;
		}*/

		return false;

	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
			// Intent i = new Intent(this, NewActivity.class);
			// startActivity(i);
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {
			return false;
		}
		return false;
	}

	public void onDestroy() {
		super.onDestroy();
	}
	
	// TODO
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("result", "result1");
		if(requestCode == 2) {
			Log.d("result", "result");
			if(resultCode == RESULT_OK) {
				// TODO
				String packName = data.getStringExtra("packageName");
			    PackageManager  pmi = getPackageManager();
			    Intent intent = null;

			    if(packName.equals("com.meatloversv2.alphalockdb.AlphaLock.class")) {
			    	intent = new Intent(this, AlphaLock.class);
			    	startActivity(intent);
			    }
			    else {
			    	intent = pmi.getLaunchIntentForPackage(packName);                    
			    	if (intent != null){
			    		startActivity(intent);
			    	}
			    }
				// GET RID OF LOCK SCREEN
			}
		}
	}

}