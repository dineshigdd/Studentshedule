package com.example.schedule.studentschedule;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.schedule.studentschedule.Scheduler.DueDateReceiver;
import com.example.schedule.studentschedule.Scheduler.DueDateScheduler;
import com.example.schedule.studentschedule.Scheduler.MyReceiver;
import com.example.schedule.studentschedule.Scheduler.NotificationScheduler;
import com.example.schedule.studentschedule.View.AssessmentActivity;
import com.example.schedule.studentschedule.View.CourseActivity;
import com.example.schedule.studentschedule.View.DetailedTermActivity;
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

        try {
            Button btnAddAssessment = findViewById(R.id.btnAddAssessment);
            btnAddAssessment.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AssessmentActivity.isEditing = false;
                    Intent intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                    startActivity(intent);

                }
            });
        }catch ( Exception e){
            //Toast.makeText( getApplicationContext() , " There are no assessments", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch( item.getItemId() ) {
            case R.id.menu_home:
                intent = new Intent(getApplicationContext(), MainActivity.class);

                break;

            case R.id.menu_term:
                intent = new Intent(getApplicationContext(), DetailedTermActivity.class);

                break;

            case R.id.menu_course:
                intent = new Intent(getApplicationContext(), CourseActivity.class);

                break;

            case R.id.menu_assessment:
                intent = new Intent(getApplicationContext(), AssessmentActivity.class);

                break;
        }

        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }


}


