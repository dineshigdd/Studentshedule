package com.example.schedule.studentschedule;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.schedule.studentschedule.View.AssessmentActivity;
import com.example.schedule.studentschedule.View.CourseActivity;
import com.example.schedule.studentschedule.View.ListCourseActivity;
import com.example.schedule.studentschedule.View.TermActivity;
import com.example.schedule.studentshedule.R;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationScheduler.showNotification(MainActivity.this,MyReceiver.class);


        Thread dueDataThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(200);
                    DueDateScheduler.showAssessmentNotification(MainActivity.this, DueDateReceiver.class);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        dueDataThread.start();



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
                Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
                startActivity(intent);

            }
        });


        Button btnAddAssessment = findViewById(R.id.btnAddAssessment);
        btnAddAssessment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AssessmentActivity.isEditing = false;
                Intent intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                startActivity(intent);

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
//        NotificationScheduler.isStartAlert = false;
//        NotificationScheduler.showNotification(MainActivity.this, MyReceiver.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }


}


