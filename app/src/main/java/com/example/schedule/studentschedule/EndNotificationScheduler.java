package com.example.schedule.studentschedule;

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
import android.widget.Toast;

import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentshedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;


public class EndNotificationScheduler {
    public static ArrayList<Course> startCourseList;
    public static ArrayList<Course> endCourseList;
    public static String startDay;
    public static boolean isStartAlert;

    public static String endDay;
    public static boolean isEndAlert;
    public static long mills;
    public static String channel_id = "myEnDChannel";
    public static int notificationID = 1000;
    private  static long startdatedifference;
    private static  SimpleDateFormat df;
    private static ArrayList<PendingIntent> pIntent;
    private static ArrayList<Long> pMills;
    private static ArrayList<AlarmManager> alarmmList;
    private static String startCourse;
    public static void showNotification(final Context context, Class<?> cls) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();



        DbManager dbManager = new DbManager(context);
        dbManager.open();

        ArrayList<Course> list;

        if (!(dbManager.getRowCount(DbHelper.TABLE_COURSE) > 0)) {
            Toast.makeText(context, "There are no courses", Toast.LENGTH_LONG).show();

        } else {

            list = dbManager.getAllCourse();


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
                    Log.d("List End date ", i + ":" + list.get(i).getEndDate());
                    Log.d("List End date ", i + ":" + list.get(i).getItem());

                    if (list.get(i).getEndDateAlert().equalsIgnoreCase("true")) {

                        endCourseList.add(list.get(i));
                    }
                }
//


                for (int i = 0; i < endCourseList.size(); i++) {

                    ComponentName receiver = new ComponentName(context, cls);
                    PackageManager pm = context.getPackageManager();
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);

                    mills = getMills(endCourseList.get(i).getEndDate());
                    Log.d("emills", Long.toString(mills));


                    notificationID = i;
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


    public static void showNotification(Context context, String date, String course, int notificationID) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        Intent notificationIntent = new Intent(context, cls);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(cls);
//        stackBuilder.addNextIntent(notificationIntent);

//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        // NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

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

