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
    private static Intent intent;
    private PendingIntent sender;
    String startDay = "";
    String endDay = "";
    String startCourseList = "";
    String endCourseList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStartAlert = false;
        isEndAlert = false;
        isDueDateAlert = false;

        intent = new Intent(getApplicationContext(), MyReceiver.class);
        showNotification();
        showAssessmentNotification();
        startNotification();










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

        if( dbManager.getRowCount(DbHelper.TABLE_ASSESSMENT) > 0 ) {
            ArrayList<Assessment> list = dbManager.getAllAssesment();
            String assessmentList = "";

            //--------------------------------------------------------------------------------------

            long difference;
            String dueDate = "";

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
                        Log.d("list item:", assessmentList);
                        dueDate = list.get(i).getDueDate();

                        isDueDateAlert = true;
                    }

                }

                if (isDueDateAlert) {

                    intent.putExtra("DUE-DATE", dueDate);
                    intent.putExtra("ASSESSMENT", assessmentList);


                }
            } catch (Exception e) {
                intent.putExtra("DUE-DATE", dueDate);
                intent.putExtra("ASSESSMENT", assessmentList);
                e.printStackTrace();
//                   isDueDateAlert = false;

            }
        }
    }





    public void showNotification( ){
        DbManager dbManager = new DbManager(this);
        dbManager.open();

        ArrayList<Course> list = dbManager.getAllCourse();

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
//            String startDay = "";
//            String endDay = "";

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





        } catch (Exception e) {
//            isStartAlert = false;
//            isEndAlert = false;
            intent.putExtra("START-DATE", startDay);
            intent.putExtra("START-COURSE", startCourseList);
            intent.putExtra("END-DATE", endDay);
            intent.putExtra("END-COURSE", endCourseList);

        }

    }

//    @Override
//    protected void onDestroy() {
////
////        showNotification();
////        showAssessmentNotification();
//        startNotification();
//        Log.d("I am","destroy");
//        super.onDestroy();
//    }
//
    @Override
    protected void onStop() {
        //intent = new Intent(this, MyReceiver.class);
        showNotification();
        showAssessmentNotification();
        startNotification();
        Log.d("I am","stop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d("I am","pause");


        startNotification();
        super.onPause();

    }

    @Override
    protected void onResume() {
        Log.d("I am", "resume");
        showNotification();
        showAssessmentNotification();
        startNotification();
        super.onResume();

    }

    private void startNotification() {
//
        if (isStartAlert || isEndAlert) {

            intent.putExtra("START-DATE", startDay);
            intent.putExtra("START-COURSE", startCourseList);
            intent.putExtra("END-DATE", endDay);
            intent.putExtra("END-COURSE", endCourseList);


//                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, 0, sender);
        }

        int delay = 0;
        if (isStartAlert || isEndAlert || isStartAlert) {
            Log.d("start Notification", Boolean.toString(isStartAlert));
            delay = 5000;
        }

            sender = PendingIntent.getBroadcast(getApplicationContext(), MyReceiver.notificationID, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis() + delay, sender);
        }

}
