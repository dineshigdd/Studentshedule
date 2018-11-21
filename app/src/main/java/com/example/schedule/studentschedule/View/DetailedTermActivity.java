package com.example.schedule.studentschedule.View;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.MainActivity;
import com.example.schedule.studentschedule.Model.DataItem;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;
import java.util.Calendar;


public class DetailedTermActivity extends AppCompatActivity {
    protected LinearLayout mainLayout;
    private Spinner sp;
    private TextView tv;
    protected DatePickerDialog datePickerDialog;
    private int date;
    private int month;
    private int year;
    protected TextView startDateTv;
    protected TextView endDateTv;
    private TextView tvTerm;
    protected DbManager dbManager;
    private DataItem term;
    private Button submit;
    private String termTitle;
    private EditText edCustomTerm;
    private boolean isCustomTermName;
    private ArrayList list;
    private RelativeLayout termLayout;
    private DataItem editTerm;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // This will create the LinearLayout


        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        sp = new Spinner(this);
        edCustomTerm = new EditText(this);
        startDateTv= addTextView("");
        endDateTv = addTextView("");
        submit = new Button(this);


        //---TextView and Spinner
        termLayout = new RelativeLayout(this);
        sp.setId(99);

        tvTerm = addTextView(getString(R.string.term));
        tvTerm.setId(100);
        termLayout.addView(tvTerm);
        RelativeLayout.LayoutParams tvTermDimensions = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tvTerm.setLayoutParams(tvTermDimensions);
        RelativeLayout.LayoutParams spDimesnsion = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tvTermDimensions.addRule(RelativeLayout.ALIGN_BASELINE, sp.getId());
        spDimesnsion.addRule(RelativeLayout.RIGHT_OF, tvTerm.getId());
        spDimesnsion.addRule(RelativeLayout.END_OF, tvTerm.getId());
        sp.setLayoutParams(spDimesnsion);
        termLayout.addView(sp);

        list = new ArrayList();
        list.add("Select Term");
        list.add("Spring");
        list.add("Summer");
        list.add("Fall");
        list.add("Winter");
        list.add("Other");


        populateSppiner(list,sp);


        if( TermActivity.isTermEditing ) {
            setEditTerm();
        }


        mainLayout.addView(termLayout);

        ///----


        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               if( sp.getSelectedItem().equals("Other")){
                   isCustomTermName = true;
                   RelativeLayout edCustomTermLayout = new RelativeLayout(DetailedTermActivity.this);
                   RelativeLayout.LayoutParams edCustomTermDimension = new RelativeLayout.LayoutParams(
                           LayoutParams.MATCH_PARENT,
                           LayoutParams.WRAP_CONTENT);
                   if( !TermActivity.isTermEditing ){
                      edCustomTerm.setHint("Enter the custom name(Ex:term-1)");
                   }
                   edCustomTerm.setTextSize(12);
                   edCustomTermDimension.addRule(RelativeLayout.BELOW, sp.getId());
                   edCustomTermLayout.setLayoutParams(edCustomTermDimension);
                   edCustomTermLayout.addView(edCustomTerm);
                   termLayout.addView(edCustomTermLayout);

               }
//               else if(!sp.getSelectedItem().equals("Other")){
//                   edCustomTerm.setText("");
//                   edCustomTerm.setVisibility(View.INVISIBLE);
//               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback

            }
        });




        //--------------------------------------------------------------

        tv = new TextView(this); // to hold the date from Datepicker dialog

        TextView startDateTvLabel = addTextView(getString(R.string.start_date));

        startDateTv.setHint(getString(R.string.select_date));
        setDate(startDateTvLabel, startDateTv, 101);
        startDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DAY_OF_MONTH);

                // TODO Auto-generated method stub
                datePickerDialog = new DatePickerDialog(DetailedTermActivity.this,
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

        endDateTv.setId(102);
        endDateTv.setHint(getString(R.string.end_date));
        setDate(endDateTvLabel, endDateTv, 103);
        endDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DAY_OF_MONTH);

                // TODO Auto-generated method stub
                datePickerDialog = new DatePickerDialog(DetailedTermActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endDateTv.setText(month + 1 + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, date);

                datePickerDialog.show();
            }
        });


        //attach the Linear layout to the current Activity.
        //Creating  submit button

        LinearLayout btnLayout = new LinearLayout(this);
