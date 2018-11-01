package com.example.schedule.studentschedule.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.Model.Assessment;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;





public class ListAssessmentActivity extends AppCompatActivity {

      private DbManager dbManager;
      private ArrayList<String> assessmentList;
      private int termId;
      private int courseId;
      private ListView listAssessment;
      private ArrayList<Assessment> list ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AssessmentActivity.isEditing = false;
        dbManager = new DbManager(this);
        dbManager.open();


        termId = (int)getIntent().getSerializableExtra("TERM_ID");
        courseId = (int)getIntent().getSerializableExtra("COURSE_ID");
        //int mentorId = (int)getIntent().getSerializableExtra("MENTOR_ID");


        Log.d("Term ID :", Integer.toString(termId));
        Log.d("Course ID :", Integer.toString(courseId));
    //    Log.d("mentor ID :", Integer.toString(mentorId));



        String table = DbHelper.TABLE_TERM + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_ASSESSMENT + "," +
                DbHelper.TABLE_ASSIGN;

//        int numberOfAssessments   = dbManager.getQueryCount(termId,courseId,table);
//        Log.d("numberOfAssessments :", Integer.toString(numberOfAssessments));


        list = dbManager.getAssessmentOfCourse(termId,courseId,table);
        assessmentList = new ArrayList<>();

        for(int i = 0 ; i < list.size() ; i++ ){
            assessmentList.add(
                    list.get(i).getTitle() + "\n" +
                    list.get(i).getType() + "\n" +
                    list.get(i).getDueDate());
        }


        listAssessment = findViewById(R.id.listAssessment);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, assessmentList);
        listAssessment.setAdapter(listViewAdapter);


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


    private void editAssessment() {
        listAssessment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                intent.putExtra("serializeData", list.get(position));
                AssessmentActivity.isEditing = true;
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        editAssessment();

    }




}
