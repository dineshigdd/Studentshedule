package com.example.schedule.studentschedule.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.schedule.studentschedule.DbManager;
import com.example.schedule.studentschedule.Model.DataItem;
import com.example.schedule.studentshedule.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CommonControls extends AppCompatActivity {

    private int date;
    private int month;
    private int year;
    private TextView tv;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = addTextView("");

    }


    public void setDatePickerDialog(String textFortvLabel, String textFortv,int id){
        TextView tvLabel = addTextView(textFortvLabel);
        tv.setText(textFortv);
        setDate(tvLabel,tv,id );

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                date = calendar.get(Calendar.DAY_OF_MONTH);

                // TODO Auto-generated method stub
                datePickerDialog = new DatePickerDialog(CommonControls.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                tv.setText(month + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, date);

                datePickerDialog.show();
            }
        });

    }

    protected TextView addTextView(String label) {

        TextView tv = new TextView(this);
        tv.setText(label);
        RelativeLayout.LayoutParams tvLayoutDimensions = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(tvLayoutDimensions);
        tv.setText(label);
        tv.setPadding(8, 8, 8, 8);

        return tv;

    }

    private void setDate(TextView tvLabel,TextView tv, int id) {
        //RelativeLayout DateLayout = new RelativeLayout(this);
        tvLabel.setId(id);
        //DateLayout.addView(tvLabel);

        RelativeLayout.LayoutParams DateLayoutDimensions = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        DateLayoutDimensions.addRule(RelativeLayout.RIGHT_OF, tvLabel.getId());
        DateLayoutDimensions.addRule(RelativeLayout.END_OF, tvLabel.getId());

        tv.setLayoutParams(DateLayoutDimensions);
       // DateLayout.addView(tv);
    }
}