package com.example.myapp.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapp.CustomPinActivity;
import com.example.myapp.security.JwtSecurityService;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;

public class HomeApplication extends Application implements JwtSecurityService {

    private static HomeApplication instance;
    private static Context appContext;

    public static HomeApplication getInstance() { return instance; }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        HomeApplication.appContext = appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
//        lockManager.getAppLock().disableAndRemoveConfiguration();
        lockManager.enableAppLock(this, CustomPinActivity.class);
//        lockManager.getAppLock().setPasscode(null);
//        lockManager.getAppLock().addIgnoredActivity(MainActivity.class);
//        lockManager.disableAppLock();
        instance=this;
        Log.d("InitApp","Наш додаток створено");
        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void saveJwtToken(String token) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs =  instance.getSharedPreferences("jwtStore", MODE_PRIVATE);
        edit=prefs.edit();
        try {
            edit.putString("token",token);
            edit.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getToken() {
        SharedPreferences prefs=instance.getSharedPreferences("jwtStore", Context.MODE_PRIVATE);
        String token = prefs.getString("token","");
        return token;
    }

    @Override
    public void deleteToken() {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs=instance.getSharedPreferences("jwtStore", Context.MODE_PRIVATE);
        edit=prefs.edit();
        try {
            edit.remove("token");
            edit.apply();
            edit.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