//
        LinearLayout.LayoutParams btnLayoutDimensions = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);//
        btnLayoutDimensions.setMargins(175, 10, 100, 0);
        btnLayout.setLayoutParams(btnLayoutDimensions);

        if( !TermActivity.isTermEditing ) {

            submit.setText("Add Term");

//            mainLayout.addView(btnLayout);
            submitbtnActionHandler();

        }else{
//            submit = new Button(this);
//           btnLayoutDimensions.setMargins(175, 10, 100, 0);
//           btnLayout.setLayoutParams(btnLayoutDimensions);
           submit.setText("Save");
//           btnLayout.addView(btnUpdate);
//           mainLayout.addView(btnLayout);
           updateTermRecordHandler();

       }

        btnLayout.addView(submit);
        mainLayout.addView(btnLayout);
     //   submitbtnActionHandler();

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        scroll.addView(mainLayout);

        setContentView(scroll);



    }

    private  void updateTermRecordHandler(){
        dbManager = new DbManager(this);
        dbManager.open();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isCustomTermName ) {
                    termTitle = edCustomTerm.getText().toString();
                }else {
                    termTitle = sp.getSelectedItem().toString();
                }

                editTerm.setItem(termTitle);
                editTerm.setStartDate(startDateTv.getText().toString());
                editTerm.setEndDate( endDateTv.getText().toString());

                String condition =  DbHelper.TERM_ID + "=?";

                ContentValues values = dbManager.setData(editTerm,DbHelper.TABLE_TERM);
                dbManager.update(DbHelper.TABLE_TERM,values,condition,editTerm.getItemId());
                TermActivity.isTermEditing = false;
                Toast.makeText(DetailedTermActivity.this,"The term Data has been updated", Toast.LENGTH_LONG).show();

            }
        });




   }


    private void submitbtnActionHandler(){
        dbManager = new DbManager(this);
        dbManager.open();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( isCustomTermName ) {
                    termTitle = edCustomTerm.getText().toString();
                }else {
                    termTitle = sp.getSelectedItem().toString();
                }
                Cursor cursor;
                String [] columns = { DbHelper.TERM_TITLE , DbHelper.TERM_START_DATE , DbHelper.TERM_END_DATE};

                String selection = DbHelper.TERM_TITLE + "=?" + " and " +
                        DbHelper.TERM_START_DATE + "=?" + " and " + DbHelper.TERM_END_DATE + "=?";

                cursor = dbManager.query(true,DbHelper.TABLE_TERM,columns, selection,columns,null,
                        null,null,null);
                if( !cursor.moveToFirst()) {
                    term = new DataItem(
                            termTitle,
                            startDateTv.getText().toString(),
                            endDateTv.getText().toString());

                    ContentValues values =dbManager.setData(term,DbHelper.TABLE_TERM);
                    dbManager.insertData(DbHelper.TABLE_TERM,values);
                   // TermActivity.setNoTerm(false);
                }else{
                    Toast.makeText(DetailedTermActivity.
                            this,"This Record is already in Database",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    protected void populateSppiner(ArrayList list,Spinner sp){

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, list);
        sp.setAdapter(spinnerArrayAdapter);


    }



    protected TextView addTextView(String label) {
        TextView tv = new TextView(this);
        RelativeLayout.LayoutParams tvLayoutDimensions = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(tvLayoutDimensions);
        tv.setText(label);
        tv.setPadding(8, 8, 8, 8);

        return tv;

    }

    private void setDate(TextView tvLabel,TextView tv, int id) {
        RelativeLayout DateLayout = new RelativeLayout(this);
        tvLabel.setId(id);
        DateLayout.addView(tvLabel);

        RelativeLayout.LayoutParams DateLayoutDimensions = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        DateLayoutDimensions.addRule(RelativeLayout.RIGHT_OF, tvLabel.getId());
        DateLayoutDimensions.addRule(RelativeLayout.END_OF, tvLabel.getId());

        tv.setLayoutParams(DateLayoutDimensions);
        DateLayout.addView(tv);
        mainLayout.addView(DateLayout);


    }


    public void setEditTerm(){

        editTerm = (DataItem)getIntent().getSerializableExtra("serializeData");


        String term = editTerm.getItem();
      //load spinner with the selected term
        switch( term.toLowerCase() ){
            case  "spring":
                sp.setSelection(1);
                break;
            case  "summer":
                sp.setSelection(2);
                break;
            case  "fall":
                sp.setSelection(3);
                break;
            case  "winter":
                sp.setSelection(4);
                break;
            default:
                sp.setSelection(5);
                //edCustomTerm.setText(term);

                break;
        }


        startDateTv.setText(editTerm.getStartDate());
        endDateTv.setText((editTerm.getEndDate()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if( sp.getSelectedItem().equals("Other")) {
                    isCustomTermName = true;
                    edCustomTerm = new EditText(DetailedTermActivity.this);
                    RelativeLayout edCustomTermLayout = new RelativeLayout(DetailedTermActivity.this);
                    RelativeLayout.LayoutParams edCustomTermDimension = new RelativeLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    if ( !TermActivity.isTermEditing ){
                        edCustomTerm.setHint("Enter the custom name(Ex:term-1)");
                    }else {
                        edCustomTerm.setText(editTerm.getItem());
                        edCustomTerm.requestFocus();
                    }
                    edCustomTerm.setTextSize(12);
                    edCustomTermDimension.addRule(RelativeLayout.BELOW, sp.getId());
                    edCustomTermLayout.setLayoutParams(edCustomTermDimension);
                    edCustomTermLayout.addView(edCustomTerm);
                    termLayout.addView(edCustomTermLayout);

                }
               else if(!sp.getSelectedItem().equals("Other")){
                   isCustomTermName = false;
                   edCustomTerm.setText("");
                   edCustomTerm.setVisibility(View.GONE);
                  }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback

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


        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_home:
                TermActivity.isTermEditing = false;
                finish();
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_term:
                TermActivity.isTermEditing = false;
                finish();
                intent = new Intent(getApplicationContext(), DetailedTermActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_course:
                TermActivity.isTermEditing = false;
                finish();
                intent = new Intent(getApplicationContext(), CourseActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_assessment:
                TermActivity.isTermEditing = false;
                finish();
                intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                startActivity(intent);
                break;
        }




        return super.onOptionsItemSelected(item);
    }
}











