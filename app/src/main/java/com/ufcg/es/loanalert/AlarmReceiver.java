package com.ufcg.es.loanalert;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.List;
import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.ufcg.es.loanalert.AlarmService.ALARM_SERVICE_TAG;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(ALARM_SERVICE_TAG, "Performing alarm service checks!");
        checkAlarms(context);
    }

    private void checkAlarms(Context context) {
        List<LoanEntry> loanEntryList = new Select().from(LoanEntry.class).execute();
        for (LoanEntry loanEntry : loanEntryList) {
            if (loanEntry.getDueDate().isBeforeNow()) {
                fireAlarmFor(context, loanEntry);
            }
        }
    }

    private void fireAlarmFor(Context context, LoanEntry loanEntry) {
        Log.w(ALARM_SERVICE_TAG, String.format(Locale.US, "Firing alarm for %d (%s)!",
            loanEntry.getLoanEntryId(), loanEntry.getTitle()));
        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat
            .Builder(context).setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(loanEntry.getTitle())
            .setContentText(context.getString(R.string.notification_item_must_be_returned))
            .setAutoCancel(false);
        notificationManager.notify(loanEntry.getLoanEntryId(), builder.build());
    }

}
