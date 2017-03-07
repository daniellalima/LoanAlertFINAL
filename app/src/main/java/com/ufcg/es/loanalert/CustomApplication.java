package com.ufcg.es.loanalert;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;

import net.danlew.android.joda.JodaTimeAndroid;

public class CustomApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        ActiveAndroid.initialize(this);
        Iconify.with(new MaterialModule());
        Intent alarmService = new Intent(getApplicationContext(), AlarmService.class);
        startService(alarmService);
    }

}
