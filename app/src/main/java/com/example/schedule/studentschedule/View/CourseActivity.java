package com.example.schedule.studentschedule.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.MainActivity;
import com.example.schedule.studentschedule.Model.Assign;
import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentschedule.Model.CourseListAdapter;
import com.example.schedule.studentschedule.Model.DataItem;
import com.example.schedule.studentschedule.Model.Mentor;
import com.example.schedule.studentschedule.Scheduler.MyReceiver;
import com.example.schedule.studentschedule.Scheduler.NotificationScheduler;
import com.example.schedule.studentshedule.R;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CourseActivity extends AppCompatActivity {

    private Button submit;
    private Course course;
    private Mentor mentor;
    private EditText edTextNotes;
    private DbManager dbManager;
    protected LinearLayout mainLayout;
    private Spinner spCourse;
    private TextView tv;
    protected TextView startDateTv;
    protected TextView endDateTv;
    private TextView mentorText;
    private EditText mentorEditText;
    private  TextView phone;
    private TextView email;
    protected DatePickerDialog datePickerDialog;
    private Spinner spTerm;
    private int date;
    private int month;
    private int year;
    private Spinner spStatus;
    private EditText phoneEditText;
    private EditText emailEditText;
    private TextView tvTerm;
    private Assign assign;
    private Cursor cursor;
    private Map<String, Integer> mapId;
    private Map<String, Integer> mapCourseId;
    private Integer termId;
    private int courseId;
    private String editCourseTermId;
    private Course editCourse;
    private int mentorId;
    private Mentor editMentor;
    private Assign editAssign;
    private boolean isCustomCourseName;
    private EditText customCourseEditText;
    private RelativeLayout CourseLayout;
    private RelativeLayout edCustomCourseLayout;
    private String courseTitle;
    private int assessmentId;
    private CourseListAdapter dataAdapter;
    private Calendar calendar;
    private RelativeLayout DateLayout;
    private  Intent intent;
    private static CheckBox startCheckBox;
    private CheckBox endCheckBox;
    private String startCheckBoxValue;
    private String endCheckBoxValue;
    public static boolean BACK_BUTTON_PRESSED = false;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbManager = new DbManager(this);
        tv = new TextView(this);
        mapId = new HashMap<>();
        isCustomCourseName = false;



        // This will create the LinearLayout
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        CourseLayout = new RelativeLayout(this);
        edCustomCourseLayout = new RelativeLayout(this);
        customCourseEditText = new EditText(this);

        if( !ListCourseActivity.isCourseEditing) {
            intent = new Intent(CourseActivity.this, AssessmentActivity.class);
        }else{
            intent = new Intent(CourseActivity.this, ListAssessmentActivity.class);
        }
        //Select term ---------------------------------------

        // int[] to = {android.R.id.text1};
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT));
        scroll.addView(mainLayout);



        RelativeLayout termLayout = new RelativeLayout(this);
        spTerm = new Spinner(this);
        spTerm.setId(99);
        tvTerm = addTextView(getString(R.string.term));
        tvTerm.setId(100);
        termLayout.addView(tvTerm);
        RelativeLayout.LayoutParams tvTermDimensions = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvTerm.setLayoutParams(tvTermDimensions);
        RelativeLayout.LayoutParams spTermDimension = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvTermDimensions.addRule(RelativeLayout.ALIGN_BASELINE, spTerm.getId());
        spTermDimension.addRule(RelativeLayout.RIGHT_OF, tvTerm.getId());
        spTermDimension.addRule(RelativeLayout.END_OF, tvTerm.getId());
        spTerm.setLayoutParams(spTermDimension);
        termLayout.addView(spTerm);




        try {

            final ArrayList<DataItem> list = dbManager.getAllTerms();
            ArrayList<String> term = new ArrayList<>();
            mapId = new HashMap<>();

            term.add("Select term");
            for (int i = 0; i < list.size(); i++) {
                term.add(list.get(i).getItem()+
                         " => " +
                        list.get(i).getStartDate() +
                        " to " + list.get(i).getEndDate());
            }

            //Inserting keys and Values
            for (int i = 0; i < list.size(); i++) {
                mapId.put(term.get(i + 1), list.get(i).getItemId());

            }

            populateSppiner(term, spTerm);

            if( ListCourseActivity.isCourseEditing )
            {

                editCourseTermId =  (getIntent().getSerializableExtra("termID").toString());
                int spTermPosition = 0;

                while( (spTermPosition < list.size()) && list.get(spTermPosition).getItemId() != Integer.parseInt(editCourseTermId) ) {
                         spTermPosition++;
                }

               spTerm.setSelection(spTermPosition + 1);//Add one to skip the first Item




               // int spTermPosition = 0;
//                for( int i = 0; i< term.size() &&  termTitle.equals(list.get(i)); i++ ){
//
//                            spTermPosition = i - 1;
//                }


             }


        } catch (Exception e) {
            Toast.makeText(this, "First you must create a term/terms to add a course", Toast.LENGTH_SHORT);
        }


        //if Editing



