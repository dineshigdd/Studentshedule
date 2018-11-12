package com.example.schedule.studentschedule;

import android.app.Activity;
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
import android.util.TimeUtils;
import android.widget.Toast;

import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentschedule.View.CourseActivity;
import com.example.schedule.studentshedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;


public class NotificationShedulaer {
    public static ArrayList<Course> startCourseList;
    public static String startDay;
    public static boolean isStartAlert;
    public static String endCourseList;
    public static String endDay;
    public static boolean isEndAlert;
    public static long mills;
    public static String channel_id = "myChannel";
    static int notificationID;
    private  static long startdatedifference;
    private static  SimpleDateFormat df;
    private static ArrayList<PendingIntent> pIntent;
    private static ArrayList<Long> pMills;
    private static String startCourse;

    public static void showNotification(Context context, Class<?> cls) {
        DbManager dbManager = new DbManager(context);
        dbManager.open();

        ArrayList<Course> list = null;

        if( dbManager.getRowCount(DbHelper.TABLE_COURSE) > 0 ) {
            list = dbManager.getAllCourse();
        }else {
            Toast.makeText(context, "There are no courses",Toast.LENGTH_LONG).show();

        }

        //--------------------------------------------------------------------------------------


//        long enddatedifference = 0;
//        // long difference;
        df = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        String strCurrentDate = df.format(currentDate);
//
        try {
            currentDate = df.parse(strCurrentDate);
            Date dbstartDate = null;
            Date dbendDate;
//            String startDay = "";
//            String endDay = "";

//
            for (int i = 0; i < list.size(); i++) {
                Log.d("List start date " , i + ":" + list.get(i).getStartDate());
                dbstartDate = df.parse(list.get(i).getStartDate());
                Log.d("dbstartDate.getTime() ", Long.toString(dbstartDate.getTime()));
                Log.d("current.getTime() ", Long.toString(currentDate.getTime()));
                startdatedifference = dbstartDate.getTime() - currentDate.getTime();
//
                Log.d("Day to mills ",Long.toString(TimeUnit.DAYS.toMillis(1)));
                Log.d("startdatedifference", Long.toString(startdatedifference));
//                Log.d("Db Date", Long.toString(dbstartDate.getTime() ));
//
                if (list.get(i).getStartDateAlert().equalsIgnoreCase("true") && startdatedifference == 0) {

                    Toast.makeText(context, "start", Toast.LENGTH_LONG).show();
                    Log.d("startdatedifference inside in", Long.toString(startdatedifference));
                    startCourseList.add(list.get(i));
                    dbstartDate = df.parse(list.get(i).getStartDate());
                    pMills.add(dbstartDate.getTime());
                    startCourse = startCourse + "\n" + list.get(i).getItem();
                    mills = dbstartDate.getTime() - TimeUnit.DAYS.toMillis(1);
                    dbstartDate = df.parse(list.get(i).getStartDate());


                    //  isStartAlert = true;
                }
                else{
                    // isStartAlert =  false;
                }

                dbManager.close();

//                dbendDate = df.parse(list.get(i).getEndDate());
//                mills = dbendDate.getTime();
//                enddatedifference =mills - currentDate.getTime();
//
//                if (list.get(i).getEndDateAlert().equalsIgnoreCase("true") && enddatedifference == 0) {
//                    Toast.makeText(context, "End ", Toast.LENGTH_LONG).show();
//
//                    endCourseList = endCourseList + "\n" + list.get(i).getItem();
//                    endDay = list.get(i).getEndDate();
//                    isEndAlert = true;
//                }
            }
//
//
            Log.d("isAlert", Boolean.toString(isStartAlert));


            {

                ComponentName receiver = new ComponentName(context, cls);
                PackageManager pm = context.getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);


                        Intent intent1 = new Intent(context, cls);
                        intent1.putExtra("START-DAY", dbstartDate);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,notificationID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        Log.d("mills", Long.toString(mills));
                        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, mills, AlarmManager.INTERVAL_DAY, pendingIntent);


            }
//
//
        } catch (Exception e) {
//            isStartAlert = false;
//            isEndAlert = false;
//            intent.putExtra("START-DATE", startDay);
//            intent.putExtra("START-COURSE", startCourseList);
//            intent.putExtra("END-DATE", endDay);
//            intent.putExtra("END-COURSE", endCourseList);

        }

        //String sDate = "11/12/2018 ";
        // String sDate = startDay;

//        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");


//            Date date = df.parse(sDate);
//            Log.d("date after parse", date.toString());
//
//            mills = date.getTime();
//            Log.d("mills", Long.toString(mills));





    }


//    public static void startNotification(Context context,Class<?> cls) {
//
////        if (isStartAlert || isEndAlert) {
////
////            intent.putExtra("START-DATE", startDay);
////            intent.putExtra("START-COURSE", startCourseList);
////            intent.putExtra("END-DATE", endDay);
////            intent.putExtra("END-COURSE", endCourseList);
////
////
////
////        }
////
////        int delay = 0;
////        if (isStartAlert || isEndAlert || isStartAlert) {
////            Log.d("start Notification", Boolean.toString(isStartAlert));
////            delay = 5000;
////        }
//
//
//        ComponentName receiver = new ComponentName(context, cls);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//
//
//        Intent intent = new Intent(context, cls);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//
//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, mills, AlarmManager.INTERVAL_DAY, pendingIntent);
//    }


    public static void showTESTNotification(Context context, Class<?> cls, Date date) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        Intent notificationIntent = new Intent(context, cls);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(cls);
//        stackBuilder.addNextIntent(notificationIntent);

//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        // NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Notification notification = new NotificationCompat.Builder(context,channel_id)
                .setContentTitle("Start Date")
                .setContentText("The starting date of "+ startCourseList + " is " + df.format(date))
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText( "The following courses starts on "+ df.format(date) + "\n"+  startCourseList ))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);

    }


}

