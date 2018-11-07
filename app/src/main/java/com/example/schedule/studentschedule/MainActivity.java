package com.example.schedule.studentschedule;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.schedule.studentschedule.Model.Assessment;
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

    public static boolean isStartAlert;
    public static boolean isEndAlert;
    public static boolean isDueDateAlert;
    private Intent intent;
    private PendingIntent sender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStartAlert = false;
        isEndAlert = false;
        isDueDateAlert = false;

        intent = new Intent(this, MyReceiver.class);


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
        int delay = 0;
        showNotification();
        showAssessmentNotification();
        if( isStartAlert || isEndAlert || isStartAlert ) {
            delay = 10000;
        }
            sender = PendingIntent.getBroadcast(this, 0, intent, 0);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis() + delay , sender);





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



    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main,menu);
            return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    public void showAssessmentNotification() {
        DbManager dbManager = new DbManager(this);
        dbManager.open();

        ArrayList<Assessment> list = dbManager.getAllAssesment();
        String assessmentList = "";

        //--------------------------------------------------------------------------------------

        long difference;
        String dueDate="";

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        String strCurrentDate = df.format(currentDate);

        try {
            currentDate = df.parse(strCurrentDate);
            Date dbDate;

            for (int i = 0; i < list.size(); i++) {
                Log.d("list item:", list.get(i).toString());
                dbDate = df.parse(list.get(i).getDueDate());
                difference = dbDate.getTime() - currentDate.getTime();

                Log.d("difference", Long.toString(difference));

                if (list.get(i).getDueDateAlert().equalsIgnoreCase("true") && difference == 0) {

                    assessmentList = assessmentList + "\n" + list.get(i).getTitle();
                    Log.d("list item:",assessmentList);
                    dueDate = list.get(i).getDueDate();

                    isDueDateAlert = true;
                }

            }

            if( isDueDateAlert ) {

                intent.putExtra("DUE-DATE", dueDate);
                intent.putExtra("ASSESSMENT", assessmentList);


            }
            }catch(Exception e){
                   isDueDateAlert = false;

        }
    }





    public void showNotification( ){
        DbManager dbManager = new DbManager(this);
        dbManager.open();

        ArrayList<Course> list = dbManager.getAllCourse();
        String startCourseList = "";
        String endCourseList = "";
        //--------------------------------------------------------------------------------------

        long startdatedifference = 0;
        long enddatedifference = 0;
       // long difference;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        String strCurrentDate = df.format(currentDate);

        try {
            currentDate = df.parse(strCurrentDate);
            Date dbstartDate;
            Date dbendDate;

            String startDay = "";
            String endDay = "";
            for (int i = 0; i < list.size(); i++) {

                dbstartDate = df.parse(list.get(i).getStartDate());
                startdatedifference = dbstartDate.getTime() - currentDate.getTime();


                Log.d("List size", Integer.toString(list.size()));
                if (list.get(i).getStartDateAlert().equalsIgnoreCase("true") && startdatedifference == 0) {
                    Toast.makeText(this, "start", Toast.LENGTH_LONG).show();

                    startCourseList = startCourseList + "\n" + list.get(i).getItem();
                    startDay = list.get(i).getStartDate();

                    isStartAlert = true;
                }

                dbendDate = df.parse(list.get(i).getEndDate());
                enddatedifference = dbendDate.getTime() - currentDate.getTime();

                if (list.get(i).getEndDateAlert().equalsIgnoreCase("true") && enddatedifference == 0) {
                    Toast.makeText(this, "End ", Toast.LENGTH_LONG).show();

                    endCourseList = endCourseList + "\n" + list.get(i).getItem();
                    endDay = list.get(i).getEndDate();
                    isEndAlert = true;
                }
            }


            if (isStartAlert || isEndAlert) {

                intent.putExtra("START-DATE", startDay);
                intent.putExtra("START-COURSE", startCourseList);
                intent.putExtra("END-DATE", endDay);
                intent.putExtra("END-COURSE", endCourseList);


//                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, 0, sender);
            }


        } catch (Exception e) {
            isStartAlert = false;
            isEndAlert = false;

        }

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
