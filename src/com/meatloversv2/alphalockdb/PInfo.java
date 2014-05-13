package com.meatloversv2.alphalockdb;

import android.graphics.drawable.Drawable;

public class PInfo {
    private String appname = "";
    private String pname = "";
    private String versionName = "";
    private int versionCode = 0;
    private Drawable icon;
    
    public void setAppName(String appname) {
    	this.appname = appname;
    }
    
    public void setPName(String pname) {
    	this.pname = pname;
    }
    
    public void setVersionName(String versionName) {
    	this.versionName = versionName;
    }
    
    public void setVersionCode(int versionCode) {
    	this.versionCode = versionCode;
    }
    
    public void setIcon(Drawable icon) {
    	this.icon = icon;
    }
    
    public String getAppName() {
    	return appname;
    }
    
    public String getPName() {
    	return pname;
    }
}
