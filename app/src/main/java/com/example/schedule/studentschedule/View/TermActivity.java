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
import com.example.schedule.studentschedule.Model.DataItem;
import com.example.schedule.studentschedule.Model.TermListAdapter;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;

public class TermActivity extends AppCompatActivity {

    DbManager dbManager;
    private ArrayList<DataItem> list;
    private ArrayList<Button> btn;
    private  int listPosition;
    private ListView listTerm;
    private int termId;
    Button deleteBtn;
    public static boolean isTermEditing = false;
    private TermListAdapter dataAdapter;
    private Button btnDelete;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      //        deleteBtn.setVisibility(View.INVISIBLE);



        setTerms();
//        btn = new ArrayList<>(list.size());
//        for(int i = 0 ; i < list.size(); i ++ ) {
//            Button button = new Button(this);
//            btn.add(button);
//            btn.add(i, (Button)listTerm.findViewById(R.id.btnDeleteTerm));
//          //  btn.add(( listTerm.findViewById(R.id.btnDeleteTerm));
//        }
        Button btnAddATerm = findViewById(R.id.btnAddTerm);
        btnAddATerm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DetailedTermActivity.class);
                startActivity(intent);

            }
        });


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

    @Override
    protected void onStart() {
        super.onStart();

            setTerms();
            manageListView();


    }


    public void removeTerm(){
        String selection = DbHelper.TERM_ID + "=?";
                //+ "." + DbHelper.TERM_ID + "<>" +
              //  DbHelper.TABLE_ASSIGN + "." + DbHelper.TERM_ID;

        //String [] selectionArg = { Integer.toString(termId) };

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

    public  void buttonClickHandler(View view) {
        //Button btn = findViewById(R.id.btnAddTerm);
//        btn.setText(Integer.toString(listPosition));
//        Toast.makeText(this,Integer.toString(listPosition),Toast.LENGTH_LONG);
        try {
            removeTerm();
        }catch (Exception e){
                  Toast.makeText(this,"One or more course are assigned to this term. " +
                                  "You must first remove all courses to delete the term",
                    Toast.LENGTH_LONG ).show();
        }

    }

    public void BtnEditClickHandler(View view) {
       isTermEditing = true;
       DataItem dataItem = new DataItem(dataAdapter.getItem(listPosition).getItem(),
                    dataAdapter.getItem(listPosition).getStartDate(),
                    dataAdapter.getItem(listPosition).getEndDate());
       dataItem.setItemId(termId);
       Intent intent = new Intent(this, DetailedTermActivity.class );
       intent.putExtra("serializeData",dataItem);
       startActivity(intent);
    }

    public void btnDisplayCourseHandler(View view) {

        Intent intent = new Intent(this, ListCourseActivity.class );
        intent.putExtra("serializeData",termId);
        startActivity(intent);

    }


    private void manageListView(){

        listTerm.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listTerm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                termId = list.get(position).getItemId();
                listPosition = position;
                Toast.makeText(TermActivity.this, Integer.toString(list.get(position).getItemId()), Toast.LENGTH_SHORT).show();
             //   btn.add(position, (Button)listTerm.findViewById(R.id.btnDeleteTerm));

              //  btn.get(position).setVisibility(View.VISIBLE);

//                for (int i = 0; i <= list.size(); i++) {
//                    btn.add((btnDelete));
//                }

                listTerm.setBackgroundColor(5);

//                DataItem dataItem = new DataItem(dataAdapter.getItem(listPosition).getItem(),
//                        dataAdapter.getItem(listPosition).getStartDate(),
//                        dataAdapter.getItem(listPosition).getEndDate());
//
//                Intent intent = new Intent(TermActivity.this, DetailedTermActivity.class );
//                intent.putExtra("serializeData",dataItem);
//
//                startActivity(intent);


            }
        });
        //btnDelete.setVisibility(View.GONE);

    }




    private void setTerms(){

        dbManager = new DbManager(this);
        dbManager.open();
//        Spinner spinner = findViewById(R.id.spTerm);
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
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



}