//        if(!cursor.isAfterLast()){
//            do{
//
//                termName.add(cursor.getColumnIndex(list.get(i).getItem()) + " => " +
//                        cursor.getString(cursor.getColumnIndex(list.get(i).getStartDate())) + " to "
//                        + cursor.getString(cursor.getColumnIndex(list.get(i).getEndDate())));
//
//            }while(cursor.moveToNext());
//        }



        mainLayout.addView(termLayout);

        ///ACTION ON TERM SPINNER SELECT---------------
        spTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //    Map hm = null;
              //  submit.setText(spTerm.getSelectedItem().toString());
                String key = spTerm.getSelectedItem().toString();
                termId = mapId.get(key);

               // Log.d("Term Id in CourseActivity:" , termId );
            //    String termId = ;

                //getKey(mapId,spTerm.getSelectedItem().toString());
               // submit.setText(Integer.toString(termId));
//              Toast.makeText(CourseActivity.this,Integer.toString(termId),Toast.LENGTH_SHORT);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Need for the Listener to work
            }
        });
        
        
        //--------------select the course----------------

        spCourse = new Spinner(this);
        spCourse.setId(200);

        TextView tvCourse = addTextView(getString(R.string.course));
        tvCourse.setId(201);
        CourseLayout.addView(tvCourse);

        RelativeLayout.LayoutParams tvCourseDimensions = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        tvCourse.setLayoutParams(tvCourseDimensions);

        RelativeLayout.LayoutParams spDimension = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        tvCourseDimensions.addRule(RelativeLayout.ALIGN_BASELINE, spCourse.getId());
        spDimension.addRule(RelativeLayout.RIGHT_OF, tvCourse.getId());
        spDimension.addRule(RelativeLayout.END_OF, tvCourse.getId());
        spCourse.setLayoutParams(spDimension);
        CourseLayout.addView(spCourse);


        //Set Course UI Elements
        ArrayList<String> courseTitle = new ArrayList<>();
        courseTitle.add("Select Course");
        courseTitle.add("New Course");
        populateSppiner(courseTitle, spCourse);

        long numItems ;
        numItems = dbManager.getRowCount( DbHelper.TABLE_COURSE );
        if( numItems != 0 ) {
            ArrayList<Course> courseList = dbManager.getAllCourse();
            try {
             //   mapCourseId = new HashMap<>();

                ArrayList<String> courseNames = getCourseName();
                for( int i = 0 ; i < courseNames.size(); i++ ) {
                    courseTitle.add(courseNames.get(i));

                }
                populateSppiner(courseTitle, spCourse);

//                for (int i = 0; i < courseList.size(); i++) {
//                    mapCourseId.put(courseTitle.get(i + 2), courseList.get(i).getItemId());
//
//                }

                populateSppiner(courseTitle, spCourse);

                editCourse = (Course) getIntent().getSerializableExtra("serializeCourseData");
                int spCoursePosition = 0;
                for( int i = 0 ; i < courseTitle.size() && !(editCourse.getItem().equalsIgnoreCase(courseTitle.get(i))); i++){
                    spCoursePosition = i;
                }


//                while ((spCoursePosition < courseTitle.size()) && courseList.get(spCoursePosition).getItemId() != editCourse.getItemId()) {
//                    spCoursePosition++;
//                }
//            for(int i = 0; i < courseList.size() &&
//                    courseList.get(i).getItemId() == course.getItemId(); i ++ ) {
////
//                    spCoursePosition = spCoursePosition + i;
//
//            }

                spCourse.setSelection(spCoursePosition + 1 ); //Add one to skip the first two Items

            } catch (Exception e) {
                Toast.makeText(this, "First you must create a course", Toast.LENGTH_SHORT);
            }

        }else{

            addNewCourse();
        }
        mainLayout.addView(CourseLayout);

        //StartDate and EndDate UI Elements----------------------------------------



