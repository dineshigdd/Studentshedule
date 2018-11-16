package com.example.schedule.studentschedule;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.schedule.studentshedule.R;

public class DueDateReceiver extends BroadcastReceiver {


    String channel_id = "myChannel";



    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // Toast.makeText(context, "Notification", Toast.LENGTH_LONG).show();
        Log.d("Status in reciver  ", "assessment receivinng ..");



            String date = intent.getSerializableExtra("DUE-DAY").toString();
            String course = intent.getSerializableExtra("ASSESSMENT").toString();
            DueDateScheduler.showNotification(context, date, course, DueDateScheduler.notificationID);
        }




}