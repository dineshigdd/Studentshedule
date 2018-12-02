package com.example.schedule.studentschedule.Scheduler;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentshedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;


public class EndNotificationScheduler {
    private static final int BASE_ID = 1000;
    public static ArrayList<Course> startCourseList;
    public static ArrayList<Course> endCourseList;


    public static long mills;
    public static String channel_id = "myEnDChannel";
    public static int notificationID;

    private static  SimpleDateFormat df;
    private static ArrayList<PendingIntent> pIntent;
    private static ArrayList<AlarmManager> alarmmList;

    public static void showNotification(final Context context, Class<?> cls) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();



        DbManager dbManager = new DbManager(context);
        dbManager.open();

        ArrayList<Course> list;

      if ( dbManager.getRowCount(DbHelper.TABLE_COURSE) > 0 )  {

            list = dbManager.getAllCourse();


            //--------------------------------------------------------------------------------------
            df = new SimpleDateFormat("MM/dd/yyyy");
            Date currentDate = new Date();

            startCourseList = new ArrayList<>();
            endCourseList = new ArrayList<>();
            alarmmList = new ArrayList<>();
            pIntent = new ArrayList<>();

            try {


                Log.d("list size", Long.toString(list.size()));
                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).getEndDateAlert().equalsIgnoreCase("true")) {

                        endCourseList.add(list.get(i));
                    }
                }

                if( endCourseList.size() > 0 ) {


                    for (int i = 0; i < endCourseList.size(); i++) {

                        ComponentName receiver = new ComponentName(context, cls);
                        PackageManager pm = context.getPackageManager();
                        pm.setComponentEnabledSetting(receiver,
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);

                        mills = getMills(endCourseList.get(i).getEndDate());



                        notificationID = BASE_ID + i;
                        Intent intent1 = new Intent(context, cls);
                        intent1.putExtra("END-DAY", endCourseList.get(i).getEndDate());
                        intent1.putExtra("COURSE", endCourseList.get(i).getItem());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                                notificationID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                        pIntent.add(pendingIntent);
                        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        alarmmList.add(am);
                        alarmmList.get(i).set(AlarmManager.RTC_WAKEUP, mills, pIntent.get(i));


                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }


        private static long getMills(String date){
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date parseDate = null;
            try {
                parseDate = df.parse(date);

            }catch (Exception e){

            }
            return parseDate.getTime();
        }



    public static void showNotification(Context context, String date, String course, int notificationID) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        Notification notification = new NotificationCompat.Builder(context,channel_id)
                .setContentTitle("End Date" + Long.toString(notificationID))
                .setContentText("The Ending date of "+ course + " is " + date)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText( "The following courses Ends on "+ date + "\n"+  course ))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);

    }


}

