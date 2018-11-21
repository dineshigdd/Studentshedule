package com.example.schedule.studentschedule.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.MainActivity;
import com.example.schedule.studentschedule.Model.Assessment;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;





public class ListAssessmentActivity extends AppCompatActivity {

      private DbManager dbManager;
      private ArrayList<String> assessmentList;
      private static int termId;
      private static int courseId;
      private ListView listAssessment;
      protected ArrayList<Assessment> list ;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assessment);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbManager = new DbManager(this);
        dbManager.open();


        if( ListCourseActivity.isCourseEditing &&  !AssessmentActivity.isEditing  ){
            termId = (int)getIntent().getSerializableExtra("TERM-ID-FROM-COURSE");
            courseId=(int)getIntent().getSerializableExtra("COURSE-ID-FROM-COURSE");
        }

        if( !ListCourseActivity.isCourseEditing && AssessmentActivity.isEditing ) {
            termId = (int) getIntent().getSerializableExtra("TERM_ID");
            courseId = (int) getIntent().getSerializableExtra("COURSE_ID");
        }
        //int mentorId = (int)getIntent().getSerializableExtra("MENTOR_ID");


        Log.d("Term ID :", Integer.toString(termId));
        Log.d("Course ID :", Integer.toString(courseId));
    //    Log.d("mentor ID :", Integer.toString(mentorId));



        String table = DbHelper.TABLE_TERM + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_ASSESSMENT ;

//        int numberOfAssessments   = dbManager.getQueryCount(termId,courseId,table);
//        Log.d("numberOfAssessments :", Integer.toString(numberOfAssessments));


        list = dbManager.getAssessmentOfCourse(termId,courseId,table);
        if( list.size() == 0 ){
            Toast.makeText( getApplicationContext(), " There are no assessments ", Toast.LENGTH_LONG).show();
            finish();
        }

            assessmentList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                assessmentList.add(
                        list.get(i).getTitle() + "\n" +
                                list.get(i).getType() + "\n" +
                                list.get(i).getDueDate());
            }

            listAssessment = findViewById(R.id.listAssessment);
            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, assessmentList);
            listAssessment.setAdapter(listViewAdapter);




    }


    private void editAssessment() {
        listAssessment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                intent.putExtra("serializeData", list.get(position));
                intent.putExtra("assessment-Id", list.get(position).getAssessmentId());
                intent.putExtra("term-id", termId);
                intent.putExtra("course-id",courseId);
//                intent.putExtra( "list-size",list.size());
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


    @Override
    public void onBackPressed() {
          AssessmentActivity.isEditing = false;

        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        AssessmentActivity.isEditing = false;
        Toast.makeText(this,"goin back",Toast.LENGTH_SHORT).show();
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_home:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_term:
                intent = new Intent(getApplicationContext(), DetailedTermActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_course:
                intent = new Intent(getApplicationContext(), CourseActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_assessment:
                intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                startActivity(intent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
