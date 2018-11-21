package com.example.schedule.studentschedule.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.schedule.studentschedule.DbHelper;
import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.MainActivity;
import com.example.schedule.studentschedule.Model.DataItem;
import com.example.schedule.studentschedule.Model.TermListAdapter;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;

public class TermActivity extends AppCompatActivity {

    DbManager dbManager;
    private ArrayList<DataItem> list;
    private ArrayList<Button> btn;
    private static int listPosition;
    private ListView listTerm;
    private int termId;
    public static boolean isTermEditing = false;
    private TermListAdapter dataAdapter;
    private Button btnDelete;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        listPosition = -1;
        setTerms();

        Button btnAddATerm = findViewById(R.id.btnAddTerm);
        btnAddATerm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DetailedTermActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

            setTerms();
            manageListView();


    }


    public void removeTerm(){
        String selection = DbHelper.TERM_ID + "=?";


        if( dbManager.getRowCount(DbHelper.TABLE_TERM) != 0) {
            try {
                dbManager.delete(DbHelper.TABLE_TERM, selection, termId);
                list.remove(listPosition);
                dataAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Toast.makeText(this, "This terms has one or more course. Please, delete all the" +
                        " associated courses first to delete the term", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "There are no terms to delete", Toast.LENGTH_SHORT).show();
        }

    }

    public  void brnDeleteClickHandler(View view) {

        isTermEditing = false;

        if(listPosition < 0 ) {
            Toast.makeText(getApplicationContext(),
                    "Please, first Click on the record to select it", Toast.LENGTH_LONG).show();
        }else{

            try {
                removeTerm();
            } catch (Exception e) {
                Toast.makeText(this, "One or more course are assigned to this term. " +
                                "You must first remove all courses to delete the term",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public void BtnEditClickHandler(View view) {

        if( listPosition < 0) {
            Toast.makeText(getApplicationContext(),
                    "Please, first Click on the record to select it", Toast.LENGTH_LONG).show();
        }else {
            isTermEditing = true;
            DataItem dataItem = new DataItem(dataAdapter.getItem(listPosition).getItem(),
                    dataAdapter.getItem(listPosition).getStartDate(),
                    dataAdapter.getItem(listPosition).getEndDate());
            dataItem.setItemId(termId);
            Intent intent = new Intent(this, DetailedTermActivity.class);
            intent.putExtra("serializeData", dataItem);
            startActivity(intent);
        }
    }

    public void btnDisplayCourseHandler(View view) {
        if( listPosition < 0){
            Toast.makeText(getApplicationContext(),
                    "Please, first Click on the record to select it", Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(this, ListCourseActivity.class);
            intent.putExtra("serializeData", termId);
            startActivity(intent);
        }

    }


    private void manageListView(){

        listTerm.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listTerm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                termId = list.get(position).getItemId();
                listPosition = position;


                listTerm.setBackgroundColor(5);



            }
        });


    }




    private void setTerms(){

        dbManager = new DbManager(this);
        dbManager.open();

        listTerm = findViewById(R.id.listTerm);

        try {
               // if( !noTerm) {
            long numItems =  dbManager.getRowCount(DbHelper.TABLE_TERM);
            if( numItems != 0 ) {

                list = dbManager.getAllTerms();
                dataAdapter = new TermListAdapter(this, R.layout.list_item, list);
                listTerm.setAdapter(dataAdapter);

            }
                else{
                Toast.makeText(this, "There are no terms",Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){


        }



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
                finish();
                isTermEditing = false;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_term:
                finish();
                isTermEditing = false;
                intent = new Intent(getApplicationContext(), DetailedTermActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_course:
                finish();
                isTermEditing = false;
                intent = new Intent(getApplicationContext(), CourseActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_assessment:
                finish();
                isTermEditing = false;
                intent = new Intent(getApplicationContext(), AssessmentActivity.class);
                startActivity(intent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }



}
