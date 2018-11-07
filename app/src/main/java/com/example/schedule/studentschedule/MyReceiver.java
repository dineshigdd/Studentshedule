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

        createNotificationChannel(context, channel_id);

        if( MainActivity.isStartAlert ) {

            String startDate = intent.getSerializableExtra("START-DATE").toString();
            String startcourse = intent.getSerializableExtra("START-COURSE").toString();
            Toast.makeText(context, startcourse, Toast.LENGTH_LONG).show();

            Notification startNotification = new NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(startcourse + " course/courses start Date is on " + startDate )
                    .setContentTitle("Start Day Of Courses")
                    .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText( "The following courses starts on "+ startDate + "\n"+ startcourse ))
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID, startNotification);
            MainActivity.isStartAlert = false;

        }
        notificationID++;

        if( MainActivity.isEndAlert ) {

            String endDate = intent.getSerializableExtra("END-DATE").toString();
            String endCourse = intent.getSerializableExtra("END-COURSE").toString();
            Log.d("endDate",endDate);
         //   String endcourse = intent.getSerializableExtra("END-COURSE").toString();

            Notification endNotification = new NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(endCourse+ " course/courses End Date is on " + endDate )
                    .setContentTitle("End Day Of Courses")
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText( ( "The following courses ends on "+ endDate + "\n"+ endCourse )))
                    .build();


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID, endNotification);
            MainActivity.isEndAlert = false;
        }



        notificationID++;

        if( MainActivity.isDueDateAlert ) {

            String dueDate = intent.getSerializableExtra("DUE-DATE").toString();
            String assessment = intent.getSerializableExtra("ASSESSMENT").toString();

            //   String endcourse = intent.getSerializableExtra("END-COURSE").toString();

            Notification endNotification = new NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(assessment+ " Due Date is on " + dueDate )
                    .setContentTitle("Assessment Due Date")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText( ( "The following Assessment ends on "+ dueDate + "\n"+ assessment )))
                    .build();


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID, endNotification);
            MainActivity.isEndAlert = false;
        }

        notificationID++;
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