package com.example.schedule.studentschedule;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.schedule.studentshedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    static int notificationID;
    String channel_id = "myChannel";
    String COURSE_GROUP = "COURSE-DATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // Toast.makeText(context, "Notification", Toast.LENGTH_LONG).show();

        String date = intent.getSerializableExtra("DATE").toString();
        String startcourse = intent.getSerializableExtra("START-COURSE").toString();
        String endcourse = intent.getSerializableExtra("END-COURSE").toString();

        Toast.makeText(context, startcourse, Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);

        Notification startNotification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(startcourse + " course/courses start Date is on " + date )
                .setContentTitle("Test of Notification with an id of :" + Integer.toString(notificationID))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, startNotification);

        notificationID++;

        Notification endNotification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(endcourse + " course/courses End Date is on " + date )
                .setContentTitle("Test of Notification with an id of :" + Integer.toString(notificationID))
                .setGroup(COURSE_GROUP)
                .build();


         notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
         notificationManager.notify(notificationID, endNotification);


//        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}