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
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.Model.Assessment;
import com.example.schedule.studentshedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class DueDateScheduler  {

    private static final int BASE_ID = 3000;
    public static boolean isAssessmentAlert;
    private static long mills;
    private static ArrayList<Assessment> assessmentList;
    public static int notificationID;
    private static ArrayList<PendingIntent> pIntent;
    private static ArrayList<AlarmManager> alarmmList;
    public static String channel_id = "assessmentChannel";


    public static void showAssessmentNotification(final Context context, Class<?> cls) {
        DbManager dbManager = new DbManager(context);
        dbManager.open();

        ArrayList<Assessment> list= null;

        if ((dbManager.getRowCount(DbHelper.TABLE_ASSESSMENT) < 0)) {
            Toast.makeText(context, "There are no Assessments", Toast.LENGTH_LONG).show();

        } else {
            list = dbManager.getAllAssesment();

            //--------------------------------------------------------------------------------------

            assessmentList = new ArrayList<>();
            pIntent = new ArrayList<>();
            alarmmList = new ArrayList<>();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date currentDate = new Date();
            String strCurrentDate = df.format(currentDate);

            try {
//                currentDate = df.parse(strCurrentDate);
//                Date dbDate;

                for (int i = 0; i < list.size(); i++) {
                    Log.d("list item:", list.get(i).toString());
                    Log.d("Due status:", list.get(i).getDueDateAlert());

                    if (list.get(i).getDueDateAlert().equalsIgnoreCase("true")) {

                        assessmentList.add(list.get(i));
                        //isAssessmentAlert = true;
                    }

                }

                if( assessmentList.size() > 0 ) {


                    for (int i = 0; i < assessmentList.size(); i++) {
                        ComponentName receiver = new ComponentName(context, cls);
                        PackageManager pm = context.getPackageManager();
                        pm.setComponentEnabledSetting(receiver,
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);

                        mills = getMills(assessmentList.get(i).getDueDate());
                        Log.d("Amills", Long.toString(mills));
//
//
                        notificationID = BASE_ID + i;
                        Intent intent = new Intent(context, cls);
                        intent.putExtra("DUE-DAY", assessmentList.get(i).getDueDate());
                        intent.putExtra("ASSESSMENT", assessmentList.get(i).getTitle());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                                notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        pIntent.add(pendingIntent);
                        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        alarmmList.add(am);
                        alarmmList.get(i).setInexactRepeating(AlarmManager.RTC_WAKEUP, mills, AlarmManager.INTERVAL_DAY, pIntent.get(i));

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

    public static void showNotification(Context context, String date, String assessment, int notificationID) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context,channel_id)
                .setContentTitle("Due Date" + Long.toString(notificationID))
                .setContentText("The Due date of "+ assessment + " is " + date)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText( "The following assessment due on "+ date + "\n"+  assessment ))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);


    }
}
