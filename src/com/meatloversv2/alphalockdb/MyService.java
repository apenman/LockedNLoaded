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




     /*try{
     StateListener phoneStateListener = new StateListener();
     TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
     telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
     }catch(Exception e){
    	 System.out.println(e);
     }*/
    
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);

		mReceiver = new LockScreenReceiver();
		registerReceiver(mReceiver, filter);


		super.onCreate();
}
@Override
public void onStart(Intent intent, int startId) {
	// TODO Auto-generated method stub

	super.onStart(intent, startId);
}

/*class StateListener extends PhoneStateListener{
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        super.onCallStateChanged(state, incomingNumber);
        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                System.out.println("call Activity off hook");
            	getApplication().startActivity(myIntent);



                break;
            case TelephonyManager.CALL_STATE_IDLE:
                break;
        }
    }
};*/


/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	Log.d("res", "checking");
 if(requestCode == REQ_ENTER_PATTERN) {
		if(resultCode == 1) {
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
}*/

@Override
public void onDestroy() {
	unregisterReceiver(mReceiver);
	super.onDestroy();
}
}
