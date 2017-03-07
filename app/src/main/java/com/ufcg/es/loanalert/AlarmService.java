package com.ufcg.es.loanalert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

public class AlarmService extends Service {

    public static final String ALARM_SERVICE_TAG = AlarmService.class.toString();

    private final IBinder binder = new AlarmServiceBinder();
    private AlarmServiceHandler serviceHandler;
    private AlarmManager alarmManager;
    private PendingIntent currentAlarmIntent;

    private static final int START_ALARM_SERVICE = 0;

    public class AlarmServiceBinder extends Binder {

        AlarmService getService() {
            return AlarmService.this;
        }

    }

    private final class AlarmServiceHandler extends Handler {

        AlarmServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case START_ALARM_SERVICE:
                    doStartAlarmService();
                    break;
                default:
                    break;
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread(AlarmService.class.getName(),
            Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        Looper serviceLooper = thread.getLooper();
        serviceHandler = new AlarmServiceHandler(serviceLooper);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startAlarmService();
        return START_STICKY;
    }

    public void startAlarmService() {
        serviceHandler.sendMessage(serviceHandler.obtainMessage(START_ALARM_SERVICE));
    }

    private void doStartAlarmService() {
        Log.w(ALARM_SERVICE_TAG, "Starting alarm service...");
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        if (currentAlarmIntent != null) {
            alarmManager.cancel(currentAlarmIntent);
        }
        currentAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 30 * 1000,
            AlarmManager.INTERVAL_DAY, currentAlarmIntent);
        Log.w(ALARM_SERVICE_TAG, "Alarm service started!");
    }

    @Override
    public void onDestroy() {
        if (alarmManager != null && currentAlarmIntent != null) {
            alarmManager.cancel(currentAlarmIntent);
        }
        super.onDestroy();
    }

}