//        tv = new TextView(this); // to hold the date from Datepicker dialog
//        tv.setId(554);
        TextView startDateTvLabel = addTextView(getString(R.string.start_date));
        startDateTv= addTextView("");
        startDateTv.setHint(getString(R.string.select_date));
        startDateTv.setId(602);
        setDate(startDateTvLabel, startDateTv, 202);

        RelativeLayout checkBoxLayout = new RelativeLayout(this);
        startCheckBox = new CheckBox(this);
        startCheckBox.setText("Alert Me");
        startCheckBox.setId(603);

        RelativeLayout.LayoutParams checkboxDimension = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        checkboxDimension.addRule(RelativeLayout.END_OF,startDateTv.getId());
        startCheckBox.setLayoutParams(checkboxDimension);

        DateLayout.addView(startCheckBox);

        mainLayout.addView(checkBoxLayout);

        startDateTv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DAY_OF_MONTH);

                // TODO Auto-generated method stub
                datePickerDialog = new DatePickerDialog(CourseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startDateTv.setText(month + 1 + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, date);

                datePickerDialog.show();
            }
        });


        
        
        TextView endDateTvLabel = addTextView(getString(R.string.end_date));
        endDateTv = addTextView("");
        endDateTv.setId(605);
        endDateTv.setHint(getString(R.string.end_date));
        setDate(endDateTvLabel, endDateTv, 203);

        endCheckBox = new CheckBox(this);
        endCheckBox.setText("Alert Me");
        endCheckBox.setId(604);

        RelativeLayout.LayoutParams endCheckboxDimension = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        endCheckboxDimension.addRule(RelativeLayout.END_OF,endDateTv.getId());
        endCheckBox.setLayoutParams(endCheckboxDimension);

        DateLayout.addView(endCheckBox);


        endDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                date = calendar.get(Calendar.DAY_OF_MONTH);

                // TODO Auto-generated method stub
                datePickerDialog = new DatePickerDialog(CourseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endDateTv.setText(month + 1 + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, date);

                datePickerDialog.show();
            }
        });


