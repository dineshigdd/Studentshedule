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
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.TableRow;
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


public class NotificationScheduler {
    public static ArrayList<Course> startCourseList;
    public static ArrayList<Course> endCourseList;
    public static String startDay;
    public static boolean isStartAlert;

    public static String endDay;
    public static boolean isEndAlert;
    public static long mills;
    public static String channel_id = "myChannel";
    static int notificationID;
    private  static long startdatedifference;
    private static  SimpleDateFormat df;
    private static ArrayList<PendingIntent> pIntent;
    private static ArrayList<Long> pMills;
    private static ArrayList<AlarmManager> alarmmList;
    private static ArrayList<Course> list;
    private static String startCourse;
    public static void showNotification(final Context context, Class<?> cls) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();




        DbManager dbManager = new DbManager(context);
        dbManager.open();

        list = new ArrayList<>();

        if( !( dbManager.getRowCount(DbHelper.TABLE_COURSE) > 0 )) {
            Toast.makeText(context, "There are no courses",Toast.LENGTH_LONG).show();
        }else {
            list = dbManager.getAllCourse();

            final Runnable myRun = new Runnable() {
                public void run() {

                    EndNotificationScheduler.showNotification(context,EndReceiver.class);

                }
            };

            Handler myHandler = new Handler();
            myHandler.postDelayed(myRun,200);

            //--------------------------------------------------------------------------------------


//        long enddatedifference = 0;
//        // long difference;
            df = new SimpleDateFormat("MM/dd/yyyy");
            Date currentDate = new Date();
            String strCurrentDate = df.format(currentDate);
            startCourseList = new ArrayList<>();
            endCourseList = new ArrayList<>();
            alarmmList = new ArrayList<>();
            pIntent = new ArrayList<>();
//
            try {
                currentDate = df.parse(strCurrentDate);
                Date dbstartDate = null;
                Date dbendDate;
//            String startDay = "";
//            String endDay = "";


                Log.d("list size", Long.toString(list.size()));
                for (int i = 0; i < list.size(); i++) {
                    Log.d("List start date ", i + ":" + list.get(i).getStartDate());
                    Log.d("List start date ", i + ":" + list.get(i).getItem());
                    //dbstartDate = df.parse(list.get(i).getStartDate());

                    //  startdatedifference = dbstartDate.getTime() - currentDate.getTime();


                    if (list.get(i).getStartDateAlert().equalsIgnoreCase("true")) {

                        startCourseList.add(list.get(i));
                    }


                }

                if( startCourseList.size() > 0 ) {

                    for (int i = 0; i < startCourseList.size(); i++) {
                        ComponentName receiver = new ComponentName(context, cls);
                        PackageManager pm = context.getPackageManager();
                        pm.setComponentEnabledSetting(receiver,
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);

                        mills = getMills(startCourseList.get(i).getStartDate());
                        Log.d("smills", Long.toString(mills));


                        notificationID = i;
                        Intent intent1 = new Intent(context, cls);
                        intent1.putExtra("START-DAY", startCourseList.get(i).getStartDate());
                        intent1.putExtra("COURSE", startCourseList.get(i).getItem());
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




    public static void showNotification(Context context, String date, String course ,int notificationID) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(context,channel_id)
                .setContentTitle("Start Date" + Long.toString(notificationID))
                .setContentText("The starting date of "+ course + " is " + date)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText( "The following courses starts on "+ date + "\n"+  course ))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);


    }


}

