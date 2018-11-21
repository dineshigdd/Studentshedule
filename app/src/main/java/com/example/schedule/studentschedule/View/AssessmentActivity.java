package com.example.schedule.studentschedule.View;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.MainActivity;
import com.example.schedule.studentschedule.Scheduler.DueDateReceiver;
import com.example.schedule.studentschedule.Scheduler.DueDateScheduler;
import com.example.schedule.studentschedule.Model.Assessment;
import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentschedule.Model.DataItem;

import com.example.schedule.studentshedule.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AssessmentActivity extends AppCompatActivity{
    private LinearLayout mainLayout;
    private TextView tv;
    private EditText edText;
    private int date;
    private int month;
    private int year;
    private int termId;
    private DatePickerDialog datePickerDialog;
    private Button submit;
    private DbManager dbManager;
    private Assessment assessment;
    private String radValue;
    private String checkBoxValue;
    private  RadioGroup radioGroup;
    private RadioButton typeRadioObjective;
    private RadioButton typeRadioPerformance;
    private Spinner spTerm;
    private Map<String,Integer> mapId;
    private int spTermPosition;
    private ArrayList<DataItem> termList;
    private Spinner spCourse;
    private ArrayList<Course> course;
    private int courseId;
    private ArrayList<String> courseTitle;

    private  int assessmentCounter;
    private Button btnDisplay;
    private int assessmentId;
    private int mentorId;
    private String table;
    public static boolean isEditing;
    private static int  termID;
    private static int courseID;
    private int spCoursePosition;
    private CheckBox checkBox;
    private TextView tvCourseNote;
    private Button btnShareNotes;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        dbManager = new DbManager(this);
        dbManager.open();


        //notification = new startNotification();
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        scroll.addView(mainLayout);



        table = DbHelper.TABLE_TERM + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_ASSESSMENT ;

        //Term spinner-------------------------------------
        spTerm = new Spinner(this);
        mainLayout.addView(spTerm);


            termList = dbManager.getAllTerms();
            ArrayList<String> term = new ArrayList<>();
            courseTitle = new ArrayList<>();
            mapId = new HashMap<>();
            assessmentCounter = 0;


            for (int i = 0; i < termList.size(); i++) {
                term.add(termList.get(i).getItem() +
                        " => " +
                        termList.get(i).getStartDate() +
                        " to " + termList.get(i).getEndDate());
            }


            //Inserting keys and Values
//        for (int i = 0; i < list.size(); i++) {
//            mapId.put(term.get(i + 1), list.get(i).getItemId());
//
//        }

            populateSppiner(term, spTerm);


        //IF THE USER ACCESS THIS ACTIVITY FROM COURSE ACTIVITY
        int termIDfromCourse;
        try {
            termIDfromCourse = (int) getIntent().getSerializableExtra("TERM-ID-FROM-COURSE");
            Log.d("termIDfromCourse", String.valueOf(termIDfromCourse));

            int spTermPosition = 0;
            if (termIDfromCourse > 0) {
                while (spTermPosition < termList.size() && termList.get(spTermPosition).getItemId() != termIDfromCourse) {
                    spTermPosition++;
                }
                spTerm.setSelection(spTermPosition);
                spTerm.setEnabled(false);
            }
        }catch (Exception e){
            termIDfromCourse = 0;
        }




        spCourse = new Spinner(this);
        mainLayout.addView(spCourse);
//


        // ------------------------------------------------
        //create TextView and EditText For Assessment Title
        RelativeLayout assessmentLayout = new RelativeLayout(this);
        TextView titleLabel = addTextView(getString(R.string.title));
        titleLabel.setId(306);


        RelativeLayout.LayoutParams titleLblDimensions = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        titleLabel.setLayoutParams(titleLblDimensions);
        assessmentLayout.addView(titleLabel);

        edText = new EditText(this);
        edText.setId(307);
        edText.setTag(edText.getKeyListener());
        edText.setKeyListener( null );
        RelativeLayout.LayoutParams edTextDimension = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        edText.setLayoutParams(edTextDimension);
        assessmentLayout.addView(edText);

        titleLblDimensions.addRule(RelativeLayout.ALIGN_BASELINE, edText.getId());
        edTextDimension.addRule(RelativeLayout.RIGHT_OF, titleLabel.getId());
        edTextDimension.addRule(RelativeLayout.END_OF, titleLabel.getId());

        mainLayout.addView(assessmentLayout);

        //------------------------------------------------------------------------------

        //Creating Assessment Type
        LinearLayout assessmentTypeLayout = new LinearLayout(this);
        TextView typeLabel = addTextView(getString(R.string.assesment_type));
        typeLabel.setId(308);


        LinearLayout.LayoutParams assessmentTypeLayoutDimension = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        typeLabel.setLayoutParams(assessmentTypeLayoutDimension);
        typeLabel.setId(309);
        assessmentTypeLayout.addView(typeLabel);


        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(radioGroup.VERTICAL);


        typeRadioObjective = new RadioButton(this);
        typeRadioObjective.setId(400);
        typeRadioObjective.setText("Objective Assessment");


        typeRadioPerformance = new RadioButton(this);
        typeRadioPerformance.setId(500);
        typeRadioPerformance.setText("Performance Assessment");

        radioGroup.addView(typeRadioObjective);
        radioGroup.addView(typeRadioPerformance);

        assessmentTypeLayout.addView(radioGroup);

      // assessmentTypeLayoutDimension.addRule(LinearLayout., typeLabel.getId());
        //assessmentTypeLayoutDimension.addRule(RelativeLayout.END_OF, typeLabel.getId());

        mainLayout.addView(assessmentTypeLayout);

        ///Date Picker ........................................................
        RelativeLayout DateLayout = new RelativeLayout(this);

        TextView tvLabel = addTextView(getString(R.string.due_date));
        tvLabel.setId(310);
        tv = addTextView("");
        tv.setHint("Select Due Date");
        tv.setId(311);
        setDate(tvLabel,tv,310);


        checkBox = new CheckBox(this);
        checkBox.setText("Alert Me");
        checkBox.setId(603);

        RelativeLayout.LayoutParams checkboxDimension = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        checkboxDimension.addRule(RelativeLayout.END_OF,tv.getId());
        checkBox.setLayoutParams(checkboxDimension);

        DateLayout.addView(checkBox);
        DateLayout.addView(tvLabel);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DAY_OF_MONTH);

                // TODO Auto-generated method stub
                datePickerDialog = new DatePickerDialog(AssessmentActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                tv.setText(month + 1 + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, date);

                datePickerDialog.show();
            }
        });
        DateLayout.addView(tv);
        mainLayout.addView(DateLayout);

        TextView courseTvLabel = addTextView("Course Notes:");
        mainLayout.addView(courseTvLabel);
        tvCourseNote = addTextView("");
        mainLayout.addView(tvCourseNote);

        btnShareNotes = new Button(this);
      //  btnShareNotes.setBackgroundColor(R.color.colorWhite);
        btnShareNotes.setText("Share Notes");
        btnShareNotes.setTextColor(R.color.colorBlue);
        btnShareNotes.setTextSize(11);
        LinearLayout btnShareNotesLayout = new LinearLayout(this);//
        LinearLayout.LayoutParams btnShareNotesDimensions = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        btnShareNotesDimensions.setMargins(10,15,0,0);
        btnShareNotesLayout.setLayoutParams(btnShareNotesDimensions);
        btnShareNotesLayout.addView(btnShareNotes);
        mainLayout.addView(btnShareNotesLayout);

        //submit -----------------------------------
        submit = new Button(this);
        if( !isEditing ) {
            submit.setText("Add");
        }else{
            submit.setText("Save");
        }
        LinearLayout btnLayout = new LinearLayout(this);

        LinearLayout.LayoutParams btnLayoutDimensions = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        btnLayoutDimensions.setMargins(275,25,100,0);
        btnLayout.setLayoutParams(btnLayoutDimensions);
        btnLayout.addView(submit);
        mainLayout.addView(btnLayout);

        //------------------------------------------
        btnDisplay = new Button(this);
        btnDisplay.setText(getString(R.string.view_assessment));
        LinearLayout btnDisplayLayout = new LinearLayout(this);//
        LinearLayout.LayoutParams btnDisplayLayoutDimensions = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        btnDisplayLayoutDimensions.setMargins(200,40,100,0);
        btnDisplayLayout.setLayoutParams(btnDisplayLayoutDimensions);
        btnDisplayLayout.addView(btnDisplay);
        mainLayout.addView(btnDisplayLayout);


        setContentView(scroll);

        if( !isEditing) {
            submitbtnActionHandler();
        }else if(isEditing){
                spTerm.setEnabled(false);
                spCourse.setEnabled(false);
                    if( dbManager.getRowCount( DbHelper.TABLE_ASSESSMENT ) > 0 ) {
                        setEditData();
                        updatebtnHandler();
                    }
        }

        displayBtnHandler();

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        if(assessmentCounter < 6 ) {
//
//        }else {
//            Toast.makeText(AssessmentActivity.this,"You have reached the max amount",Toast.LENGTH_SHORT).show();
////                    AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentActivity.this);
////                    builder.setMessage("The ")
////                            .setPositiveButton(getString(android.R.string.yes),on)
////                            .setNegativeButton(getString(android.R.string.no))
////                            .show();
//        }




    }


    private void populateSppiner(ArrayList list,Spinner sp){

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, list);
        sp.setAdapter(spinnerArrayAdapter);


    }
    private void displayBtnHandler(){
        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                termID = termId;  //to use later in the updatebtnActionHandler
                courseID = courseId; //to use later in the updatebtnActionHandler
                isEditing = true;
                Intent intent = new Intent(getApplicationContext(),ListAssessmentActivity.class);
                intent.putExtra("TERM_ID",termID);
                intent.putExtra("COURSE_ID",courseID);
                startActivity(intent);

            }
        });

    }

    @SuppressLint("ResourceType")
    private void setEditData() {
        //Load spTerm
        assessment = (Assessment)getIntent().getSerializableExtra("serializeData");
        termID = (int)getIntent().getSerializableExtra("term-id");
        courseID = (int)getIntent().getSerializableExtra("course-id");
        int spTermPosition = 0;
        while( spTermPosition < termList.size() && termList.get(spTermPosition).getItemId()!= termID ){
            spTermPosition = spTermPosition + 1;
        }
//        Log.d("Asseesment ID:",String.valueOf(assessment.getAssessmentId()));
//        Log.d("spPosition: " , String.valueOf(spTermPosition));
//        Log.d("Term ID: " , String.valueOf(termID));
//        Log.d("Course ID: " , String.valueOf(courseID));
        //setting data
        spTerm.setSelection( spTermPosition );

        edText.setText(assessment.getTitle());

        if( assessment.getType().equals("objective")){
            radioGroup.check(400);
        }else{
            radioGroup.check(500);
        }

        tv.setText(assessment.getDueDate());

        if( assessment.getDueDateAlert().equalsIgnoreCase("true")) {
           //    Log.d("Assessment getAlert",assessment.getDueDateAlert());
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

    }

    @SuppressLint("ResourceType")
    private void updatebtnHandler(){

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == 400) {
                    radValue = "objective";
                } else {
                    radValue = "performance";
                }

                if (checkBox.isChecked()) {
                    checkBoxValue = "true" ;
                } else {
                    checkBoxValue = "false";
                }



                //Edit assessment table
                assessment.setTitle(edText.getText().toString());
                assessment.setType(radValue);
                assessment.setDueDate(tv.getText().toString());
                assessment.setDueDateAlert(checkBoxValue);
                assessment.setTermID(termID);
                assessment.setCourseID(courseID);

                Log.d("Update ID:", String.valueOf(assessment.getAssessmentId()));
                Log.d("Update AssTitle: ",assessment.getTitle());
                Log.d("Update setType: ",assessment.getType());
                Log.d("Update Due date: ",assessment.getDueDate());

                ContentValues values;
                values = dbManager.setData(assessment, DbHelper.TABLE_ASSESSMENT);
                String condition = DbHelper.ASSESSMENT_ID + "=?";
                dbManager.update(DbHelper.TABLE_ASSESSMENT, values, condition, assessment.getAssessmentId());
                AssessmentActivity.isEditing = false;

            }
        });

    }

    private void submitbtnActionHandler(){

        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                if ( assessmentCounter < 5 ) {
                    if (radioGroup.getCheckedRadioButtonId() == 400) {
                        radValue = "objective";
                    } else {
                        radValue = "performance";
                    }



                    mentorId = getMentorId(termId, courseId);
                    Log.d("mENTOR id:" , String.valueOf(mentorId));
                    assessmentId = dbManager.getAssessmentId(termId,courseId);
                    Log.d("Assessment in Assessment Activtivity id:" , String.valueOf(assessmentId));
                    //get the record for corresponding assessment in the assessment table
//                    boolean isInsertOp = false;
//                    Assessment assessmentRecord = dbManager.getAssessment(assessmentId);
//
//                    if (assessmentRecord.getTitle()== null ) {
//                        isInsertOp = false; //do not do insert operation
//                       Log.d("IsInsert top",  String.valueOf(isInsertOp ));
//                    }else {
//                        isInsertOp = true;  // do insert
//                        Log.d("IsInsert top",  String.valueOf(isInsertOp ));
//                    }
//

                     assessment = new Assessment(
                            edText.getText().toString(),
                            radValue,
                            tv.getText().toString(),
                            termId,
                            courseId
                    );

                    if(checkBox.isChecked()){

                        assessment.setDueDateAlert("true");

                    }else{

                        assessment.setDueDateAlert("false");
                    }


                    ContentValues values;
                    values = dbManager.setData(assessment, DbHelper.TABLE_ASSESSMENT);
                    dbManager.insertData(DbHelper.TABLE_ASSESSMENT,values);
//                    if( isInsertOp == false ) {
//                        String condition = DbHelper.ASSESSMENT_ID + "=?";
//                        dbManager.update(DbHelper.TABLE_ASSESSMENT, values, condition, assessmentId);
//                    }else if( isInsertOp ){
//                        dbManager.insertData(DbHelper.TABLE_ASSESSMENT,values);
//                        assessmentId = dbManager.getAllAssesment().get(dbManager.getAllAssesment().size() - 1).getAssessmentId();
//                    }

//                    values.clear();
//
////                    ArrayList<Assessment> assessmentList = dbManager.getAllAssesment();
////                    int assessmentId = assessmentList.get(assessmentList.size() - 1).getAssessmentId();
//
//                    Assign assign = new Assign(
//                            termId,
//                            courseId,
//                            mentorId
//                    );


//                    int assignId = dbManager.getAssignId(termId,courseId,mentorId, DbHelper.TABLE_MENTOR);
//                    Log.d("ASSIGN id:" , String.valueOf(assignId));
//                    assign.setAssessmentId(assessmentId);
//                    values = dbManager.setData(assign, "assign");
//                    Log.d("assessmentCounter BEFORE IF: ", String.valueOf(assessmentCounter));
//                    if( isInsertOp == false ) {
//                        String condition = DbHelper.ASSIGN_ID + "=?";
//                        dbManager.update(DbHelper.TABLE_ASSIGN, values, condition, assignId);
//                    }else if(isInsertOp){
//                        dbManager.insertData(DbHelper.TABLE_ASSIGN,values);
//                    }

                     DueDateScheduler.showAssessmentNotification( AssessmentActivity.this, DueDateReceiver.class);
                } else {
                    Toast.makeText(AssessmentActivity.this, "You have reached the max amount", Toast.LENGTH_SHORT).show();

                }

                //getting the number of assessment for a course in a particular term



                    assessmentCounter = dbManager.getQueryCount(termId, courseId, table);
                    Log.d("counter assessmentCounter :", String.valueOf(assessmentCounter));


            }


        });


    }



    @Override
    protected void onStart() {
        super.onStart();
            setupAssessment();
            shareCourseNote();
            //ArrayList<String> list = dbManager.getDate(DbHelper.COURSE_START_DATE);
            edText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    edText.setKeyListener((KeyListener) edText.getTag());
                }
            });

