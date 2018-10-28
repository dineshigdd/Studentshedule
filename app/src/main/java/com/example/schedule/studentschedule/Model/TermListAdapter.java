package com.example.schedule.studentschedule.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.schedule.studentshedule.R;

import java.util.ArrayList;
import java.util.List;

public class TermListAdapter extends ArrayAdapter <DataItem>{
    private List<DataItem> termList;
    private int position;



    public TermListAdapter(Context context, int resource, ArrayList<DataItem> objects) {
        super(context, resource, objects);
        termList = objects;
    }

//    private View.OnClickListener btnDeleteTermListner = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            View parentrow = (View) v.getParent();
//            ListView listView = (ListView) parentrow.getParent();
//            final int position = listView.getPositionForView(parentrow);
//            //final int position = (Integer) v.getTag();
//
//        }
//    };


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        if( convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        DataItem term = termList.get(position);
        TextView termTitle = convertView.findViewById(R.id.title);
        termTitle.setText("Term:"+ term.getItem());
        TextView startDate = convertView.findViewById(R.id.startDate);
        startDate.setText("Start:" + term.getStartDate());
        TextView endDate = convertView.findViewById(R.id.endDate);
        endDate.setText("End:"+term.getEndDate());

        final Button btnDisplayCourse = convertView.findViewById(R.id.btnDisplayCourse);
        btnDisplayCourse.setText("Courses");
        final Button btnDelete = convertView.findViewById(R.id.btnDeleteTerm);
        btnDelete.setText("Delete");
        final Button btnEdit = convertView.findViewById(R.id.btnEditTerm);
        btnEdit.setText("Edit");

        this.position = position;
        //btnDelete.setOnClickListener(btnDeleteTermListner);


        return convertView;
    }




    }
