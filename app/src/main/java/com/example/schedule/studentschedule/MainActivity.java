package com.example.schedule.studentschedule;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentschedule.View.AssessmentActivity;
import com.example.schedule.studentschedule.View.CourseActivity;
import com.example.schedule.studentschedule.View.ListCourseActivity;
import com.example.schedule.studentschedule.View.TermActivity;
import com.example.schedule.studentshedule.R;
import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements Serializable {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




//        final Runnable myR = new Runnable() {
//            public void run() {
//                displayNotification();
//            }
//        };



//        DbManager scheduleDbManager = new DbManager(this);
//
//        //inserting  term Data
//        ContentValues values= new ContentValues();
//        DataItem sTerm = new DataItem("Spring","2018-10-20","2019-10-20");
//
//
//        scheduleDbManager.insertData( sTerm,"term");
//        values.clear();
//
//        Course sCourse = new Course("Math","2018-11-20", "2019-11-20","complted","Test Note");
//        scheduleDbManager.insertData(sCourse,"course");
//        values.clear();
//
//        Assessment sAssessment = new Assessment("Cs Test","performance","2019-11-20");
//        scheduleDbManager.insertData(sAssessment,"assessment");
//        values.clear();
//
//        Mentor sMentor = new Mentor("Dinesh","818984","d@yahoo.com");
//        scheduleDbManager.insertData(sMentor,"mentor");
//
//        values.clear();

//        Assign sAssign = new Assign(
//        values.put( DbHelper.ASSIGN_TERM_ID,DbHelper.TERM_ID);
//        values.put( DbHelper.ASSIGN_COURSE_ID,DbHelper.COURSE_ID);
//        values.put( DbHelper.ASSIGN_MENTOR_ID,DbHelper.MENTOR_ID);
//        scheduleDbManager.insertData("assign",values);





//        Handler handler = new Handler();
////        handler.postDelayed(myR, datedifference);
        DbManager dbManager = new DbManager(this);
        dbManager.open();


        //ArrayList <String> list = dbManager.getDate(DbHelper.TABLE_COURSE,DbHelper.COURSE_START_DATE);

        ArrayList <Course> list = dbManager.getAllCourse();
        String startCourseList = "";
        String endCourseList = "";
        //--------------------------------------------------------------------------------------

        long startdatedifference = 0;
        long enddatedifference = 0;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        String strCurrentDate = df.format(currentDate);

        try {
            currentDate = df.parse(strCurrentDate);

            Intent intent = new Intent(this, MyReceiver.class);
          //  Intent eintent = new Intent(this, MyReceiver.class);

            for (int i = 0; i < list.size(); i++) {

                Date dbstartDate = df.parse(list.get(i).getStartDate());
                Date dbendDate = df.parse(list.get(i).getEndDate());

                startdatedifference = dbstartDate.getTime() - currentDate.getTime();
                enddatedifference = dbendDate.getTime() - currentDate.getTime();

                if ( startdatedifference == 0 ) {
                    startCourseList = startCourseList + " " + list.get(i).getItem();
                    Toast.makeText(this, "This is a start" + startCourseList, Toast.LENGTH_LONG).show();
                    intent.putExtra("DATE", list.get(i).getStartDate());
                    intent.putExtra("START-COURSE", startCourseList);

                }

                if( enddatedifference == 0){
                    endCourseList = endCourseList + " " + list.get(i).getItem();
                    Toast.makeText(this, "This is end date" + endCourseList, Toast.LENGTH_LONG).show();
                    intent.putExtra("DATE", list.get(i).getEndDate());
                    intent.putExtra("END-COURSE", endCourseList);

                }

            }



                PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, startdatedifference, sender);

//            PendingIntent sender1= PendingIntent.getBroadcast(this, 0, eintent, 0);
//            AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, enddatedifference , sender);



            // Log.d("This is in the list:" , strCurrentDate+ "\n");
                //Toast.makeText(this, "This is a test" + datedifference, Toast.LENGTH_LONG).show();







        } catch (Exception e) {
            e.printStackTrace();
        }


        //--------------------------------------------------------------------------------------







        Button btnTerm = findViewById(R.id.term);
        btnTerm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), TermActivity.class);
                startActivity(intent);

            }
        });


        Button btnAddCourse = findViewById(R.id.btnAddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ListCourseActivity.isCourseEditing = false;
                Intent intent = new Intent(getApplicationContext(),CourseActivity.class);
                startActivity(intent);

            }
        });


        Button btnAddAssessment = findViewById(R.id.btnAddAssessment);
        btnAddAssessment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AssessmentActivity.isEditing = false;
                Intent intent = new Intent(getApplicationContext(),AssessmentActivity.class);
                startActivity(intent);

            }
        });

    }

    private void displayNotification() {

//        Intent intent=new Intent( context,MyReceiver.class);
//        PendingIntent sender= PendingIntent.getBroadcast(context,0,intent,0);
//        //  AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        AlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000, sender);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent i = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "MyID-107");
        notificationBuilder.setAutoCancel(true).setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground).
                setContentTitle("title").setContentText("This is a test message");


        assert notificationManager != null;
        notificationManager.notify(1, notificationBuilder.build());
    }


    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main,menu);
            return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    protected void onPause() {
        super.onPause();
        scheduleDbManager.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduleDbManager.open();
    }
    */
}