//    btnDisplay.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            displayBtnHandler();
//        }
//    });



    }

    private void setupAssessment(){
        spTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position , long id) {

                termId = termList.get(position).getItemId();
                Toast.makeText(AssessmentActivity.this,Integer.toString(termId),Toast.LENGTH_LONG).show();
//                courseTitle = dbManager.getCoursesTitleOfTerm(termId);
                course = dbManager.getCoursesOfTerm(termId);
                courseTitle = new ArrayList<>();
                for( int i = 0 ;i < course.size(); i++ ) {
                      courseTitle.add(
                      course.get(i).getItem() + " => "
                    + course.get(i).getStartDate() + " to "
                    + course.get(i).getEndDate());
                }
                     populateSppiner( courseTitle, spCourse );




                if( isEditing ){
                    spCoursePosition = 0;
                    while( spCoursePosition < courseTitle.size() && course.get(spCoursePosition).getItemId() != courseID ){
                        spCoursePosition++;
                    }
                    Log.d("spCoursePosition: " , String.valueOf(spCoursePosition));

                    spCourse.setSelection(spCoursePosition);

                }

                int courseIDfromCourse;
                try {
                    courseIDfromCourse = (int) getIntent().getSerializableExtra("COURSE-ID-FROM-COURSE");
                    int spCoursePosition = 0;
                    if (courseIDfromCourse > 0) {
                        while (spCoursePosition < course.size() && course.get(spCoursePosition).getItemId() != courseIDfromCourse) {
                            spCoursePosition++;
                        }
                        spCourse.setSelection(spCoursePosition);
                        spCourse.setEnabled(false);
                    }
                }catch(Exception e){
                    courseIDfromCourse = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            } });


        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position , long id) {
                Course course = dbManager.getCoursesOfTerm(termId).get(position);
                courseId = course.getItemId();
                assessmentCounter = dbManager.getQueryCount(termId, courseId, table);

                tvCourseNote.setText(course.getNotes());
                Toast.makeText(AssessmentActivity.this,"Course Id:" + courseId, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            } });

    }

    private void  shareCourseNote(){
        btnShareNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, tvCourseNote.getText());

                startActivity(sendIntent);
            }
        });
    }

    private TextView addTextView(String label) {
        TextView tv = new TextView(this);
        RelativeLayout.LayoutParams tvLayoutDimensions = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(tvLayoutDimensions);
        tv.setText(label);
        tv.setPadding(8, 8, 8, 8);

        return tv;

    }


    private void setDate(TextView tvLabel,TextView tv, int id) {

        RelativeLayout.LayoutParams DateLayoutDimensions = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        DateLayoutDimensions.addRule(RelativeLayout.RIGHT_OF, tvLabel.getId());
        DateLayoutDimensions.addRule(RelativeLayout.END_OF, tvLabel.getId());
        tv.setLayoutParams(DateLayoutDimensions);


    }


    private int getMentorId(int termId, int courseId){
        dbManager.open();
        int mentorId = 0;
        String table = DbHelper.TABLE_ASSIGN + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_TERM + "," + DbHelper.TABLE_MENTOR;
        String selection = DbHelper.TABLE_COURSE + "." + DbHelper.COURSE_ID + "=" + DbHelper.TABLE_ASSIGN + "." +  DbHelper.ASSIGN_COURSE_ID +
                " AND " + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_TERM_ID + "=" + DbHelper.TABLE_TERM + "." + DbHelper.TERM_ID +
                " AND " + DbHelper.TABLE_MENTOR + "." + DbHelper.MENTOR_ID + "=" + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_MENTOR_ID +
                " AND "  + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_TERM_ID + "=?" +
                " AND " + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_COURSE_ID + "=?" ;

        String [] selectionArgs = { String.valueOf(termId), String.valueOf(courseId) };
        Cursor cursor = dbManager.query(true,table,DbHelper.All_COLUMNS_MENTOR,
                selection,selectionArgs,null,null,null,null);

        boolean isMentor =  cursor.moveToFirst();

        if( isMentor ) {
            if (!cursor.isAfterLast()) {
                do {
                    mentorId = cursor.getInt(cursor.getColumnIndex(DbHelper.MENTOR_ID));
//                    mentor.setName(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_NAME)));
//                    mentor.setPhone(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_PHONE)));
//                    mentor.setEmail(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_EMAIL)));

                } while (cursor.moveToNext());
            }
        }else return -1;
        cursor.close();
        return mentorId;
    }

    private void removeAssessment(){

        int assessmentId = (int) getIntent().getSerializableExtra("assessment-Id");

//        int assessmentId = dbManager.getAssessmentId(termId,courseId);

        String selection  ;

        Log.d("assessmentId :", String.valueOf(assessmentId));
        selection = DbHelper.ASSESSMENT_ID + "=?";
        dbManager.delete(DbHelper.TABLE_ASSESSMENT, selection,assessmentId);

            Intent intent = new Intent(getApplicationContext(), ListAssessmentActivity.class);
            startActivity(intent);
            AssessmentActivity.isEditing = false;
            ListCourseActivity.isCourseEditing = false;
            // displayBtnHandler();
            finish();
    }

    private AlertDialog deleateConfirmation()
    {
        AlertDialog alertDialog =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        removeAssessment();

                        dialog.dismiss();
                    }

                })


                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return alertDialog;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        if( isEditing ) {
            menu.add(0, 0, 0, "Delete").
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if( item.getItemId() == 0 ) {
            AlertDialog diaBox = deleateConfirmation();
            diaBox.show();
        }



        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_home:
                finish();
                isEditing = false;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_term:
                finish();
                isEditing = false;
                intent = new Intent(getApplicationContext(), DetailedTermActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_course:
                finish();
                isEditing = false;
                intent = new Intent(getApplicationContext(), CourseActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_assessment:
                finish();
                isEditing = false;
                intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                startActivity(intent);
                break;
        }



        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
