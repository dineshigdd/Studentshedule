package com.example.schedule.studentschedule;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.schedule.studentschedule.Model.Assessment;
import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentschedule.Model.DataItem;
import com.example.schedule.studentschedule.Model.Mentor;
import com.example.schedule.studentschedule.View.AssessmentActivity;
import com.example.schedule.studentschedule.View.CourseActivity;
import com.example.schedule.studentschedule.View.DetailedTermActivity;

import com.example.schedule.studentschedule.View.ListAssessmentActivity;
import com.example.schedule.studentschedule.View.ListCourseActivity;
import com.example.schedule.studentschedule.View.TermActivity;
import com.example.schedule.studentshedule.R;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
//        values.put( DbHelper.ASSIGN_ASSESSMENT_ID,DbHelper.ASSESSMENT_ID);
//        values.put( DbHelper.ASSIGN_MENTOR_ID,DbHelper.MENTOR_ID);
//        scheduleDbManager.insertData("assign",values);


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
