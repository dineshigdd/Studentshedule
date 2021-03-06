package com.example.schedule.studentschedule.View;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.MainActivity;
import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentschedule.Model.CourseListAdapter;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;

public class    ListCourseActivity extends AppCompatActivity {


    private ArrayList<Course> list;
    private CourseListAdapter dataAdapter;
    private DbManager dbManager;
    private ListView listCourse;
    private static String termId;
    public static boolean isCourseEditing;
    private static int listPositon;
    private boolean isDeleted;
    static final int REQUEST_CODE = 1;
    public static boolean isAddCourseToSelectedTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        list = new ArrayList<>();
            setCourse();

    }

    @Override
    protected void onStart() {
       super.onStart();

       editCourse();

        Button btnAddCourse = findViewById(R.id.btnAddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isCourseEditing = false;
                isAddCourseToSelectedTerm = true;
                Intent intent = new Intent(getApplicationContext(),CourseActivity.class);
                intent.putExtra("SELECTED-TERM", termId);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
       // isCourseEditing = false;
    }

    private void editCourse(){

        listCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isCourseEditing = true;

                Intent intent = new Intent(ListCourseActivity.this, CourseActivity.class );
                intent.putExtra("serializeCourseData",list.get(position));
               // Toast.makeText(ListCourseActivity.this, termId,Toast.LENGTH_LONG).show();
                Log.d("term ID in edit course",termId);
                intent.putExtra("termID",termId);
                startActivity(intent);

            }
        });

    }



        public void setCourse() {
        Log.d("is CourseEditing in setCourse", Boolean.toString(isCourseEditing));
        Log.d("isBacK in CourseActitvty", Boolean.toString(CourseActivity.BACK_BUTTON_PRESSED));

        dbManager = new DbManager(this);
        dbManager.open();


            if( !isCourseEditing) {
            termId = getIntent().getSerializableExtra("serializeData").toString();
        }

            listCourse = findViewById(R.id.listCourse);



            try {

                String table = DbHelper.TABLE_ASSIGN + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_TERM;
                String[] id = {String.valueOf(termId)};
                String[] projection = DbHelper.ALL_COLUMNS_COURSE;
                String selection =
                        DbHelper.TABLE_COURSE + "." + DbHelper.COURSE_ID + "=" + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_COURSE_ID +
                                " AND " + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_TERM_ID + "=" + DbHelper.TABLE_TERM + "." + DbHelper.TERM_ID +
                                " AND " + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_TERM_ID + "=?";

                Cursor cursor = dbManager.query(true,
                        table,
                        projection,
                        selection,
                        id,
                        null,
                        null,
                        null,
                        null);

                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    do {
                        Course course = new Course();
                        course.setItemId(cursor.getInt(cursor.getColumnIndex(DbHelper.COURSE_ID)));
                        course.setItem(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_TITLE)));
                        course.setStartDate(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_START_DATE)));
                        course.setStartDateAlert(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_START_ALERT)));
                        course.setEndDate(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_END_DATE)));
                        course.setEndDateAlert(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_END_ALERT)));
                        course.setStatus(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_STATUS)));
                        course.setNotes(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_NOTES)));

                        list.add(course);


                    } while (cursor.moveToNext());
                }else{

                    Toast.makeText(getApplicationContext(), " There are no courses in this term ", Toast.LENGTH_LONG).show();
                    finish();
                }
                cursor.close();

                dataAdapter = new CourseListAdapter(this, R.layout.list_course, list);
                listCourse.setAdapter(dataAdapter);


                isCourseEditing = false;


            } catch (Exception e) {

        }

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
                finish();
                isCourseEditing = false;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_term:
                finish();
                isCourseEditing = false;
                intent = new Intent(getApplicationContext(), DetailedTermActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_course:
                finish();
                isCourseEditing = false;
                intent = new Intent(getApplicationContext(), CourseActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_assessment:
                finish();
                isCourseEditing = false;
                intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                startActivity(intent);
                break;
        }



        return super.onOptionsItemSelected(item);
    }



}
