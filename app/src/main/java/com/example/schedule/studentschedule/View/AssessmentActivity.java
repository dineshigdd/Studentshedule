package com.example.schedule.studentschedule.View;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.view.DragStartHelper;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.Model.Assessment;
import com.example.schedule.studentschedule.Model.Assign;
import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentschedule.Model.DataItem;
import com.example.schedule.studentschedule.Model.Mentor;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

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
    private static int termID;
    private static int courseID;
    private int spCoursePosition;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        dbManager = new DbManager(this);
        dbManager.open();
        table = DbHelper.TABLE_TERM + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_ASSESSMENT + "," +
                DbHelper.TABLE_ASSIGN;

        //Term spinner-------------------------------------
        spTerm = new Spinner(this);
        mainLayout.addView(spTerm);
        termList = dbManager.getAllTerms();
        ArrayList<String> term = new ArrayList<>();
        courseTitle = new ArrayList<>();
        mapId = new HashMap<>();
        assessmentCounter = 0;


        for (int i = 0; i < termList.size(); i++) {
            term.add(termList.get(i).getItem()+
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

        spCourse = new Spinner(this);
        spCourse.setPrompt("select course");
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
        setDate(tvLabel,tv,310);
        DateLayout.addView(tvLabel);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                date = calendar.get(Calendar.DAY_OF_MONTH);

                // TODO Auto-generated method stub
                datePickerDialog = new DatePickerDialog(AssessmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                tv.setText(month + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, date);

                datePickerDialog.show();
            }
        });
        DateLayout.addView(tv);
        mainLayout.addView(DateLayout);

        //submit -----------------------------------
        submit = new Button(this);
        if( !isEditing ) {
            submit.setText("Add");
        }else{
            submit.setText("Save");
        }
        LinearLayout btnLayout = new LinearLayout(this);
//
        LinearLayout.LayoutParams btnLayoutDimensions = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//
        btnLayoutDimensions.setMargins(275,25,100,0);
        btnLayout.setLayoutParams(btnLayoutDimensions);
        btnLayout.addView(submit);
        mainLayout.addView(btnLayout);


        btnDisplay = new Button(this);
        btnDisplay.setText("Show assessment");
        LinearLayout btnDisplayLayout = new LinearLayout(this);//
        LinearLayout.LayoutParams btnDisplayLayoutDimensions = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//
        btnDisplayLayoutDimensions.setMargins(200,40,100,0);
        btnDisplayLayout.setLayoutParams(btnDisplayLayoutDimensions);
        btnDisplayLayout.addView(btnDisplay);
        mainLayout.addView(btnDisplayLayout);


        setContentView(mainLayout);

        if( !isEditing) {
            submitbtnActionHandler();
        }else{
            spTerm.setEnabled(false);
            spCourse.setEnabled(false);
            setEditData();
            updatebtnHandler();
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

                Intent intent = new Intent(getApplicationContext(),ListAssessmentActivity.class);
                intent.putExtra("TERM_ID",termId);
                intent.putExtra("COURSE_ID",courseId);
                startActivity(intent);

            }
        });

    }

    @SuppressLint("ResourceType")
    private void setEditData() {
        //Load spTerm
        assessment = (Assessment)getIntent().getSerializableExtra("serializeData");
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

                //Edit assessment table
                assessment.setTitle(edText.getText().toString());
                assessment.setType(radValue);
                assessment.setDueDate(tv.getText().toString());

                Log.d("Update ID:", String.valueOf(assessment.getAssessmentId()));
                Log.d("Update AssTitle: ",assessment.getTitle());
                Log.d("Update setType: ",assessment.getType());
                Log.d("Update Due date: ",assessment.getDueDate());

                ContentValues values;
                values = dbManager.setData(assessment, DbHelper.TABLE_ASSESSMENT);
                String condition = DbHelper.ASSESSMENT_ID + "=?";
                dbManager.update(DbHelper.TABLE_ASSESSMENT, values, condition, assessment.getAssessmentId());
                AssessmentActivity.isEditing = false;

                        /*
                Edit assignment table
                int editAssignId =   dbManager.getAssignId(
                termID,
                courseID,
                getMentorId(termID,courseID)
                );


                Assign editAssign = new Assign(
                termID,
                courseID,
                getMentorId(termID,courseID)
                );

                // editAssign.setAssignId(editAssignId);


                values = dbManager.setData(editAssign,DbHelper.TABLE_ASSIGN);
                condition =  DbHelper.ASSIGN_ID + "=?";
                dbManager.update(DbHelper.TABLE_ASSIGN,values,condition,editAssignId);
                */

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
                    boolean isInsertOp = false;
                    Assessment assessmentRecord = dbManager.getAssessment(assessmentId);

                    if (assessmentRecord.getTitle()== null ) {
                        isInsertOp = false; //do not do insert operation
                       Log.d("IsInsert top",  String.valueOf(isInsertOp ));
                    }else {
                        isInsertOp = true;  // do insert
                        Log.d("IsInsert top",  String.valueOf(isInsertOp ));
                    }


                    assessment = new Assessment(
                            edText.getText().toString(),
                            radValue,
                            tv.getText().toString()
                    );


                    ContentValues values;
                    values = dbManager.setData(assessment, "assessment");
                    if( isInsertOp == false ) {
                        String condition = DbHelper.ASSESSMENT_ID + "=?";
                        dbManager.update(DbHelper.TABLE_ASSESSMENT, values, condition, assessmentId);
                    }else if( isInsertOp ){
                        dbManager.insertData(DbHelper.TABLE_ASSESSMENT,values);
                        assessmentId = dbManager.getAllAssesment().get(dbManager.getAllAssesment().size() - 1).getAssessmentId();
                    }

                    values.clear();

//                    ArrayList<Assessment> assessmentList = dbManager.getAllAssesment();
//                    int assessmentId = assessmentList.get(assessmentList.size() - 1).getAssessmentId();

                    Assign assign = new Assign(
                            termId,
                            courseId,
                            mentorId
                    );


                    int assignId = dbManager.getAssignId(termId,courseId,mentorId);
                    Log.d("ASSIGN id:" , String.valueOf(assignId));
                    assign.setAssessmentId(assessmentId);
                    values = dbManager.setData(assign, "assign");
                    Log.d("assessmentCounter BEFORE IF: ", String.valueOf(assessmentCounter));
                    if( isInsertOp == false ) {
                        String condition = DbHelper.ASSIGN_ID + "=?";
                        dbManager.update(DbHelper.TABLE_ASSIGN, values, condition, assignId);
                    }else if(isInsertOp){
                        dbManager.insertData(DbHelper.TABLE_ASSIGN,values);
                    }


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
                     populateSppiner(courseTitle, spCourse);

                if( isEditing ){
                    spCoursePosition = 0;
                    while( spCoursePosition < courseTitle.size() && course.get(spCoursePosition).getItemId() != courseID ){
                        spCoursePosition++;
                    }
                    Log.d("spCoursePosition: " , String.valueOf(spCoursePosition));
                    spCourse.setSelection(spCoursePosition);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            } });


        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position , long id) {

                courseId = dbManager.getCoursesOfTerm(termId).get(position).getItemId();
                assessmentCounter = dbManager.getQueryCount(termId, courseId, table);
                 Toast.makeText(AssessmentActivity.this,"Course Id:" + courseId, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            } });

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
}
