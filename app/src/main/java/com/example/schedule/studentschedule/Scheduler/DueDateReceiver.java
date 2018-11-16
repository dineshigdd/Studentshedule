package com.example.schedule.studentschedule.Scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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