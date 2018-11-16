package com.example.schedule.studentschedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class EndReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("Receving", "recieving endRecieve");
        String date = intent.getSerializableExtra("END-DAY").toString();
        String course = intent.getSerializableExtra("COURSE").toString();
        EndNotificationScheduler.showNotification(context, date, course, EndNotificationScheduler.notificationID );

        // throw new UnsupportedOperationException("Not yet implemented");
    }
}
