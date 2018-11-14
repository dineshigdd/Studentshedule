package com.example.schedule.studentschedule;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.schedule.studentshedule.R;

public class MyReceiver extends BroadcastReceiver {


    String channel_id = "myChannel";



    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // Toast.makeText(context, "Notification", Toast.LENGTH_LONG).show();
        Log.d("Status in reciver  ", "receivinng ..");
      createNotificationChannel(context, channel_id);
       // NotificationScheduler.startNotification(context,MainActivity.class);
        //if( NotificationScheduler.isStartAlert )
        {

                    String date = intent.getSerializableExtra("START-DAY").toString();
                    String course = intent.getSerializableExtra("COURSE").toString();
                   // NotificationScheduler.notificationID++;
                    NotificationScheduler.showTESTNotification(context, date, course , NotificationScheduler.notificationID);




//                NotificationScheduler.isStartAlert = false;
//
//                if( NotificationScheduler.isEndAlert ){
//                    String date = intent.getSerializableExtra("END-DAY").toString();
//                    String course = intent.getSerializableExtra("COURSE").toString();
//                    NotificationScheduler.showTESTNotification(context, date, course);
//                }


        }

//       if( MainActivity.isStartAlert )
       {
        //    Log.d("Status in reciver inside IF", "receivinng ..");
//            String startDate = intent.getSerializableExtra("START-DATE").toString();
//            String startcourse = intent.getSerializableExtra("START-COURSE").toString();
          //  Toast.makeText(context, startcourse, Toast.LENGTH_LONG).show();

           // PendingIntent sender = PendingIntent.getActivity(context,notificationID,intent,0);


//            Notification startNotification = new NotificationCompat.Builder(context, channel_id)
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .setContentText(/*startcourse+*/ " course/courses start Date is on " /*+ startDate*/ )
//                    .setContentTitle("Start Day Of Courses")
//                    //.setStyle(new NotificationCompat.BigTextStyle()
//                      //  .bigText( "The following courses starts on "+ /*startDate */+ "\n"+ /*startcourse*/ ))*
//                    .build();
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(notificationID, startNotification);
//            MainActivity.isStartAlert = false;

      }
   //     notificationID++;

//        if( MainActivity.isEndAlert ) {
//
//            String endDate = intent.getSerializableExtra("END-DATE").toString();
//            String endCourse = intent.getSerializableExtra("END-COURSE").toString();
//            Log.d("endDate",endDate);
//         //   String endcourse = intent.getSerializableExtra("END-COURSE").toString();
//
//            Notification endNotification = new NotificationCompat.Builder(context, channel_id)
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .setContentText(endCourse+ " course/courses End Date is on " + endDate )
//                    .setContentTitle("End Day Of Courses")
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                    .bigText( ( "The following courses ends on "+ endDate + "\n"+ endCourse )))
//                    .build();
//
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(notificationID, endNotification);
//            MainActivity.isEndAlert = false;
//        }
//
//
//
//        notificationID++;
//
//        if( MainActivity.isDueDateAlert ) {
//
//            String dueDate = intent.getSerializableExtra("DUE-DATE").toString();
//            String assessment = intent.getSerializableExtra("ASSESSMENT").toString();
//
//            //   String endcourse = intent.getSerializableExtra("END-COURSE").toString();
//
//            Notification endNotification = new NotificationCompat.Builder(context, channel_id)
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .setContentText(assessment+ " Due Date is on " + dueDate )
//                    .setContentTitle("Assessment Due Date")
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText( ( "The following Assessment ends on "+ dueDate + "\n"+ assessment )))
//                    .build();
//
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(notificationID, endNotification);
//            MainActivity.isDueDateAlert = false;
//        }
//
//        notificationID++;
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