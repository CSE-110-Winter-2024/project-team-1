package edu.ucsd.cse110.successorator.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateChangeReceiver extends BroadcastReceiver {
    private final Runnable updateDate;

    public DateChangeReceiver(Runnable updateDate) {
        this.updateDate = updateDate;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            // Update date text
            updateDate.run();
        }
    }
}