//--------------------------Status--------------------------------------------------------------

        RelativeLayout statusLayout = new RelativeLayout(this);
        spStatus = new Spinner(this);
        spStatus.setId(204);
        TextView tvStatus = addTextView(getString(R.string.status));
        tvStatus.setId(205);
        statusLayout.addView(tvStatus);
        RelativeLayout.LayoutParams tvStatusDimensions = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvStatus.setLayoutParams(tvStatusDimensions);

        RelativeLayout.LayoutParams spStatusDimension = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvStatusDimensions.addRule(RelativeLayout.ALIGN_BASELINE, spStatus.getId());
        spStatusDimension.addRule(RelativeLayout.RIGHT_OF, tvStatus.getId());
        spStatusDimension.addRule(RelativeLayout.END_OF, tvStatus.getId());
        spStatus.setLayoutParams(spStatusDimension);
        statusLayout.addView(spStatus);

        ArrayList<String> statuslist = new ArrayList();
        statuslist.add("in progress");
        statuslist.add("completed");
        statuslist.add("dropped");
        statuslist.add("plan to take");

        populateSppiner(statuslist,spStatus);

        mainLayout.addView(statusLayout);



        //Mentor data----------------------------------
        RelativeLayout mentorLayout = new RelativeLayout(this);
        TextView tvMentor = addTextView(getString(R.string.mentor_name));
        tvMentor.setId(206);
        RelativeLayout.LayoutParams tvMentorDimensions = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        tvMentor.setLayoutParams(tvMentorDimensions);
        mentorLayout.addView(tvMentor);


        mentorEditText = setEditText(206,207);
        mentorEditText.setHint("Enter mentor name");


        mentorLayout.addView(mentorEditText);
        tvMentorDimensions.addRule(RelativeLayout.ALIGN_BASELINE, mentorEditText.getId());

        mainLayout.addView(mentorLayout);
//        TextView tvMentorPhone = addTextView(getString(R.string.phone));
//        tvMentorPhone.setId(208);
//        mentoredTextDimension.addRule(RelativeLayout.RIGHT_OF, titleLabel.getId());
//        mentoredTextDimension.addRule(RelativeLayout.END_OF, titleLabel.getId());

        ////Phone--------------------------------------------------------
        RelativeLayout phoneLayout = new RelativeLayout(this);
        TextView tvPhone = addTextView(getString(R.string.phone));
        tvPhone.setId(209);
        RelativeLayout.LayoutParams tvPhoneDimensions = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        tvPhone.setLayoutParams(tvPhoneDimensions);
        phoneLayout.addView(tvPhone);

        phoneEditText = setEditText(209,210);
        phoneEditText.setHint("Enter phone number");


        phoneLayout.addView(phoneEditText);
        tvPhoneDimensions.addRule(RelativeLayout.ALIGN_BASELINE, phoneEditText.getId());

        mainLayout.addView(phoneLayout);

        ///----Email--------------------------------------------
        RelativeLayout emailLayout = new RelativeLayout(this);
        TextView tvEmail = addTextView(getString(R.string.email));
        tvEmail.setId(209);
        RelativeLayout.LayoutParams tvEmailDimensions = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        tvEmail.setLayoutParams(tvEmailDimensions);
        emailLayout.addView(tvEmail);

        emailEditText = setEditText(209,210);
        emailEditText.setHint("Enter E-mail          ");//keep this space


        emailLayout.addView(emailEditText);
        tvEmailDimensions.addRule(RelativeLayout.ALIGN_BASELINE, emailEditText.getId());

        mainLayout.addView(emailLayout);

        ///For Notes-------------------
        RelativeLayout NotesLayout = new RelativeLayout(this);
        TextView tvNotesLabel = addTextView(getString(R.string.notes));
        tvNotesLabel.setId(208);
        NotesLayout.addView(tvNotesLabel);

        RelativeLayout.LayoutParams tvNotesLblDimensions = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvNotesLabel.setLayoutParams(tvNotesLblDimensions);

        edTextNotes = new EditText(this);
        edTextNotes.setId(209);
        RelativeLayout.LayoutParams edTextDimension = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        tvNotesLblDimensions.addRule(RelativeLayout.ALIGN_BASELINE, edTextNotes.getId());
        edTextDimension.addRule(RelativeLayout.RIGHT_OF, tvNotesLabel.getId());
        edTextDimension.addRule(RelativeLayout.END_OF, tvNotesLabel.getId());

        edTextNotes.setLayoutParams(edTextDimension);
        NotesLayout.addView(edTextNotes);
        mainLayout.addView(NotesLayout);



        ///  submit button------------------------------
        submit = new Button(this);
        submit.setText("Add Course");
        LinearLayout btnLayout = new LinearLayout(this);
