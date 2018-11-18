package com.example.schedule.studentschedule.View;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ListCourseActivity extends AppCompatActivity {


    private ArrayList<Course> list;
    //private ArrayList<Mentor> mentorList;
    private CourseListAdapter dataAdapter;
    private DbManager dbManager;
    private ListView listCourse;
    private static String termId;
    public static boolean isCourseEditing;
    private static int listPositon;
    private boolean isDeleted;
    static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CourseActivity.BACK_BUTTON_PRESSED = false;
        list = new ArrayList<>();
        //Mentorlist = new ArrayList<>();
        //my code.............................
        Button btnAddCourse = findViewById(R.id.btnAddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CourseActivity.class);
                startActivity(intent);

            }
        });


        setCourse();





//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }

    @Override
    protected void onStart() {
       super.onStart();

           editCourse();



    }

    @Override
    public void onBackPressed() {

        isCourseEditing = false;
    }

    private void editCourse(){

        listCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isCourseEditing = true;

                Intent intent = new Intent(ListCourseActivity.this, CourseActivity.class );
                intent.putExtra("serializeCourseData",list.get(position));
               // Toast.makeText(ListCourseActivity.this, termId,Toast.LENGTH_LONG).show();
                intent.putExtra("termID",termId);
                startActivityForResult(intent, REQUEST_CODE );

            }
        });

    }

//    private String[] combineArray( String[] ArrayOne, String[] ArrayTwo ){
//        int length = ArrayOne.length + ArrayTwo.length;
//        String [] combineArray = new String[length];
//        System.arraycopy(ArrayOne, 0, combineArray, 0, ArrayOne.length);
//        System.arraycopy(ArrayTwo, 0, combineArray, ArrayOne.length, ArrayTwo.length);
//
//        return combineArray;
//    }

        public void setCourse() {

        dbManager = new DbManager(this);
        dbManager.open();

        if( CourseActivity.BACK_BUTTON_PRESSED ) {
            termId = getIntent().getSerializableExtra("EDITCOURSE-TERMID").toString();
        }

        if( !isCourseEditing) {
            termId = getIntent().getSerializableExtra("serializeData").toString();
        }

            listCourse = findViewById(R.id.listCourse);

            try {
                //list = dbManager.getAllCourse();
                //  long numItems = dbManager.getRowCount(DbHelper.TABLE_COURSE);
                //if (numItems != 0) {
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
                        //    mentorList.add(mentor);

                    } while (cursor.moveToNext());
                }
                cursor.close();
                Toast.makeText(this, "List course:" + list.get(0).getItem(), Toast.LENGTH_LONG).show();
                dataAdapter = new CourseListAdapter(this, R.layout.list_course, list);
                listCourse.setAdapter(dataAdapter);

                //     TextView tv = findViewById(R.id.mentorTv);
//            ListView lv = findViewById(R.id.listMentor);
//
//            ArrayAdapter<Mentor> spinnerArrayAdapter = new ArrayAdapter<>(
//                    this, android.R.layout.simple_spinner_dropdown_item, Mentorlist);
//            lv.setAdapter(spinnerArrayAdapter);

            } catch (Exception e) {
                //  noTerm = true;
//            finish();
//            Intent intent = new Intent(getApplicationContext(),TermActivity.class);
//            startActivity(intent);

//            AlertDialog alertDialog = new AlertDialog.Builder(TermActivity.this).create();
//            alertDialog.setTitle("Alert");
//            alertDialog.setMessage("No terms");
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();



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
