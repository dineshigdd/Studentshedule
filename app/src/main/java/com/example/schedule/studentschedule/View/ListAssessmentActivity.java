package com.example.schedule.studentschedule.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.Model.Assessment;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;



public class ListAssessmentActivity extends AppCompatActivity {

      private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DbManager(this);
        dbManager.open();


        int termId = (int)getIntent().getSerializableExtra("TERM_ID");
        int courseId = (int)getIntent().getSerializableExtra("COURSE_ID");
        //int mentorId = (int)getIntent().getSerializableExtra("MENTOR_ID");


        Log.d("Term ID :", Integer.toString(termId));
        Log.d("Course ID :", Integer.toString(courseId));
    //    Log.d("mentor ID :", Integer.toString(mentorId));

        ArrayList<Assessment> assessmentsForCourse ;

        String table = DbHelper.TABLE_TERM + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_ASSESSMENT + "," +
                DbHelper.TABLE_ASSIGN;

        int numberOfAssessments   = dbManager.getQueryCount(termId,courseId,table);
        Log.d("numberOfAssessments :", Integer.toString(numberOfAssessments));

//        for(int i = 0 ; i < numberOfAssessments ; i++ ){
//            Log.d("Assessemnt ID: ", Integer.toString(dbManager.getAssessmentId(termId,courseId)));
//            assessmentsForCourse.add(
//                    dbManager.getAssessment(dbManager.getAssessmentId(termId,courseId))
//            );
//        }

        assessmentsForCourse = dbManager.getAssessmentOfCourse(termId,courseId,table);

        ArrayList<String> assessmentInfo = new ArrayList<>();
//        for( Assessment data : assessmentsForCourse ) {
//            assessmentInfo.add( data.getTitle() + "\n" + data.getType() + "\n" + data.getDueDate());
//        }
        for(int i = 0 ; i < assessmentsForCourse.size() ; i++ ){
            assessmentInfo.add(assessmentsForCourse.get(i).getTitle());
        }


        ListView lv = findViewById(R.id.listAssessment);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, assessmentInfo);
        lv.setAdapter(listViewAdapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