//
        LinearLayout.LayoutParams btnLayoutDimensions = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//
        btnLayoutDimensions.setMargins(205,10,100,0);
        btnLayout.setLayoutParams(btnLayoutDimensions);
        btnLayout.addView(submit);
        mainLayout.addView(btnLayout);

        submitbtnActionHandler();


        Button btnAddAssessment = new Button(this);
        if( ! ListCourseActivity.isCourseEditing) {
            btnAddAssessment.setText(getString(R.string.add_assessment));
        }else{
            btnAddAssessment.setText(getString(R.string.view_assessment));
        }


        LinearLayout btnAssessmentLayout = new LinearLayout(this);
//
        LinearLayout.LayoutParams btnAssessmentLayoutDimension = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//
        btnAssessmentLayoutDimension.setMargins(180,55,100,0);
        btnAssessmentLayout.setLayoutParams(btnAssessmentLayoutDimension);
        btnAssessmentLayout.addView(btnAddAssessment);
        mainLayout.addView(btnAssessmentLayout);

        btnAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( !ListCourseActivity.isCourseEditing ) {
                    intent.putExtra("TERM-ID-FROM-COURSE", termId);
                    intent.putExtra("COURSE-ID-FROM-COURSE", courseId);
                }else{
                    intent.putExtra("TERM-ID-FROM-COURSE", Integer.parseInt(editCourseTermId));
                    intent.putExtra("COURSE-ID-FROM-COURSE", editCourse.getItemId());
                }
                startActivity(intent);
            }

        });





        if( ListCourseActivity.isCourseEditing )
            { //if editing
            startDateTv.setText(editCourse.getStartDate());
            endDateTv.setText(editCourse.getEndDate());

            if(editCourse.getStartDateAlert().equalsIgnoreCase("true")){
                startCheckBox.setChecked(true);
            }else{
                startCheckBox.setChecked(false);
            }



             if(editCourse.getEndDateAlert().equalsIgnoreCase("true")){
                 endCheckBox.setChecked(true);
             }else{
                 endCheckBox.setChecked(false);
             }


            edTextNotes.setText(editCourse.getNotes());

            String status = editCourse.getStatus();
            switch ( status){
                case "in progress":
                    spStatus.setSelection(0);
                    break;
                case "completed":
                    spStatus.setSelection(1);
                    break;
                case "dropped":
                    spStatus.setSelection(2);
                    break;
                case "plan to take":
                    spStatus.setSelection(3);
                    break;
            }
          editMentor = getMentorData();
          mentorEditText.setText(editMentor.getName());
          phoneEditText.setText(editMentor.getPhone());
          emailEditText.setText(editMentor.getEmail());


            submit.setText("Save");
            updateButtonHandler();
        }

        setContentView(scroll);


    }



    protected void populateSppiner(ArrayList list,Spinner sp){

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, list);
        sp.setAdapter(spinnerArrayAdapter);

    }

//    private getSpinnerPosition(String value) {
//
//        ArrayAdapter<String> spinnerArrayAdapter =
//                new ArrayAdapter<String>(
//                        this, android.R.layout.simple_spinner_dropdown_item, list);
//        mSpinner.setAdapter(adapter);
//        if (compareValue != null) {
//            int spinnerPosition = adapter.getPosition(compareValue);
//            mSpinner.setSelection(spinnerPosition);
//        }
//    }

    private void updateButtonHandler(){

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String condition ;
                ContentValues values;
                if( isCustomCourseName ) {
                    courseTitle = customCourseEditText.getText().toString();
                }else {
                    courseTitle = spCourse.getSelectedItem().toString();
                }

                if( startCheckBox.isChecked()){
                    startCheckBoxValue = "true";
                }else{
                    startCheckBoxValue = "false";
                }

                if( endCheckBox.isChecked()){
                    endCheckBoxValue = "true";
                }else{
                    endCheckBoxValue = "false";
                }


                editCourse.setItem(courseTitle);
                editCourse.setStartDate(startDateTv.getText().toString());
                editCourse.setStartDateAlert(startCheckBoxValue);
                editCourse.setEndDate(endDateTv.getText().toString());
                editCourse.setEndDateAlert(endCheckBoxValue);
                editCourse.setStatus(spStatus.getSelectedItem().toString());
                editCourse.setNotes(edTextNotes.getText().toString());


                Log.d("editCourse", editCourse.getStartDateAlert());

                condition =  DbHelper.COURSE_ID + "=?";
                values = dbManager.setData(editCourse,DbHelper.TABLE_COURSE);
                dbManager.update(DbHelper.TABLE_COURSE,values,condition,editCourse.getItemId());

                editMentor.setName(mentorEditText.getText().toString());
                editMentor.setPhone(phoneEditText.getText().toString());
                editMentor.setEmail(emailEditText.getText().toString());

                condition =  DbHelper.MENTOR_ID + "=?";
                values = dbManager.setData(editMentor,DbHelper.TABLE_MENTOR);
                dbManager.update(DbHelper.TABLE_MENTOR,values,condition,editMentor.getMentorId());

                int editAssignId =   dbManager.getAssignId(
                        Integer.parseInt(editCourseTermId),
                        editCourse.getItemId(),
                        editMentor.getMentorId(),
                        DbHelper.TABLE_MENTOR
                );


                editAssign = new Assign(
                        Integer.parseInt(editCourseTermId),
                        editCourse.getItemId(),
                        editMentor.getMentorId()
                );

               // editAssign.setAssignId(editAssignId);

                condition =  DbHelper.ASSIGN_ID + "=?";
                values = dbManager.setData(editAssign,DbHelper.TABLE_ASSIGN);
                dbManager.update(DbHelper.TABLE_ASSIGN,values,condition,editAssignId);

               // ListCourseActivity.isCourseEditing = false;  should revise 11/16/2018
            }
        });

    }

    private void submitbtnActionHandler(){

        dbManager.open();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( isCustomCourseName ) {
                    courseTitle = customCourseEditText.getText().toString();
                }else {
                    courseTitle = spCourse.getSelectedItem().toString();
                }


//                Cursor cursor;
//
//                String selection = DbHelper.COURSE_TITLE + "=?" + " and " +
//                        DbHelper.COURSE_START_DATE + "=?" + " and " + DbHelper.COURSE_END_DATE + "=?";
//
//                cursor = dbManager.query(true,,DbHelper.ALL_COLUMNS_COURSE, selection,
//                        DbHelper.ALL_COLUMNS_COURSE,null,
//                        null,null,null);
//                if( !cursor.moveToFirst() ) {

                    ContentValues values;
                    course = new Course(
                            courseTitle,
                            startDateTv.getText().toString(),
                            endDateTv.getText().toString(),
                            spStatus.getSelectedItem().toString(),
                            edTextNotes.getText().toString()
                    );



                if(startCheckBox.isChecked()){
                    course.setStartDateAlert("true");
                }else{
                    course.setStartDateAlert("false");
                }


                if(endCheckBox.isChecked()){
                    course.setEndDateAlert("true");
                }else{
                    course.setEndDateAlert("false");
                }

                    values = dbManager.setData(course, DbHelper.TABLE_COURSE);
                    dbManager.insertData(DbHelper.TABLE_COURSE, values);
                    values.clear();


                    ArrayList<Course> courseList = dbManager.getAllCourse();
                    courseId = courseList.get(courseList.size() -  1).getItemId();
//                }
//            else{
//                    Toast.makeText(CourseActivity.this,"This Record is already in Database",Toast.LENGTH_SHORT);


                mentor = new Mentor(
                           mentorEditText.getText().toString(),
                           phoneEditText.getText().toString(),
                           emailEditText.getText().toString()
                         );



                values = dbManager.setData(mentor, DbHelper.TABLE_MENTOR);
                dbManager.insertData(DbHelper.TABLE_MENTOR, values);
                values.clear();

                if( dbManager.getRowCount(DbHelper.TABLE_MENTOR) != 0 ) {
                    ArrayList<Mentor> mentorList = dbManager.getAllMentor();
                    mentorId = mentorList.get(mentorList.size() - 1).getMentorId();
                }



                assign = new Assign();

                try {
                    assign.setTermId(termId.intValue());
                    assign.setCourseId(courseId);
                    assign.setMentorId(mentorId);
//                    assign.setAssessmentId(assessmentId);
                }catch (Exception e){

                }

//                long numItems = dbManager.getRowCount(DbHelper.TABLE_ASSIGN);
//                if( numItems == 0) {
                    //Toast.makeText(CourseActivity.this,Long.toString(numItems) ,Toast.LENGTH_SHORT);
                 values = dbManager.setData(assign, "assign");
                 dbManager.insertData(DbHelper.TABLE_ASSIGN,values);


//                }
//                else{
//                    Toast.makeText(CourseActivity.this,"record is already there",Toast.LENGTH_SHORT);
//               }

                NotificationScheduler.showNotification(CourseActivity.this, MyReceiver.class);



            }
        });


    }



    private Mentor getMentorData(){
        dbManager.open();
        String table = DbHelper.TABLE_ASSIGN + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_TERM + "," + DbHelper.TABLE_MENTOR;
        String selection = DbHelper.TABLE_COURSE + "." + DbHelper.COURSE_ID + "=" +  DbHelper.ASSIGN_COURSE_ID +
                " AND " +  DbHelper.ASSIGN_TERM_ID + "=" + DbHelper.TABLE_TERM + "." + DbHelper.TERM_ID +
                " AND " +  DbHelper.TABLE_MENTOR + "." + DbHelper.MENTOR_ID + "=" +  DbHelper.ASSIGN_MENTOR_ID +
                " AND "  + DbHelper.ASSIGN_TERM_ID + "=?" +
                " AND " +  DbHelper.ASSIGN_COURSE_ID + "=?" ;

        String [] selectionArgs = { editCourseTermId , String.valueOf(editCourse.getItemId())};
        Cursor cursor = dbManager.query(true,table,DbHelper.All_COLUMNS_MENTOR,
                selection,selectionArgs,null,null,null,null);

        boolean isMentor =  cursor.moveToFirst();
        Mentor mentor = new Mentor();
        if( isMentor ) {
            if (!cursor.isAfterLast()) {
                do {
                    mentor.setMentorId(cursor.getInt(cursor.getColumnIndex(DbHelper.MENTOR_ID)));
                    mentor.setName(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_NAME)));
                    mentor.setPhone(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_PHONE)));
                    mentor.setEmail(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_EMAIL)));

                } while (cursor.moveToNext());
            }
        }else return null;
        cursor.close();
        return mentor;
    }

    private ArrayList<String> getCourseName(){
        ArrayList<String> list = new ArrayList();
        String [] projection = { DbHelper.COURSE_TITLE };
        Cursor cursor = dbManager.query(true, DbHelper.TABLE_COURSE, projection, null, null, null,
                null, null, null);

        boolean isCourse =  cursor.moveToFirst();
        if( isCourse ) {
            if (!cursor.isAfterLast()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_TITLE)));

                } while (cursor.moveToNext());
            }
        }else return null;
        cursor.close();
        return list;

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

    @SuppressLint("ResourceType")
    private void setDate(TextView tvLabel, TextView tv, int id) {
        DateLayout = new RelativeLayout(this);
        tvLabel.setId(id);
        DateLayout.addView(tvLabel);

        RelativeLayout.LayoutParams DateLayoutDimensions = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        DateLayoutDimensions.addRule(RelativeLayout.RIGHT_OF, tvLabel.getId());
        DateLayoutDimensions.addRule(RelativeLayout.END_OF, tvLabel.getId());

        tv.setLayoutParams(DateLayoutDimensions);
        DateLayout.addView(tv);
        DateLayout.setId(600);
        mainLayout.addView(DateLayout);


    }


    @SuppressLint("ResourceType")
    private EditText setEditText( int idLabel, int id){
        EditText editText;
        editText = new EditText(this);
        editText.setId(id);
        RelativeLayout.LayoutParams edTextDimension = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        editText.setLayoutParams(edTextDimension);
        edTextDimension.addRule(RelativeLayout.RIGHT_OF, idLabel);
        edTextDimension.addRule(RelativeLayout.END_OF, idLabel);

        return editText;

    }

    private void addNewCourse(){
        customCourseEditText = new EditText(CourseActivity.this);
        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if( spCourse.getSelectedItem().equals("New Course") ){
                    isCustomCourseName = true;
                    edCustomCourseLayout = new RelativeLayout(CourseActivity.this);
                    RelativeLayout.LayoutParams edCustomCourseDimension = new RelativeLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    //    if( !ListCourseActivity.isCourseEditing ){
                            customCourseEditText .setHint("Enter the custom name");
                    //   }
                        customCourseEditText .setTextSize(12);
                        edCustomCourseDimension.addRule(RelativeLayout.BELOW, spCourse.getId());
                        edCustomCourseLayout.setLayoutParams(edCustomCourseDimension);
                        edCustomCourseLayout.addView(customCourseEditText );
                        CourseLayout.addView(edCustomCourseLayout);

                }else if( !spCourse.getSelectedItem().equals("New Course") ){
                        isCustomCourseName = false;
                        customCourseEditText.setText("");
                        edCustomCourseLayout.removeView(customCourseEditText);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Need for the Listener to work

            }
        });
    }

    public void removeCourse(){

        //+ "." + DbHelper.TERM_ID + "<>" +
        //  DbHelper.TABLE_ASSIGN + "." + DbHelper.TERM_ID;

        //String [] selectionArg = { Integer.toString(termId) };



        int assignId = dbManager.getAssignId(
                Integer.parseInt(editCourseTermId)
                ,editCourse.getItemId(),
                editMentor.getMentorId(),
                DbHelper.TABLE_MENTOR);

       int assessmentId = dbManager.getAssessmentId(Integer.parseInt(editCourseTermId),editCourse.getItemId());


        String selection  ;
        selection = DbHelper.ASSIGN_ID + "=?";
        dbManager.delete(DbHelper.TABLE_ASSIGN,selection,assignId);

        selection = DbHelper.COURSE_ID + "=?";
        dbManager.delete(DbHelper.TABLE_COURSE, selection,editCourse.getItemId());


//        selection = DbHelper.ASSESSMENT_ID + "=?";
//        dbManager.delete(DbHelper.TABLE_ASSESSMENT, selection,assessmentId);

        //ListCourseActivity.isCourseEditing = false;
        Intent intent = new Intent(getApplicationContext(),ListCourseActivity.class);
        startActivity(intent);

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
                        removeCourse();

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
    protected void onStart() {
        super.onStart();
       spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               addNewCourse();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ListCourseActivity.isCourseEditing = true;
        BACK_BUTTON_PRESSED = true;
        //  finishActivity(ListCourseActivity.REQUEST_CODE);
        Intent intent = new Intent(getApplicationContext(),ListCourseActivity.class);
        intent.putExtra("EDITCOURSE-TERMID", editCourseTermId );
        startActivity(intent);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        if( ListCourseActivity.isCourseEditing) {
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




